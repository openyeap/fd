package ltd.fdsa.fds.core;

import java.util.List;
import java.util.Map;

import ltd.fdsa.fds.core.config.Configuration;
import ltd.fdsa.fds.model.Fields;

public interface DataSource extends Pluginable {
	void prepare(Configuration context);

	Fields getFields();

	void read(RecordCollector collector);

	List<Configuration> splitTask();
}
