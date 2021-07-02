package ltd.fdsa.influxdb.service;

import com.google.common.base.Strings;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.context.ApplicationContextHolder;
import ltd.fdsa.influxdb.entity.InfluxEntity;
import ltd.fdsa.influxdb.model.EntityInfo;
import ltd.fdsa.influxdb.model.Location;
import ltd.fdsa.influxdb.model.Region;
import ltd.fdsa.influxdb.properties.InfluxProperties;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.cache.Cache;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.ParameterizedType;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public
//abstract
class BaseInfluxService<Entity extends InfluxEntity> implements InfluxService<Entity> {

    private final Cache cache;
    private final InfluxDBClient influxDBClient;
    private final InfluxProperties properties;
    private final MapperFacade mapper;
    private Class<Entity> clazz;

    public BaseInfluxService(InfluxDBClient influxDBClient, InfluxProperties properties, Cache cache) {
        this.cache = cache;
        this.influxDBClient = influxDBClient;
        this.properties = properties;

        var currentClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.clazz = (Class<Entity>) currentClass.getActualTypeArguments()[0];
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        this.mapper = mapperFactory.getMapperFacade();
    }

    @Override
    public List<Entity> findEntities(Map<String, String> filters, String from, String stop) {

        var measurement = getMeasurement();
        if (Strings.isNullOrEmpty(measurement)) {
            return Collections.emptyList();
        }
        var query = FluxQuery.builder()
                .filter(measurement, this.properties.getBucket(), filters, from, stop)
                .includes(this.getColumns()).build();
        var map = this.execFlux(query.getFlux());
        return mapper.mapAsList(map, this.clazz);
    }

    @Override
    public List<Entity> findEntitiesByRegion(Region region, Map<String, String> filters, String from, String stop) {
        var measurement = getMeasurement();
        if (Strings.isNullOrEmpty(measurement)) {
            return Collections.emptyList();
        }
        var query = FluxQuery.builder()
                .filter(measurement, this.properties.getBucket(), filters, from, stop)
                .region(region)
                .includes(this.getColumns())
                .build();
        var map = this.execFlux(query.getFlux());
        return mapper.mapAsList(map, this.clazz);
    }

    @Override
    public List<Map<String, Object>> findPoints(String measurement, Map<String, String> filters, String from, String stop) {
        var query = FluxQuery.builder().filter(measurement, this.properties.getBucket(), filters, from, stop).build();
        return execFlux(query.getFlux());
    }

    @Override
    public List<Map<String, Object>> findPointsByPolygon(String measurement, Map<String, String> filters, String from, String stop, String... cells) {
        var query = FluxQuery.builder().filter(measurement, this.properties.getBucket(), filters, from, stop).cells(cells).build();
        return this.execFlux(query.getFlux());
    }

    @Override
    public List<Map<String, Object>> findPointsByRegion(String measurement, Region region, Map<String, String> filters, String from, String stop) {
        var query = FluxQuery.builder().filter(measurement, this.properties.getBucket(), filters, from, stop).region(region).build();
        return this.execFlux(query.getFlux());
    }

