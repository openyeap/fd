package cn.zhumingwu.database.config;

import cn.zhumingwu.database.datasource.DataSourceProperties;
import cn.zhumingwu.database.datasource.DynamicDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
@ConditionalOnProperty(name = "spring.datasource.master.type", havingValue = "com.alibaba.druid.pool.DruidDataSource")
@Slf4j
public class DataSourceConfig {
    public static final String WRITER_DATASOURCE = "dataSource";
    public static final String READER_DATASOURCE = "readerDataSource";

    @Bean(name = WRITER_DATASOURCE)
    public DataSource writerDataSource(DataSourceProperties properties) {
        return properties.getMaster();
    }

    @Bean
    public DataSourceTransactionManager masterTransactionManager(@Qualifier(WRITER_DATASOURCE) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = READER_DATASOURCE)
    public DataSource readerDataSource(DataSourceProperties properties) {
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