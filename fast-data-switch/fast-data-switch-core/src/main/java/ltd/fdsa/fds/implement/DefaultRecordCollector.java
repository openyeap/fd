package ltd.fdsa.fds.implement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.carrotsearch.sizeof.RamUsageEstimator;
import com.google.common.base.Stopwatch;
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
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.fds.core.Metric;
import ltd.fdsa.fds.core.RecordCollector; 
import ltd.fdsa.fds.core.exception.FastDataSwitchException; 
import ltd.fdsa.fds.util.Utils;

@Slf4j
public class DefaultRecordCollector implements RecordCollector {

	private static final long SLEEP_MILL_SECONDS = 1000;
	private static final EventTranslatorOneArg<RecordEvent, Map<String, Object>> TRANSLATOR = new EventTranslatorOneArg<RecordEvent, Map<String, Object>>() {

		@Override
		public void translateTo(RecordEvent event, long sequence, Map<String, Object> record) {
			event.setRecord(record);
		}
	};

	private final Disruptor<RecordEvent> disruptor;
	private final RingBuffer<RecordEvent> ringBuffer;

	private final Metric metric;
	private final long flowLimit;
	private final Stopwatch stopwatch = Stopwatch.createStarted();

	DefaultRecordCollector(Disruptor<RecordEvent> disruptor, RecordWorkHandler[] handlers, Metric metric,
			long flowLimit) {
		this.disruptor = disruptor;
		disruptor.setDefaultExceptionHandler(new RecordEventExceptionHandler(disruptor));
		disruptor.handleEventsWithWorkerPool(handlers);
		ringBuffer = disruptor.start();
		this.metric = metric;
		this.flowLimit = flowLimit;
		log.info("The flow limit is {} bytes/s.", this.flowLimit);
	}

	public static RecordCollector createStorage(int bufferSize, String WaitStrategyName, int producerCount,
			RecordWorkHandler[] handlers, Metric metric, long flowLimit) {
		WaitStrategy waitStrategy = WaitStrategyFactory.build(WaitStrategyName);
		ProducerType producerType;
		if (producerCount == 1) {
			producerType = ProducerType.SINGLE;
		} else {
			producerType = ProducerType.MULTI;
		}
		Disruptor<RecordEvent> disruptor = new Disruptor<>(RecordEvent.FACTORY, bufferSize,
				Executors.defaultThreadFactory(), producerType, waitStrategy);
		RecordCollector collector = new DefaultRecordCollector(disruptor, handlers, metric, flowLimit);
		return collector;
	}

	/*
	 * 限速
	 */
	private void limit() {

		if (flowLimit > 0) {
			while (true) {
				long currentSpeed = metric.getSpeed();
				if (currentSpeed > flowLimit) {
					if (stopwatch.elapsed(TimeUnit.SECONDS) >= 5) {
						log.info("Current Speed is {} MB/s, sleeping...",
								String.format("%.2f", (double) currentSpeed / 1024 / 1024));
						stopwatch.reset();
					}
					Utils.sleep(SLEEP_MILL_SECONDS);
				} else {
					break;
				}
			}
		}
	}

	@Override
	public void send(Map<String, Object> record) {
		limit();
		disruptor.publishEvent(TRANSLATOR, record);
		metric.getReadCount().incrementAndGet();
		if (flowLimit > 0) {
			metric.getReadBytes().addAndGet(RamUsageEstimator.sizeOf(record));
		}

	}

	@Override
	public void send(Map<String, Object>[] records) {
	 
		for (Map<String, Object> record : records) {
		this.send(record); 
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

	private Map<String, Object> record;

	public Map<String, Object> getRecord() {
		return record;
	}

	public void setRecord(Map<String, Object> record) {
		this.record = record;
	}

	public static final EventFactory<RecordEvent> FACTORY = new EventFactory<RecordEvent>() {

		public RecordEvent newInstance() {
			return new RecordEvent();
		}
	};

}

class RecordEventHandler implements EventHandler<Map<String, Object>> {

	@Override
	public void onEvent(Map<String, Object> event, long sequence, boolean endOfBatch) throws Exception {

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
