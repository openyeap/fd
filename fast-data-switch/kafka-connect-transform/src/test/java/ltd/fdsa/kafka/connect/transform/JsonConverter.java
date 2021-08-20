//package ltd.fdsa.kafka.connect.transform;
//
//import org.apache.kafka.common.serialization.Deserializer;
//import org.apache.kafka.common.serialization.Serializer;
//import org.apache.kafka.connect.data.Schema;
//import org.apache.kafka.connect.data.SchemaAndValue;
//import org.apache.kafka.connect.storage.Converter;
//
//import java.util.Map;
//
//public class JsonConverter implements Converter {
//    @Override
//    public void configure(Map<String, ?> configs, boolean isKey) {
//
//        JsonSchemaConverterConfig jsonSchemaConverterConfig = new JsonSchemaConverterConfig(configs);
//
//
//        serializer = new Serializer(configs, schemaRegistry);
//        deserializer = new Deserializer(configs, schemaRegistry);
//        jsonSchemaData = new JsonSchemaData(new JsonSchemaDataConfig(configs));
//    }
//
//
//    @Override
//    public byte[] fromConnectData(String topic, Schema schema, Object value) {
//        if (schema == null && value == null) {
//            return null;
//        }
//
//        JsonSchema jsonSchema = jsonSchemaData.fromConnectSchema(schema);
//        JsonNode jsonValue = jsonSchemaData.fromConnectData(schema, value);
//        try {
//            return serializer.serialize(topic, isKey, jsonValue, jsonSchema);
//        } catch (SerializationException e) {
//            throw new DataException(String.format("Converting Kafka Connect data to byte[] failed due to "
//                            + "serialization error of topic %s: ",
//                    topic),
//                    e
//            );
//        } catch (InvalidConfigurationException e) {
//            throw new ConfigException(
//                    String.format("Failed to access JSON Schema data from "
//                            + "topic %s : %s", topic, e.getMessage())
//            );
//        }
//    }
//
//    @Override
//    public SchemaAndValue toConnectData(String topic, byte[] value) {
//        try {
//            JsonSchemaAndValue deserialized = deserializer.deserialize(topic, isKey, value);
//
//            if (deserialized == null || deserialized.getValue() == null) {
//                return SchemaAndValue.NULL;
//            }
//
//            JsonSchema jsonSchema = deserialized.getSchema();
//            Schema schema = jsonSchemaData.toConnectSchema(jsonSchema);
//            return new SchemaAndValue(schema, jsonSchemaData.toConnectData(schema,
//                    (JsonNode) deserialized.getValue()));
//        } catch (SerializationException e) {
//            throw new DataException(String.format("Converting byte[] to Kafka Connect data failed due to "
//                            + "serialization error of topic %s: ",
//                    topic),
//                    e
//            );
//        } catch (InvalidConfigurationException e) {
//            throw new ConfigException(
//                    String.format("Failed to access JSON Schema data from "
//                            + "topic %s : %s", topic, e.getMessage())
//            );
//        }
//    }
//
//}