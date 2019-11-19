package ltd.fdsa.fds.core;

import java.util.Map;

import ltd.fdsa.fds.core.config.Configuration;

public interface DataPipeLine extends Pluginable {

	void prepare(Configuration context);

	Map<String, Object> process(Map<String, Object> data);
}
