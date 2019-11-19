
package ltd.fdsa.fds.core.config;

import java.util.List;
import java.util.stream.Collectors;

public class JobConfig extends EngineConfig {

	public JobConfig(String config) {
		super(config);
	}

	public List<DataSourceConfig> getDataSourceConfig() {
		return this.config.getListConfiguration("datasources").stream().map(m-> (DataSourceConfig)m).collect(Collectors.toList( ));
	}
//
//	public WriterConfig getWriterConfig();
//
//	public String getReaderName();
//
//	public String getWriterName();
//
//	public Reader newReader();
//
//	public Splitter newSplitter();
//
//	public Writer newWriter();
//
//	public TaskConfig getTaskConfig();

}
