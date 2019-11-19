package ltd.fdsa.fds.core;

import ltd.fdsa.fds.model.Record;

public interface Storage {

	public void put(Record record);

	public void put(Record[] records);

	public boolean isEmpty();

	public int size();

	public void close();
}
