package ltd.fdsa.fds.core;

import java.util.Map;

import ltd.fdsa.fds.core.config.Configuration;
import ltd.fdsa.fds.model.Record;

public interface DataTarget extends Pluginable {
	void prepare(Configuration context);

	void write(Map<String, Object> data);
}
