package ltd.fdsa.fds.plugin.pipeline.implement;

 
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;  
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.fds.core.AbstractPlugin;
import ltd.fdsa.fds.core.DataPipeLine;
import ltd.fdsa.fds.core.config.Configuration;
import ltd.fdsa.fds.core.exception.FastDataSwitchException;

@Slf4j
public class ExpressionPipeLine extends AbstractPlugin implements DataPipeLine {
	private final static Map<String, Expression> cache = new HashMap<String, Expression>();
	Expression expression;

	@Override
	public void prepare(Configuration context) {
		String config = context.getString("expression",""); 
		if(Strings.isNullOrEmpty(config))
		{
			throw new  FastDataSwitchException("expression不能为空");			
		}
		if (!cache.containsKey(config)) {
			StringBuffer sb = new StringBuffer();
			sb.append("seq.map(");
			for (String item : config.split(",|;")) {
				String[] kv = item.split(":|=");
				sb.append("'");
				sb.append(kv[0]);
				sb.append("',");
				sb.append(kv[1]);
				sb.append(",");
			}
			sb.append("'time',");
			sb.append("sysdate()");
			sb.append(")");
			String epx = sb.toString();
			// 编译表达式
			Expression ep = AviatorEvaluator.compile(epx);
			// AviatorEvaluator.addFunction(new ReturnFunction());
			cache.put(config, ep);
		}
	 this.expression = cache.get(config);
	}
	public Map<String, Object> process(Map<String, Object> data) {
		Map<String, Object> env = new HashMap<String, Object>(1);
		env.put("data", data);
		// 执行表达式
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) this.expression.execute(env);
		return result;
	}
 

}
