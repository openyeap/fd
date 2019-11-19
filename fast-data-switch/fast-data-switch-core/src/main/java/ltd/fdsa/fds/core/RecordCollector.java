package ltd.fdsa.fds.core;

import ltd.fdsa.fds.model.Record;

public interface RecordCollector extends Collector<Record> {

	public void send(Record record);

	public void send(Record[] records);
}
