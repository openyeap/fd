package ltd.fdsa.fds.core;

public interface Collector<T> {

	public void send(T record);

	public void send(T[] records);

	public boolean isEmpty();

	public int size();

	public void close();

}
