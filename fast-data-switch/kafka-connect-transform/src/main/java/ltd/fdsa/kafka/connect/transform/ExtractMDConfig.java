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

import com.google.common.base.Strings;
import lombok.var;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.protocol.types.Field;

import java.util.List;
import java.util.Map;

public class ExtractMDConfig extends AbstractConfig {
    public final String fieldName;
    public final String methodName;
    public final List<String> includes;

    public ExtractMDConfig(Map<String, ?> settings) {
        super(config(), settings);
        var name = this.getString(FIELD_NAME_CONF);
        if (Strings.isNullOrEmpty(name)) {
            this.fieldName = "sid";
        } else {
            this.fieldName = name;
        }
        name = this.getString(METHOD_NAME_CONF);
        if (Strings.isNullOrEmpty(name)) {
            this.methodName = "MD5";
        } else {
            this.methodName = name;
        }
        this.includes = this.getList(INCLUDES_CONF);
    }

    public static final String INCLUDES_CONF = "includes";
    static final String INCLUDES_DOC = "The fields name to generate record id." +
            "For example if you wanted the extract `name` and `age`, you would use `name,age`.";
    public static final String FIELD_NAME_CONF = "field";
    static final String FIELD_NAME_DOC = "The field name for generated field." +
            "For example if you wanted the extract `id`, you would use `id`.";
    public static final String METHOD_NAME_CONF = "method";
    static final String METHOD_NAME_DOC = "the method will be used to extract id." +
            "For example `md5`, `hash`.";

    public static ConfigDef config() {
        return new ConfigDef()
                .define(FIELD_NAME_CONF, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, FIELD_NAME_DOC)
                .define(INCLUDES_CONF, ConfigDef.Type.LIST, ConfigDef.Importance.HIGH, INCLUDES_DOC)
                .define(METHOD_NAME_CONF, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, METHOD_NAME_DOC);
    }

}
