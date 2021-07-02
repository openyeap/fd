package ltd.fdsa.influxdb.service;

import com.influxdb.client.write.Point;
import lombok.var;
import ltd.fdsa.influxdb.entity.InfluxEntity;
import ltd.fdsa.influxdb.model.Region;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface InfluxService<Entity extends InfluxEntity> {
    List<Entity> findEntities(Map<String, String> filters, String from, String stop);

    default List<Entity> findEntities(Object filters, String from, String stop) {
        if (filters == null) {
            return Collections.emptyList();
        }
        try {
            var map = BeanUtils.describe(filters);
            return this.findEntities(map, from, stop);
        } catch (IllegalAccessException e) {
            return Collections.emptyList();
        } catch (InvocationTargetException e) {
            return Collections.emptyList();
        } catch (NoSuchMethodException e) {
            return Collections.emptyList();
        }
    }

    List<Entity> findEntitiesByRegion(Region region, Map<String, String> filters, String from, String stop);

    default List<Entity> findEntitiesByRegion(Region region, Object filters, String from, String stop) {
        if (filters == null) {
            return Collections.emptyList();
        }
        try {
            var map = BeanUtils.describe(filters);
            return this.findEntitiesByRegion(region, map, from, stop);
        } catch (IllegalAccessException e) {
            return Collections.emptyList();
        } catch (InvocationTargetException e) {
            return Collections.emptyList();
        } catch (NoSuchMethodException e) {
            return Collections.emptyList();
        }
    }

    List<Map<String, Object>> findPoints(String measurement, Map<String, String> filters, String from, String stop);

    default List<Map<String, Object>> findPoints(String measurement, Object filters, String from, String stop) {

        if (filters == null) {
            return Collections.emptyList();
        }
        try {
            var map = BeanUtils.describe(filters);
            return this.findPoints(measurement, map, from, stop);
        } catch (IllegalAccessException e) {
            return Collections.emptyList();
        } catch (InvocationTargetException e) {
            return Collections.emptyList();
        } catch (NoSuchMethodException e) {
            return Collections.emptyList();
        }

    }


    List<Map<String, Object>> findPointsByPolygon(String measurement, Map<String, String> filters, String from, String stop, String... cells);

    default List<Map<String, Object>> findPointsByPolygon(String measurement, Object filters, String from, String stop, String... cells) {
        if (filters == null) {
            return Collections.emptyList();
        }
        try {
            var map = BeanUtils.describe(filters);
            return this.findPointsByPolygon(measurement, map, from, stop, cells);
        } catch (IllegalAccessException e) {
            return Collections.emptyList();
        } catch (InvocationTargetException e) {
            return Collections.emptyList();
        } catch (NoSuchMethodException e) {
            return Collections.emptyList();
        }
    }

    List<Map<String, Object>> findPointsByRegion(String measurement, Region region, Map<String, String> filters, String from, String stop);

    default List<Map<String, Object>> findPointsByRegion(String measurement, Region region, Object filters, String from, String stop) {
        if (filters == null) {
            return Collections.emptyList();
        }
        try {
            var map = BeanUtils.describe(filters);
            return this.findPointsByRegion(measurement, region, map, from, stop);
        } catch (IllegalAccessException e) {
            return Collections.emptyList();
        } catch (InvocationTargetException e) {
            return Collections.emptyList();
        } catch (NoSuchMethodException e) {
            return Collections.emptyList();
        }
    }

    void writeEntities(Entity... entities);

    void writePoints(Point... points);
}
