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

import com.github.jcustenborder.kafka.connect.utils.config.Description;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.transforms.Transformation;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Description("Transformation to generate record id")
public class ExtractMDTransformation<R extends ConnectRecord<R>> implements Transformation<R> {
    ExtractMDConfig config;

    Map<Schema, List<String>> fieldMappings = new HashMap<>();

    @Override
    public ConnectRecord apply(ConnectRecord record) {
        if (null == record.valueSchema() || Schema.Type.STRUCT != record.valueSchema().type()) {
            log.trace("record.valueSchema() is null or record.valueSchema() is not a struct.");
            return record;
        }

        var schemaBuilder = SchemaBuilder.struct();
        for (var field : record.valueSchema().fields()) {
            schemaBuilder.field(field.name(), field.schema());
        }
        schemaBuilder.field(config.fieldName, SchemaBuilder.string().schema());
        Struct outputStruct = new Struct(schemaBuilder.schema());

        Struct inputStruct = (Struct) record.value();
        for (var field : record.valueSchema().fields()) {
            outputStruct.put(field.name(), inputStruct.get(field));
        }

        try {
            MessageDigest m = MessageDigest.getInstance(config.methodName);
            var x = "";
            for (var field : this.config.includes) {
                x += ";" + inputStruct.getString(field);
            }
            m.update(x.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
            }
            outputStruct.put(config.fieldName, result);
            return record.newRecord(
                    record.topic(),
                    record.kafkaPartition(),
                    record.keySchema(),
                    record.key(),
                    outputStruct.schema(),
                    outputStruct,
                    record.timestamp()
            );
        } catch (NoSuchAlgorithmException e) {
            int hash = 0;
            for (var field : this.config.includes) {
                hash &= inputStruct.get(field).hashCode();
            }
            outputStruct.put(config.fieldName, hash);
            return record.newRecord(
                    record.topic(),
                    record.kafkaPartition(),
                    record.keySchema(),
                    record.key(),
                    outputStruct.schema(),
                    outputStruct,
                    record.timestamp()
            );
        } catch (UnsupportedEncodingException ex) {
            log.error("Exception thrown while parsing message.", ex);
            return record;
        }


    }

    @Override
    public ConfigDef config() {
        return ExtractMDConfig.config();
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> settings) {
        this.config = new ExtractMDConfig(settings);
    }
}
