package ltd.fdsa.fds.container;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.common.plugin.PluginManager;
import ltd.fdsa.fds.core.DataPipeLine;
import ltd.fdsa.fds.core.DataSource;
import ltd.fdsa.fds.core.DataTarget;
import ltd.fdsa.fds.core.JobContext;
import ltd.fdsa.fds.core.JobStatus;
import ltd.fdsa.fds.core.Metric;
import ltd.fdsa.fds.core.RecordCollector;
import ltd.fdsa.fds.core.TaskContext;
import ltd.fdsa.fds.core.config.Configuration;
import ltd.fdsa.fds.core.config.EngineConfig;
import ltd.fdsa.fds.implement.DefaultRecordCollector;
import ltd.fdsa.fds.util.Utils;

@Slf4j
public class FastDataSwitcher {
	static final String STORAGE_BUFFER_SIZE = "fds.storage.default.buffer.size";
	static final String STORAGE_DISRUPTOR_WAIT_STRATEGY = "fds.storage.disruptor.wait.strategy";
	static final String JOB_SLEEP_MILLIS = "fds.job.sleep.millis";

	private int exitCode = 0;

	public static void main(String[] args) {
		// step 1 初始化引擎
		PluginManager pluginManager = PluginManager.getDefaultInstance();
		final EngineConfig engineConfig = EngineConfig.create();
		// 扫描配置文件中的插件
		// 加载插件
		// pluginManager.addExternalJar(basePath);

		// step 2 加载job配置
		final Configuration jobConfig = engineConfig.merge(null, true);//("job config from http input");

		// step 3 得到作业所有数据源
		for (Configuration itemConfig : jobConfig.getListConfiguration("contents")) {
			Configuration dsConfig = itemConfig.getConfiguration("datasource");
			DataSource source = pluginManager.getInstance(dsConfig.getString("name"), DataSource.class);
			source.prepare(dsConfig);
			Configuration dtConfig = itemConfig.getConfiguration("datatarget");
			DataTarget target = pluginManager.getInstance(dtConfig.getString("name"), DataTarget.class);
			target.prepare(dtConfig);
			// 分拆数据源的任务, TODO 目前只支持一个任务
			for (Configuration taskConfig : source.splitTask()) {
				List<DataPipeLine> pipelines = new ArrayList<DataPipeLine>();
				// 得到任务的处理器
				for (Configuration pipeLineConfig : taskConfig.getListConfiguration("pipelines")) {
					DataPipeLine pipeLine = pluginManager.getInstance(pipeLineConfig.getString("name"),
							DataPipeLine.class);
					pipeLine.prepare(pipeLineConfig);
					pipelines.add(pipeLine);
				}
				TaskContext context = new TaskContext(source, pipelines.toArray(new DataPipeLine[0]), target,
						jobConfig);
				context.run();
			}
		}

//		 pipeLine.prepare("Name=data.firstName +' '+ data.lastName,Age=data.age");
//		List<Map<String, Object>> dt = new ArrayList<Map<String, Object>>();
//
//		for (Map<String, Object> item : ds) {
//			item.put("firstName", "mike");
//			item.put("lastName", "json");
//			item.put("age", 18);
//			Map<String, Object> result = pipeLine.process(item);
//			dt.add(result);
//			log.debug(JSON.toJSONString(result));
//		}
//
//		DataTarget target = pluginManager.getInstance("com.daoshu.dtl.implement.DataTarget", DataTarget.class);
//		target.write(dt);

	}

//	public void start(final Configuration jobConfig) {
//		final Configuration readerConfig = jobConfig.getReaderConfig();
//		final Configuration writerConfig = jobConfig.getWriterConfig();
// 
//
//		final JobContext context = new JobContext();
//		context.setJobConfig(jobConfig);
//
//		final Metric metric = new Metric();
//		context.setMetric(metric);
//
//		final OutputFieldsDeclarer outputFieldsDeclarer = new OutputFieldsDeclarer();
//		context.setDeclarer(outputFieldsDeclarer);
//
//		final EngineConfig engineConfig = EngineConfig.create();
//		context.setEngineConfig(engineConfig);
//
//		long sleepMillis = engineConfig.getLong(JOB_SLEEP_MILLIS, Constants.DEFAULT_JOB_SLEEP_MILLIS);
//
//		List<ReaderConfig> readerConfigList = null;
//		final Splitter splitter = jobConfig.newSplitter();
//		if (splitter != null) {
//			log.info("Executing splitter for reader.");
//
//			ExecutorService es = Executors.newCachedThreadPool();
//			Callable<List<ReaderConfig>> callable = new Callable<List<ReaderConfig>>() {
//				@Override
//				public List<ReaderConfig> call() throws Exception {
//					Thread.currentThread().setContextClassLoader(splitter.getClass().getClassLoader());
//					return splitter.split(jobConfig);
//				}
//			};
//
//			Future<List<ReaderConfig>> future = es.submit(callable);
//			es.shutdown();
//			try {
//				readerConfigList = future.get();
//			} catch (InterruptedException | ExecutionException e) {
//				log.error("", e);
//				System.exit(JobStatus.FAILED.getStatus());
//			}
//
//			if (readerConfigList.isEmpty()) {
//				System.exit(JobStatus.SUCCESS.getStatus());
//			}
//		} else {
//
//			readerConfigList = new ArrayList<>();
//			readerConfigList.add(readerConfig);
//		}
//
//		Reader[] readers = new Reader[readerConfigList.size()];
//		for (int i = 0, len = readers.length; i < len; i++) {
//			readers[i] = jobConfig.newReader();
//		}
//
//		context.setReaders(readers);
//
//		int bufferSize = engineConfig.getInt(STORAGE_BUFFER_SIZE, 1024);
//		String WaitStrategyName = engineConfig.getString(STORAGE_DISRUPTOR_WAIT_STRATEGY,
//				BlockingWaitStrategy.class.getName());
//
//		Storage storage = createStorage(bufferSize, WaitStrategyName, readers.length, handlers, context);
//		context.setStorage(storage);
//
//		log.info("Transfer data from reader to writer...");
//
//		RecordCollector rc = new DefaultRecordCollector(storage, metric, readerConfig.getFlowLimit());
//		ExecutorService es = Executors.newFixedThreadPool(readers.length);
//		CompletionService<Integer> cs = new ExecutorCompletionService<>(es);
//		for (int i = 0, len = readerConfigList.size(); i < len; i++) {
//			ReaderWorker readerWorker = new ReaderWorker(readers[i], context, readerConfigList.get(i), rc);
//			cs.submit(readerWorker);
//		}
//		es.shutdown();
//
//		metric.setReaderStartTime(System.currentTimeMillis());
//		metric.setWriterStartTime(System.currentTimeMillis());
//		while (!es.isTerminated()) {
//			if (context.isWriterError()) {
//				log.info("Write error.");
//				log.info("Closing reader and writer.");
//				// storage.close();
//				closeReaders(readers);
//
//				log.info("Job run failed!");
//				System.exit(JobStatus.FAILED.getStatus());
//			}
//
//			Utils.sleep(sleepMillis);
//			log.info("Read: {}\tWrite: {}", metric.getReadCount().get(), metric.getWriteCount().get());
//		}
//
//		context.setReaderFinished(true);
//		metric.setReaderEndTime(System.currentTimeMillis());
//
//		while (!storage.isEmpty()) {
//			if (context.isWriterError()) {
//				log.info("Write error.");
//				closeWriters(writers);
//				log.info("Job run failed!");
//				System.exit(JobStatus.FAILED.getStatus());
//			}
//			Utils.sleep(sleepMillis);
//
//			log.info("Read Finished(total: {}), Write: {}", metric.getReadCount().get(), metric.getWriteCount().get());
//		}
//
//		storage.close();
//		log.info("Read Finished(total: {}), Write Finished(total: {})", metric.getReadCount().get(),
//				metric.getWriteCount().get());
//
//		for (int i = 0, len = readers.length; i < len; i++) {
//			try {
//				Future<Integer> future = cs.take();
//				if (future == null) {
//					log.info("Read error.");
//					closeWriters(writers);
//					log.info("Job run failed!");
//					System.exit(1);
//				}
//
//				Integer result = future.get();
//				if (result == null) {
//					result = -1;
//				}
//
//				if (result != 0) {
//					log.info("Read error.");
//					closeWriters(writers);
//					log.info("Job run failed!");
//					System.exit(result);
//				}
//			} catch (Exception e) {
//				log.error(Throwables.getStackTraceAsString(e));
//				exitCode = 1;
//			}
//		}
//
//		metric.setWriterEndTime(System.currentTimeMillis());
//		if (!closeWriters(writers)) {
//			exitCode = 1;
//		}
//
//		context.setWriterFinished(true);
//
//		double readSeconds = (metric.getReaderEndTime() - metric.getReaderStartTime()) / 1000d;
//		double writeSeconds = (metric.getWriterEndTime() - metric.getWriterStartTime()) / 1000d;
//		final DecimalFormat decimalFormat = new DecimalFormat("#0.00");
//		String readSpeed = decimalFormat.format(metric.getReadCount().get() / readSeconds);
//		String writeSpeed = decimalFormat.format(metric.getWriteCount().get() / writeSeconds);
//		log.info("Read spent time: {}s, Write spent time: {}s", decimalFormat.format(readSeconds),
//				decimalFormat.format(writeSeconds));
//		log.info("Read records: {}/s, Write records: {}/s", readSpeed, writeSpeed);
//
//		// PluginUtils.closeURLClassLoader();
//
//		System.exit(exitCode);
//	}

//	private boolean closeReaders(final Reader[] readers) {
//		ExecutorService es = Executors.newCachedThreadPool();
//		Callable<Boolean> callable = new Callable<Boolean>() {
//			@Override
//			public Boolean call() throws Exception {
//				Thread.currentThread().setContextClassLoader(readers[0].getClass().getClassLoader());
//				try {
//					for (Reader reader : readers) {
//						reader.close();
//					}
//
//					return true;
//				} catch (Exception e) {
//					log.error(Throwables.getStackTraceAsString(e));
//				}
//				return false;
//			}
//		};
//
//		Future<Boolean> future = es.submit(callable);
//		es.shutdown();
//
//		try {
//			return future.get();
//		} catch (InterruptedException | ExecutionException e) {
//			log.error(Throwables.getStackTraceAsString(e));
//			return false;
//		}
//	}
//
//	private boolean closeWriters(final Writer[] writers) {
//		ExecutorService es = Executors.newCachedThreadPool();
//		Callable<Boolean> callable = new Callable<Boolean>() {
//			@Override
//			public Boolean call() throws Exception {
//				Thread.currentThread().setContextClassLoader(writers[0].getClass().getClassLoader());
//				try {
//					for (Writer writer : writers) {
//						writer.close();
//					}
//
//					return true;
//				} catch (Exception e) {
//					log.error(Throwables.getStackTraceAsString(e));
//				}
//				return false;
//			}
//		};
//
//		Future<Boolean> future = es.submit(callable);
//		es.shutdown();
//
//		try {
//			return future.get();
//		} catch (InterruptedException | ExecutionException e) {
//			log.error(Throwables.getStackTraceAsString(e));
//			return false;
//		}
//	}
}
