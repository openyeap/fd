package ltd.fdsa.fds.implement;

import com.alibaba.fastjson.JSON;

import ltd.fdsa.fds.core.exception.FastDataSwitchException;
import ltd.fdsa.fds.model.Column;
import ltd.fdsa.fds.model.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultRecord implements Record {

	private static final int RECORD_AVERGAE_COLUMN_NUMBER = 16;

	private List<Column> columns;

	private int byteSize;
//	private int memorySize;

	public DefaultRecord() {
		this.columns = new ArrayList<Column>(RECORD_AVERGAE_COLUMN_NUMBER);
	}

	public DefaultRecord(Map<String, Object> data) {
		this.columns = new ArrayList<Column>(data.size());
		for (String key : data.keySet()) {
			this.columns.add(new Column(key, data.get(key)));
		}
	}

	@Override
	public void addColumn(Column column) {
		columns.add(column);
		incrByteSize(column);
	}

	@Override
	public Column getColumn(int i) {
		if (i < 0 || i >= columns.size()) {
			return null;
		}
		return columns.get(i);
	}

	@Override
	public String toString() {
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("size", this.getColumnNumber());
		json.put("data", this.columns);
		return JSON.toJSONString(json);
	}

	@Override
	public int getColumnNumber() {
		return this.columns.size();
	}

	@Override
	public int getByteSize() {
		return byteSize;
	}

	public int getMemorySize() {
//		return memorySize;
		return byteSize;
	}

	private void decrByteSize(final Column column) {
		if (null == column) {
			return;
		}

		byteSize -= column.getByteSize();

	}

	private void incrByteSize(final Column column) {
		if (null == column) {
			return;
		}

		byteSize += column.getByteSize();

	}

	private void expandCapacity(int totalSize) {
		if (totalSize <= 0) {
			return;
		}

		int needToExpand = totalSize - columns.size();
		while (needToExpand-- > 0) {
			this.columns.add(null);
		}
	}

}
