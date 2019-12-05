package ltd.fdsa.fds.implement;
 
import java.util.Map;

import com.lmax.disruptor.WorkHandler;

import ltd.fdsa.fds.core.DataPipeLine;
import ltd.fdsa.fds.core.DataTarget;
import ltd.fdsa.fds.core.Metric; 
public class RecordWorkHandler implements WorkHandler<RecordEvent> {

	private final DataPipeLine[] pipelines;
	private final DataTarget writer;
	private final Metric metric;

	public RecordWorkHandler(DataPipeLine[] pipelines, DataTarget writer, Metric metric) {
		this.pipelines = pipelines;
		this.writer = writer;
		this.metric = metric;
	}

	@Override
	public void onEvent(RecordEvent event) { 
		Map<String, Object> record = event.getRecord();
		for (DataPipeLine pipeline : this.pipelines) {
		 record =	pipeline.process(record);
		}  
		writer.write(record);
		metric.getWriteCount().incrementAndGet();
	}
}