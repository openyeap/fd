
package cn.zhumingwu.dataswitch.plugin;

import com.google.iot.cbor.CborConversionException;
import com.google.iot.cbor.CborMap;
import com.google.iot.cbor.CborObject;
import com.google.iot.cbor.CborParseException;
import io.debezium.connector.postgresql.PostgresConnector;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.kafka.connect.data.*;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CDCTest {


    public byte[] fromConnectData(String topic, Schema schema, Object value) {
        if (schema == null || value == null) {
            return null;
        }
        try {
            Struct struct = (Struct) value;
            if (struct == null) {
                return null;
            }

            CborMap map = CborMap.create();
            for (var field : schema.fields()) {
                log.info("field:{}", field);
                var object = struct.get(field.name());

//              var sss =  CborByteString.create(null);
                map.put(field.name(), CborObject.createFromJavaObject(object));
            }
            log.info("map: {}", map.toJsonString());
            return map.toCborByteArray();
        } catch (CborConversionException ex) {
            log.error("JsonProcessingException:", ex);
            return new byte[0];
        }
    }

    public SchemaAndValue toConnectData(String topic, byte[] value) {
        if (value == null) {
            log.info("value is null");
            return SchemaAndValue.NULL;
        }
        if (value.length <= 0) {
            log.info("value is empty");
            return SchemaAndValue.NULL;
        }
        try {
            var map = CborMap.createFromCborByteArray(value);
            var builder = SchemaBuilder.struct();
            for (var entry : map.entrySet()) {
                var name = entry.getKey().toJsonString();
                var object = entry.getValue().toJavaObject();
                if (object instanceof Boolean) {
                    builder.field(name, SchemaBuilder.bool().optional().build());
                } else if (object instanceof Byte) {
                    builder.field(name, SchemaBuilder.int8().optional().build());
                } else if (object instanceof Short) {
                    builder.field(name, SchemaBuilder.int16().optional().build());
                } else if (object instanceof Integer) {
                    builder.field(name, SchemaBuilder.int32().optional().build());
                } else if (object instanceof Long) {
                    builder.field(name, SchemaBuilder.int64().optional().build());
                } else if (object instanceof Float) {
                    builder.field(name, SchemaBuilder.float32().optional().build());
                } else if (object instanceof Double) {
                    builder.field(name, SchemaBuilder.float64().optional().build());
                } else if (object instanceof java.sql.Time) {
                    builder.field(name, Time.SCHEMA);
                } else if (object instanceof java.sql.Timestamp) {
                    builder.field(name, Timestamp.SCHEMA);
                } else if (object instanceof java.sql.Date) {
                    builder.field(name, Date.SCHEMA);
                } else if (object instanceof BigDecimal) {
                    builder.field(name, Decimal.schema(3));
                } else {
                    builder.field(name, SchemaBuilder.string().optional().build());
                }
            }
            var schema = builder.schema();
            Struct result = new Struct(schema.schema());
            for (var entry : map.entrySet()) {
                var name = entry.getKey().toJsonString();
                var object = entry.getValue().toJavaObject();
                result.put(name, object);
            }
            log.info("struct:{}", result);
            return new SchemaAndValue(schema, result);

        } catch (CborParseException e) {
            log.error("ConnectData failed:{}", e);
            return SchemaAndValue.NULL;
        }
    }

    @Test
    public void testPgSQLDataChange() throws IOException {
        final Properties props = new Properties();
        props.setProperty("name", "engine"); //This property is the kafka connector name, which is required by all Kafka Connect connectors.
        props.setProperty("connector.class", PostgresConnector.class.getCanonicalName());
        props.setProperty("offset.storage", org.apache.kafka.connect.storage.FileOffsetBackingStore.class.getCanonicalName());
        props.setProperty("offset.storage.file.filename", "/var/offsets.dat");
        props.setProperty("offset.flush.interval.ms", "60000");
        /* begin connector properties */
        props.setProperty("database.hostname", "10.168.4.49");
        props.setProperty("database.port", "5321");
        props.setProperty("database.user", "postgres");
        props.setProperty("database.password", "postgres");

        props.setProperty("database.server.name", "test001-connector");// 可以任意修改
        props.setProperty("database.serverTimezone", "UTC"); // 时区，建议使用UTC
        props.setProperty("database.dbname", "dev");  //数据库名
        props.setProperty("schema.include.list", "public"); // 模式名
        props.setProperty("table.exclude.list", "t_person"); // 库.表名
        props.setProperty("database.history", "io.debezium.relational.history.FileDatabaseHistory");
        props.setProperty("database.history.file.filename", "/var/dbhistory.dat");
        props.setProperty("tasks.max", "1");
        props.setProperty("plugin.name", "pgoutput");


        DebeziumEngine engine = DebeziumEngine.create(Connect.class)
                .using(props)
                .notifying((records, committer) -> {
                    for (var record : records) {
                        var sourceRecord = record.value();
                        var topic = sourceRecord.topic();
                        var schema = sourceRecord.valueSchema();
                        var before = schema.field("before");
                        var after = schema.field("after");
                        var source = schema.field("source");
                        var op = schema.field("op");
                        var ts = schema.field("ts_ms");
                        var transaction = schema.field("transaction");
                        var value = ((Struct) sourceRecord.value()).get(after);
                        this.fromConnectData(topic, after.schema(), value);
                    }
                    // 当前批次处理完后，需要标记为完成
                    committer.markBatchFinished();
                })
                .using((success, message, error) -> {
                    // 省略代码
                    if (!success) {
                        System.out.println(message);
                    }
                }).build();
//          engine = DebeziumEngine.create(Json.class)
//                .using(props)
//                .notifying((records, committer) -> {
//                    for (var record : records) {
//
//
//                        System.out.println("recordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecord");
//                        System.out.println(record.destination());
//                        System.out.println("recordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecord");
//                        System.out.println(record.value());
//                        System.out.println("recordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecordrecord");
//                    }
//                    // 当前批次处理完后，需要标记为完成
//                    committer.markBatchFinished();
//                })
//                .using((success, message, error) -> {
//                    // 省略代码
//                    if (!success) {
//                        System.out.println(message);
//                    }
//                }).build();
        try {
            // Run the engine asynchronously ...
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(engine);
            // Do something else or wait for a signal or an event
            Thread.sleep(10000);

        } catch (Exception e) {
        } finally {
            engine.close();
        }
    }
}