    private List<Map<String, Object>> execFlux(String flux) {
        log.info("flux:\n{}", flux);
        List<Map<String, Object>> result = new LinkedList<>();
        // 开始查询
        try {
            var queryApi = this.influxDBClient.getQueryApi();

            var fluxTables = queryApi.query(flux);
            // 获取查询结果
            for (var table : queryApi.query(flux)) {
                for (var record : table.getRecords()) {
                    var map = record.getValues();
                    if (map.containsKey("lat") && map.containsKey("lon")) {
                        map.put("point", Location.builder().lat((double) map.get("lat")).lon((double) map.get("lon")).build());
                        map.remove("lat");
                        map.remove("lon");
                    } else if (map.containsKey("s2_cell_id")) {
                        var point = S2CellId.fromToken((String) map.get("s2_cell_id")).toLatLng();
                        map.put("point", Location.builder().lat(point.latDegrees()).lon(point.lngDegrees()));
                    }
                    map.remove("result");
                    map.remove("table");
                    map.remove("_measurement");
                    map.remove("_start");
                    map.remove("_stop");
                    map.put("time", map.remove("_time"));
                    result.add(map);
                }
            }
            return result;
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    private String getMeasurement() {
        if (this.clazz == null) {
            return "";
        }
        var entityInfo = this.cache.get(clazz, EntityInfo.class);
        if (entityInfo == null) {
            entityInfo = genEntityInfo();
        }
        return entityInfo.getName();
    }

    private String[] getColumns() {
        if (this.clazz == null) {
            return new String[0];
        }
        var entityInfo = this.cache.get(clazz, EntityInfo.class);
        if (entityInfo == null) {
            entityInfo = genEntityInfo();
        }
        List<String> list = new ArrayList<>();
        list.addAll(entityInfo.getTags().keySet());
        list.addAll(entityInfo.getFields().keySet());
        return list.toArray(new String[0]);
    }


    @Override
    public void writeEntities(Entity... entities) {
        if (entities.length <= 0) {
            return;
        }
        if (entities.length == 1) {
            insert(entities[0]);
            return;
        }
        var list = Arrays.stream(entities).map(m -> getPointFromPOJO(m)).collect(Collectors.toList());

        try (var write = this.influxDBClient.getWriteApi()) {
            write.writePoints(list);
        }
    }

    @Override
    public void writePoints(Point... points) {
        try (var writer = this.influxDBClient.getWriteApi()) {
            writer.writePoints(Arrays.asList(points));
        } catch (Exception exception) {
            log.error("writePoints failed:", exception);
        }
    }

    private void insert(Entity entity) {
        var point = getPointFromPOJO(entity);
        try (var writer = this.influxDBClient.getWriteApi()) {
            writer.writePoint(point);
        } catch (Exception exception) {
            log.error("", exception);
        }
    }

    private EntityInfo genEntityInfo() {
        var builder = EntityInfo.builder();
        var measurement = clazz.getAnnotation(Measurement.class);
        if (measurement == null) {
            builder.name(clazz.getName());
        } else {
            builder.name(measurement.name());
        }
        ReflectionUtils.doWithFields(this.clazz, field -> {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            var column = field.getAnnotation(Column.class);
            if (column == null) {
                builder.field(field.getName(), field);
                return;
            }
            if (column.timestamp()) {
                return;
            }
            if (column.tag()) {
                builder.tag(column.name(), field);
                if ("s2_cell_id".equals(column.name())) {
                    builder.field("lat", null);
                    builder.field("lon", null);
                }
                return;
            }
            builder.field(column.name(), field);
        });
        var entityInfo = builder.build();
        cache.put(clazz, entityInfo);
        return entityInfo;
    }

    private Point getPointFromPOJO(Entity entity) {
        if (this.clazz == null) {
            this.clazz = (Class<Entity>) entity.getClass();
        }
        var entityInfo = this.cache.get(clazz, EntityInfo.class);
        if (entityInfo == null) {
            entityInfo = genEntityInfo();
        }
        if (entity.getTime() == null) {
            entity.setTime(Instant.now());
        }
        var result = Point.measurement(entityInfo.getName()).time(entity.getTime(), WritePrecision.MS);

        for (var key : entityInfo.getFields().entrySet()) {
            var name = key.getKey();
            var field = key.getValue();
            if (field == null) {
                continue;
            }
            try {
                var value = field.get(entity);
                if (value == null) {
                    continue;
                }
                if (long.class.equals(value.getClass())) {
                    result.addField(name, (long) value);
                } else if (Number.class.equals(value.getClass())) {
                    result.addField(name, (Number) value);
                } else if (boolean.class.equals(value.getClass())) {
                    result.addField(name, (boolean) value);
                } else if (double.class.equals(value.getClass())) {
                    result.addField(name, (double) value);
                } else if (S2LatLng.class.equals(value.getClass())) {
                    var loc = (S2LatLng) value;
                    var lat = loc.latDegrees();
                    var lon = loc.lngDegrees();
                    var s2CellId = S2CellId.fromLatLng(loc).parent(30);
                    result.addTag("s2_cell_id", s2CellId.toToken());
                    result.addField("lat", lat);
                    result.addField("lon", lon);
                } else {
                    result.addField(name, value.toString());
                }
            } catch (IllegalAccessException e) {
                log.error("getPointFromPOJO failed:", e);
            }
        }
        for (var key : entityInfo.getTags().entrySet()) {
            var field = key.getValue();
            try {
                var value = field.get(entity);
                if (value == null) {
                    continue;
                }
                result.addTag(key.getKey(), value.toString());
            } catch (IllegalAccessException e) {
            }
        }
        return result;
    }
}
