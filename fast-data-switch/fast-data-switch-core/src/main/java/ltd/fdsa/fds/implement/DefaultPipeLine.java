package ltd.fdsa.fds.implement;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.fds.core.AbstractPlugin;
import ltd.fdsa.fds.core.DataPipeLine;
import ltd.fdsa.fds.core.JobContext;
import ltd.fdsa.fds.core.PluginType;
import ltd.fdsa.fds.core.RecordCollector;
import ltd.fdsa.fds.core.config.Configuration;

@Slf4j
public class DefaultPipeLine extends  AbstractPlugin implements DataPipeLine  {
	private final static Map<String, Expression> cache = new HashMap<String, Expression>();
	private final static String EXPRESSION_CONTENT_KEY = "expression";
	Expression expression;

	@Override
	public void prepare(Configuration context) {
		String expression = context.getString(EXPRESSION_CONTENT_KEY);
		if (!cache.containsKey(expression)) {
			StringBuffer sb = new StringBuffer();
			sb.append("seq.map(");
			for (String item : expression.split(",|;")) {
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
			cache.put(expression, ep);
		}
		this.expression = cache.get(expression);
	}

	public Map<String, Object> process(Map<String, Object> data) {
		Map<String, Object> env = new HashMap<String, Object>(1);
		env.put("db", data);
		// 执行表达式
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) this.expression.execute(env);
		return result;
	} 
  
}
