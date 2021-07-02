package ltd.fdsa.switcher.plugin;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.switcher.core.pipeline.Process;
import ltd.fdsa.switcher.core.config.Configuration;
import ltd.fdsa.switcher.core.pipeline.impl.AbstractPipeline;
import ltd.fdsa.web.view.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@ToString
public class ExpressionPipeline extends AbstractPipeline implements Process {
    private static final Map<String, Expression> cache = new HashMap<String, Expression>();
    private static final String EXPRESSION_CONTENT_KEY = "expression";
    private Expression expression;

    @Override
    public Result<String> init(Configuration configuration) {
        var result = super.init(configuration);
        if (result.getCode()==200) {
            String expression = this.config.getString(EXPRESSION_CONTENT_KEY);
            if (!cache.containsKey(expression)) {
                List<String> sb = new ArrayList<>();
                for (String item : expression.split(";")) {
                    String[] kv = item.split("=");
                    sb.add("'" + kv[0].trim() + "'," + kv[1].trim());
                }
                String epx = String.join(",", sb);
                // 自定义函数
//            AviatorEvaluator.addFunction(new stringSplit());
                // 编译表达式
                Expression ep = AviatorEvaluator.compile("seq.map(" + epx.toString() + ")"
                );
                cache.put(expression, ep);
            }
            this.expression = cache.get(expression);
            return Result.success();
        }
        return result;

    }

    @Override
    public void collect(Map<String, Object>... records) {
        if (!this.isRunning()) {
            return;
        }
        for (var record : records) {
            Map<String, Object> env = new HashMap<String, Object>(1);
            env.put("db", record);
            Map<String, Object> result = (Map<String, Object>) this.expression.execute(env);
            for (var item : this.nextSteps) {
                item.collect(records);
            }
        }
    }


    class stringSplit extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
            String input = FunctionUtils.getStringValue(arg1, env);
            String value = FunctionUtils.getStringValue(arg2, env);
            String[] list = input.split(value);
            return new AviatorRuntimeJavaType(list);
        }

        @Override
        public String getName() {
            return "string.split";
        }
    }
}