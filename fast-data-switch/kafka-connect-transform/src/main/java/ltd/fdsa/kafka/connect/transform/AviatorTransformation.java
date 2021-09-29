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
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.*;
import org.apache.kafka.connect.transforms.Transformation;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AviatorTransformation<R extends ConnectRecord<R>> implements Transformation<R> {
    AviatorConfig config;
    Schema schema;
    protected SchemaAndValue process(R record, Object value) {
        if (schema != null) {
            return new SchemaAndValue(schema, value);
        }
        if (value == null) {
            return new SchemaAndValue(SchemaBuilder.string().optional().build(), null);
        }
        if (value instanceof String) {
            this.schema = SchemaBuilder.string().optional().build();
        } else if (value instanceof Byte) {
            this.schema = SchemaBuilder.int8().optional().build();
        } else if (value instanceof Character) {
            this.schema = SchemaBuilder.int16().optional().build();
        } else if (value instanceof Integer) {
            this.schema = SchemaBuilder.int32().optional().build();
        } else if (value instanceof Long) {
            this.schema = SchemaBuilder.int64().optional().build();
        } else if (value instanceof Float) {
            this.schema = SchemaBuilder.float32().optional().build();
        } else if (value instanceof java.lang.Double) {
            this.schema = SchemaBuilder.float64().optional().build();
        } else if (value instanceof Boolean) {
            this.schema = SchemaBuilder.bool().optional().build();
        } else {
            log.debug(value.getClass().toString());
            this.schema = SchemaBuilder.bytes().optional().build();
        }
        return new SchemaAndValue(schema, value);
    }

    @Override
    public R apply(R record) {
        if (null == record.valueSchema() || Schema.Type.STRUCT != record.valueSchema().type()) {
            log.trace("record.valueSchema() is null or record.valueSchema() is not a struct.");
            return record;
        }
        var schemaBuilder = SchemaBuilder.struct();
        for (var field : record.valueSchema().fields()) {
            schemaBuilder.field(field.name(), field.schema());
        }
        // 得到record中的数据
        Struct inputStruct = (Struct) record.value();
        Map<String, Object> env = new HashMap<String, Object>(record.valueSchema().fields().size());
        for (var field : record.valueSchema().fields()) {
            env.put(field.name(), inputStruct.get(field));
        }
        // 得到计算结果
        var value = this.config.expression.execute(env);
        var schemaAndValue = process(record, value);
        schemaBuilder.field(this.config.field, schemaAndValue.schema());

        // 重构输入结果
        Struct outputStruct = new Struct(schemaBuilder.build());
        for (var field : record.valueSchema().fields()) {
            outputStruct.put(field.name(), inputStruct.get(field));
        }
        outputStruct.put(this.config.field, schemaAndValue.value());

        return record.newRecord(record.topic(), record.kafkaPartition(), record.keySchema(), record.key(), outputStruct.schema(), outputStruct, record.timestamp());
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
