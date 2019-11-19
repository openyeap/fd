package ltd.fdsa.fds.core;

import lombok.Data;
import ltd.fdsa.fds.core.config.Configuration;
import ltd.fdsa.fds.core.config.EngineConfig;
import ltd.fdsa.fds.implement.DefaultRecordCollector;
import ltd.fdsa.fds.implement.DefaultStorage;
import ltd.fdsa.fds.implement.RecordWorkHandler;

@Data
public class TaskContext {
	public TaskContext(DataSource reader, DataPipeLine[] pipelines, DataTarget writer, Configuration config) {
		this.writer = writer;
		this.reader = reader;
		this.metric = new Metric();
		int bufferSize = config.getInt("");
		String WaitStrategyName = config.getString("");
		int producerCount = config.getInt("");
		RecordWorkHandler[] handlers = new RecordWorkHandler[1];
		handlers[0] = new RecordWorkHandler(pipelines, this.writer, this.metric);
		this.storage = DefaultStorage.createStorage(bufferSize, WaitStrategyName, producerCount, handlers);
		long flowLimit = config.getLong("");
		this.collector = new DefaultRecordCollector(this.storage, this.metric, flowLimit);
	}

	private DataSource reader;
	private Storage storage;
	private Metric metric;
	private DataTarget writer;
	private DefaultRecordCollector collector;

	public void run() {
		this.reader.read(collector);
	}
}
