package cn.zhumingwu.dataswitch.process;

import com.google.common.base.Strings;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.dataswitch.core.container.Plugin;
import cn.zhumingwu.dataswitch.core.model.Record;
import cn.zhumingwu.dataswitch.core.pipeline.Process;
import cn.zhumingwu.dataswitch.core.util.Utils;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@ToString
@Plugin(name = "表达式", description = "通过表达式增加数据")
public class ExpressionPipeline implements Process {
    private static final Map<String, Expression> cache = new HashMap<String, Expression>();
    private static final String EXPRESSION_KEY = "expression";
    private static final String NAME_KEY = "name";

    private Expression expression;
    private String field;

    static {
        // 自定义函数
        // AviatorEvaluator.addFunction(new stringSplit());
    }

    @Override
    public void init() {
        String expression = this.context().getString(EXPRESSION_KEY);
        if (!cache.containsKey(expression)) {
            cache.put(expression, AviatorEvaluator.compile(expression));
        }
        this.expression = cache.get(expression);
        this.field = this.context().getString(NAME_KEY);
    }

    @Override
    public void execute(Record... records) {
        // 判断是否在运行
        if (!this.isRunning()) {
            return;
        }
        // 执行表达式计算出新结果并放回数据记录
        for (var record : records) {
            Map<String, Object> env = new HashMap<String, Object>(128);
            for (var column : record.toNormalMap().entrySet()) {
                env.put(column.getKey(), column.getValue());
            }
            var result = this.expression.execute(env);
            if (Strings.isNullOrEmpty(this.field))
            {
                this.field = Utils.getName(env.keySet());
            }
            record.add(this.field, result);
        }
        // 下沉数据
        for (var item : this.nextSteps()) {
            item.execute(records);
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