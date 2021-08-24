/**
 * Copyright © 2017 Jeremy Custenborder (jcustenborder@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ltd.fdsa.kafka.connect.transform;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.*;
import org.apache.kafka.connect.transforms.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class AviatorTransformation<R extends ConnectRecord<R>> implements Transformation<R> {
    AviatorConfig config;

    Map<Schema, List<String>> fieldMappings = new HashMap<>();

    @Override
    public ConnectRecord apply(ConnectRecord record) {
        if (null == record.valueSchema() || Schema.Type.STRUCT != record.valueSchema().type()) {
            log.trace("record.valueSchema() is null or record.valueSchema() is not a struct.");
            return record;
        }
        // 得到record中的数据
        Struct inputStruct = (Struct) record.value();
        Map<String, Object> env = new HashMap<String, Object>(record.valueSchema().fields().size());
        for (var field : record.valueSchema().fields()) {
            env.put(field.name(), inputStruct.get(field));
        }
        env.put(this.config.contextName, record);
        Map<String, Object> result = (Map<String, Object>) this.config.expression.execute(env);
// 根据计算结果构建结构
        var schemaBuilder = SchemaBuilder.struct();
        for (var field : result.entrySet()) {
            var value = field.getValue();
            if (value instanceof String) {
                schemaBuilder.field(field.getKey(), Schema.STRING_SCHEMA);
            } else if (value instanceof Byte) {
                schemaBuilder.field(field.getKey(), Schema.INT8_SCHEMA);
            } else if (value instanceof Character) {
                schemaBuilder.field(field.getKey(), Schema.INT16_SCHEMA);
            } else if (value instanceof Integer) {
                schemaBuilder.field(field.getKey(), Schema.INT32_SCHEMA);
            } else if (value instanceof Long) {
                schemaBuilder.field(field.getKey(), Schema.INT64_SCHEMA);
            } else if (value instanceof Float) {
                schemaBuilder.field(field.getKey(), Schema.FLOAT32_SCHEMA);
            } else if (value instanceof java.lang.Double) {
                schemaBuilder.field(field.getKey(), Schema.FLOAT64_SCHEMA);
            } else if (value instanceof Boolean) {
                schemaBuilder.field(field.getKey(), Schema.BOOLEAN_SCHEMA);
            } else if (value instanceof Bytes) {
                schemaBuilder.field(field.getKey(), Schema.BYTES_SCHEMA);
            } else {
                schemaBuilder.field(field.getKey(), Schema.STRING_SCHEMA);
            }
        }
        Struct outputStruct = new Struct(schemaBuilder.schema());
        for (var field : result.entrySet()) {
            var value = field.getValue();
            outputStruct.put(field.getKey(), value);
        }
        return record.newRecord(
                record.topic(),
                record.kafkaPartition(),
                record.keySchema(),
                record.key(),
                outputStruct.schema(),
                outputStruct,
                record.timestamp()
        );
    }

    @Override
    public ConfigDef config() {
        return DigestConfig.config();
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> settings) {
        this.config = new AviatorConfig(settings);
    }
}
