package ltd.fdsa.database.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.database.datasource.DataSourceProperties;
import ltd.fdsa.database.datasource.DynamicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
@Slf4j
public class DataSourceConfig {
    public static final String WRITER_DATASOURCE = "dataSource";
    public static final String READER_DATASOURCE = "readerDataSource";

    /**
     * @param properties
     * @return
     */
    @Bean(name = WRITER_DATASOURCE)
    @Primary
    public DataSource writerDataSource(DataSourceProperties properties) {
        NamingUtils.formatLog(log, "writerDataSource Start");
        return properties.getMaster();
    }


    @Bean
    @Primary
    public DataSourceTransactionManager masterTransactionManager(@Qualifier(WRITER_DATASOURCE) DataSource dataSource) {
        NamingUtils.formatLog(log, "TransactionManager Start");
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * @param properties
     * @return
     */
    @Bean(name = READER_DATASOURCE)
    public DataSource readerDataSource(DataSourceProperties properties) {
        NamingUtils.formatLog(log, "readerDataSource Start");
        var readerDataSources = this.getReaderDataSources(properties);
        int readSize = readerDataSources == null ? 0 : readerDataSources.size();
        Map<Object, Object> targetDataSources = new HashMap<>(readSize);
        for (int i = 0; i < readSize; i++) {
            targetDataSources.put(i, readerDataSources.get(i));
        }
        DynamicDataSource proxy = new DynamicDataSource(targetDataSources);
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }

    List<DataSource> getReaderDataSources(DataSourceProperties dataSource) {
        if (dataSource.getSlaves() == null || dataSource.getSlaves().isEmpty()) {
            List<DataSource> dataSources = new ArrayList<>(1);
            dataSources.add(dataSource.getMaster());
            return dataSources;
        }
        int readSize = dataSource.getSlaves().size();
        List<DataSource> dataSources = new ArrayList<>(readSize);
        dataSource.getSlaves().forEach(item -> {
            dataSources.add(item);
        });
        return dataSources;
    }
}