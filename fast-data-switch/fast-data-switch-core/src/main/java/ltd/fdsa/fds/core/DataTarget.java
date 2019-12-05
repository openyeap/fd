package ltd.fdsa.fds.core;

import java.util.Map;

import ltd.fdsa.fds.core.config.Configuration; 

public interface DataTarget extends Pluginable {
	void prepare(Configuration context);
	void write(Map<String, Object> data);
}
