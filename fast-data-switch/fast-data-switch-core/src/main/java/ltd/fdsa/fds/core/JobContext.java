package ltd.fdsa.fds.core;

import ltd.fdsa.fds.core.config.Configuration;
import ltd.fdsa.fds.core.config.EngineConfig;
import ltd.fdsa.fds.model.Fields; 
public class JobContext {

	private EngineConfig engineConfig;
	private Configuration jobConfig;
 
	private Metric metric; 
	private boolean isReaderFinished;
	private boolean isReaderError;
	private boolean isWriterFinished;
	private boolean isWriterError;
	private JobStatus jobStatus = JobStatus.SUCCESS;
  
	public EngineConfig getEngineConfig() {
		return engineConfig;
	}

	public void setEngineConfig(EngineConfig engineConfig) {
		this.engineConfig = engineConfig;
	}
 
	public Metric getMetric() {
		return metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public Configuration getJobConfig() {
		return jobConfig;
	}

	public void setJobConfig(Configuration jobConfig) {
		this.jobConfig = jobConfig;
	}

	public boolean isWriterError() {
		return isWriterError;
	}

	public void setWriterError(boolean isWriterError) {
		this.isWriterError = isWriterError;
	}

	public boolean isReaderFinished() {
		return isReaderFinished;
	}

	public void setReaderFinished(boolean isReaderFinished) {
		this.isReaderFinished = isReaderFinished;
	}

	public boolean isReaderError() {
		return isReaderError;
	}

	public void setReaderError(boolean isReaderError) {
		this.isReaderError = isReaderError;
	}

	public boolean isWriterFinished() {
		return isWriterFinished;
	}

	public void setWriterFinished(boolean isWriterFinished) {
		this.isWriterFinished = isWriterFinished;
	} 
	
	public JobStatus getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
	}
}