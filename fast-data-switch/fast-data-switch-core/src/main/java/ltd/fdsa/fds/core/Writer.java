package ltd.fdsa.fds.core;

import ltd.fdsa.fds.core.config.Configuration;
import ltd.fdsa.fds.model.Record;

public abstract class Writer extends AbstractPlugin {

	public void prepare(JobContext context, Configuration writerConfig) {
	}

	public void execute(Record record) {
	}

	public void close() {
	}
}
