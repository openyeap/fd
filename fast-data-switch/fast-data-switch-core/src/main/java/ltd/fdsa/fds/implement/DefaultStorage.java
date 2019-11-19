package ltd.fdsa.fds.implement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import ltd.fdsa.fds.core.DataPipeLine;
import ltd.fdsa.fds.core.DataTarget;
import ltd.fdsa.fds.core.JobContext;
import ltd.fdsa.fds.core.Metric;
import ltd.fdsa.fds.core.Storage;
import ltd.fdsa.fds.core.Writer;
import ltd.fdsa.fds.core.exception.FastDataSwitchException;
import ltd.fdsa.fds.model.Record;

public class DefaultStorage implements Storage {

	private final Disruptor<RecordEvent> disruptor;
	private final RingBuffer<RecordEvent> ringBuffer;

	private static final EventTranslatorOneArg<RecordEvent, Record> TRANSLATOR = new EventTranslatorOneArg<RecordEvent, Record>() {

		@Override
		public void translateTo(RecordEvent event, long sequence, Record record) {
			event.setRecord(record);
		}
	};

	public static Storage createStorage(int bufferSize, String WaitStrategyName, int producerCount,
			RecordWorkHandler[] handlers) {
		WaitStrategy waitStrategy = WaitStrategyFactory.build(WaitStrategyName);
		ProducerType producerType;
		if (producerCount == 1) {
			producerType = ProducerType.SINGLE;
		} else {
			producerType = ProducerType.MULTI;
		}
		Disruptor<RecordEvent> disruptor = new Disruptor<>(RecordEvent.FACTORY, bufferSize,
				Executors.defaultThreadFactory(), producerType, waitStrategy);
		Storage storage = new DefaultStorage(disruptor, handlers);
		return storage;
	}

	DefaultStorage(Disruptor<RecordEvent> disruptor, RecordWorkHandler[] handlers) {
		this.disruptor = disruptor;
		disruptor.setDefaultExceptionHandler(new RecordEventExceptionHandler(disruptor));
		disruptor.handleEventsWithWorkerPool(handlers);
		ringBuffer = disruptor.start();
	}

	@Override
	public void put(Record record) {
		disruptor.publishEvent(TRANSLATOR, record);
	}

	@Override
	public void put(Record[] records) {
		for (Record record : records) {
			put(record);
		}
	}

	@Override
	public boolean isEmpty() {
		return ringBuffer.remainingCapacity() == ringBuffer.getBufferSize();
	}

	@Override
	public int size() {
		return ringBuffer.getBufferSize();
	}

	@Override
	public void close() {
		disruptor.shutdown();
	}
}

class RecordEvent {

	private Record record;

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public static final EventFactory<RecordEvent> FACTORY = new EventFactory<RecordEvent>() {

		public RecordEvent newInstance() {
			return new RecordEvent();
		}
	};

}
class RecordEventHandler implements EventHandler<Record>{

	@Override
	public void onEvent(Record event, long sequence, boolean endOfBatch) throws Exception {
		
	}
	
}
class RecordEventExceptionHandler implements ExceptionHandler<Object> {

	private final Disruptor<RecordEvent> disruptor;

	private static Logger LOGGER = LogManager.getLogger(RecordEventExceptionHandler.class);

	public RecordEventExceptionHandler(Disruptor<RecordEvent> disruptor) {
		this.disruptor = disruptor;
	}

	public void handleEventException(Throwable t, long sequence, Object event) {
		LOGGER.error(Throwables.getStackTraceAsString(t));

		disruptor.shutdown();
	}

	public void handleOnShutdownException(Throwable t) {
		LOGGER.error(Throwables.getStackTraceAsString(t));
		disruptor.shutdown();
	}

	public void handleOnStartException(Throwable t) {
		LOGGER.error(Throwables.getStackTraceAsString(t));
		disruptor.shutdown();
	}
}


class WaitStrategyFactory {

	private static final List<String> WAIT_STRATEGY_SUPPORTED = Lists.newArrayList(BlockingWaitStrategy.class.getName(),
			BusySpinWaitStrategy.class.getName(), SleepingWaitStrategy.class.getName(),
			YieldingWaitStrategy.class.getName());

	/**
	 * 构造线程等待策略
	 */
	public static WaitStrategy build(String name) {
		if (WAIT_STRATEGY_SUPPORTED.contains(name)) {
			try {
				return (WaitStrategy) Class.forName(name).newInstance();
			} catch (InstantiationException e) {
				throw new FastDataSwitchException(e);
			} catch (IllegalAccessException e) {
				throw new FastDataSwitchException(e);
			} catch (ClassNotFoundException e) {
				throw new FastDataSwitchException(e);
			}
		} else {
			throw new FastDataSwitchException("Invalid wait strategy: " + name);
		}
	}
}
