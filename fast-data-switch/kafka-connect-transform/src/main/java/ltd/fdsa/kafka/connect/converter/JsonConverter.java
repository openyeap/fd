package ltd.fdsa.kafka.connect.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.kafka.connect.data.*;
import org.apache.kafka.connect.data.Date;
import org.apache.kafka.connect.storage.Converter;

import java.io.IOException;
import java.util.*;

@Slf4j
public class JsonConverter implements Converter {
    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }


    @Override
    public byte[] fromConnectData(String topic, Schema schema, Object value) {

        if (schema == null || value == null) {
            return null;
        }
        try {
            Struct struct = (Struct) value;
            if (struct == null) {
                return null;
            }
            Map<String, Object> map = new LinkedHashMap<>();
            for (var field : schema.fields()) {
                log.info("field:{}", field);
                var type = field.schema().type().name();
                if (field.schema().name() == null) {
                    type = field.schema().name().replace("org.apache.kafka.connect.data.", "");
                }
                map.put(field.name(), type + ":" + struct.get(field.name()).toString());
            }
            log.info("map: {}", map);
            return this.objectMapper.writeValueAsBytes(map);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException:", e);
            return new byte[0];
        }
    }

    @Override
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
            log.info("to connect:{}", new String(value));
            var objectMapper = new ObjectMapper();
            var node = objectMapper.readTree(value);
            var nodeType = node.getNodeType();
            switch (nodeType) {
//                case POJO:
//                    var pojo = (POJONode) node;
//                    pojo.getPojo();
                case OBJECT:
                    var builder = SchemaBuilder.struct();
                    var values = readSchema(node, builder);

                    var schema = builder.schema();
                    Struct result = new Struct(schema.schema());
                    for (var field : schema.fields()) {
                        result.put(field, values.get(field.name()));
                    }
                    log.info("struct:{}", result);
                    return new SchemaAndValue(schema, result);
                default:
                    return new SchemaAndValue(SchemaBuilder.string().field("text").schema(), node.asText());
            }

        } catch (IOException e) {
            log.error("ConnectData failed:{}", e);
            return SchemaAndValue.NULL;
        }

    }

    private Map<String, Object> readSchema(JsonNode node, SchemaBuilder builder) {
        Map<String, Object> map = new HashMap<>();
        for (Iterator<String> it = node.fieldNames(); it.hasNext(); ) {
            String name = it.next();
            var n = node.get(name);
            var value = n.asText();
            var index = value.indexOf(":");
            var prefix = value.substring(0, index);
            switch (prefix) {
                case "BOOLEAN":
                    builder.field(name, SchemaBuilder.bool().optional().build());
                    map.put(name, value.substring(index + 1));
                    break;
                case "INT8":
                    builder.field(name, SchemaBuilder.int8().optional().build());
                    map.put(name, value.substring(index + 1));
                    break;
                case "INT16":
                    builder.field(name, SchemaBuilder.int16().optional().build());
                    map.put(name, value.substring(index + 1));
                    break;
                case "INT32":
                    builder.field(name, SchemaBuilder.int32().optional().build());
                    map.put(name, value.substring(index + 1));
                    break;
                case "INT64":
                    builder.field(name, SchemaBuilder.int64().optional().build());
                    map.put(name, value.substring(index + 1));
                    break;
                case "FLOAT32":
                    builder.field(name, SchemaBuilder.float32().optional().build());
                    map.put(name, value.substring(index + 1));
                    break;
                case "FLOAT64":
                    builder.field(name, SchemaBuilder.float64().optional().build());
                    map.put(name, value.substring(index + 1));
                    break;
                case "Time":
                    builder.field(name, Time.SCHEMA);
                    map.put(name, value.substring(index + 1));
                    break;
                case "Timestamp":
                    builder.field(name, Timestamp.SCHEMA);
                    map.put(name, value.substring(index + 1));
                    break;
                case "Date":
                    builder.field(name, Date.SCHEMA);
                    map.put(name, value.substring(index + 1));
                    break;
                case "Decimal":
                    builder.field(name, Decimal.schema(3));
                    map.put(name, value.substring(index + 1));
                    break;
                default:
                    builder.field(name, SchemaBuilder.string().optional().build());
                    map.put(name, value.substring(index + 1));
                    break;
            }
        }
        return map;
    }
}