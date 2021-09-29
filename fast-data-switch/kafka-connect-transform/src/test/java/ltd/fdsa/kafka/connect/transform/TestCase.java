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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.transforms.Transformation;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class TestCase {
    Expression[] expressions;

    @Before
    public void before() {
        expressions = new Expression[]{
                AviatorEvaluator.compile("nil"),
                AviatorEvaluator.compile("first_name + ' ' + last_name"),
                AviatorEvaluator.compile("age"),
                AviatorEvaluator.compile("now()"),
                AviatorEvaluator.compile("now()-age"),
        };
    }

    @Test
    public void testSend() {
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("first_name", "zhu");
        env.put("last_name", "mingwu");
        env.put("age", 18);
        env.put("salary", 10000L);
        env.put("amount", 1000.0F);

        try {
            for (var ep : this.expressions) {
                var value = ep.execute(env);
                if (value != null) {
                    log.debug("class: {}, value: {}, string: {}", value.getClass(), value,value.toString());
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
