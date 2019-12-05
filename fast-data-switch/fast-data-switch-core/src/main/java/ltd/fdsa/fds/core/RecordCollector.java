package ltd.fdsa.fds.core;

import java.util.Map;

public interface RecordCollector extends Collector<Map<String,Object>> {

	public void send(Map<String,Object> record);

	public void send(Map<String,Object>[] records);
}
