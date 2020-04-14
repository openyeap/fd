package ltd.fdsa.starter.jdbc.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.starter.jdbc.constant.DataSourceKey;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

	private int readSize;

	public DynamicDataSource(Map<Object, Object> datasources) {
		this.readSize = datasources.size();
		super.setTargetDataSources(datasources);
	}

	@Override
	protected Object determineCurrentLookupKey() {
		if (readSize == 0) {
			return 0;
		}
		int result = new Random().nextInt(readSize);
		return result;
	}

}