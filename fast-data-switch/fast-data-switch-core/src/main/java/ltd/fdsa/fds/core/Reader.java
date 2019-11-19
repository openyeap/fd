package ltd.fdsa.fds.core;

import ltd.fdsa.fds.core.config.Configuration;

public abstract class Reader extends AbstractPlugin {

	public void prepare(JobContext context, Configuration readerConfig) {
	}

	public void execute(RecordCollector recordCollector) {
	}

	public void close() {
	}
 
}
