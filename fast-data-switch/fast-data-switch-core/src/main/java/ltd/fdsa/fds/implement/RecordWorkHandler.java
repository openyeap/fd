package ltd.fdsa.fds.implement;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.lmax.disruptor.WorkHandler;

import ltd.fdsa.fds.core.DataPipeLine;
import ltd.fdsa.fds.core.DataTarget;
import ltd.fdsa.fds.core.Metric;
import ltd.fdsa.fds.core.exception.FastDataSwitchException;
import ltd.fdsa.fds.model.Record;
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
		Record r = event.getRecord();
		Map<String, Object> data = new HashMap<String, Object>(r.getColumnNumber());
		for (int i = 0; i < r.getColumnNumber(); i++) {
			String key = r.getColumn(i).getName();
			Object value = r.getColumn(i).getRawData();
			data.put(key, value);
		}
		for (DataPipeLine pipeline : this.pipelines) {
		 data =	pipeline.process(data);
		}  
		writer.write(data);
		metric.getWriteCount().incrementAndGet();
	}
}