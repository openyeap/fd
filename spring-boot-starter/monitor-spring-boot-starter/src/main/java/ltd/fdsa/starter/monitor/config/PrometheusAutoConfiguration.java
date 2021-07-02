package ltd.fdsa.starter.monitor.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.prometheus.client.CollectorRegistry;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.starter.monitor.converter.DruidCollector;
import ltd.fdsa.starter.monitor.prometheus.PrometheusDruidStatEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@Slf4j
//@ConditionalOnBean({DruidDataSource.class})
public class PrometheusAutoConfiguration {
    private final PrometheusDruidStatEndpoint druidStatEndpoint;
    private final CollectorRegistry registry;

    public PrometheusAutoConfiguration(CollectorRegistry registry) {
        this.druidStatEndpoint = new PrometheusDruidStatEndpoint();
        this.registry = registry;
    }

    @Bean(name = "prometheusDruidStatEndpoint")
    public PrometheusDruidStatEndpoint prometheusDruidStatEndpoint() {
        return this.druidStatEndpoint;
    }

    @Autowired
    public void bindMetricsRegistryToDruidDataSources(Collection<DataSource> dataSources) {
        NamingUtils.formatLog(log, "Registry Druid Stat Registry");
        Map<String, DruidDataSource> druidDataSources = new LinkedHashMap<>();
        for (DataSource dataSource : dataSources) {
            DruidDataSource druidDataSource = DataSourceUnwrapper.unwrap(dataSource, DruidDataSource.class);
            if (druidDataSource != null) {
                druidDataSources.put(druidDataSource.getName(), druidDataSource);
            }
        }
        DruidCollector druidCollector = new DruidCollector(druidDataSources);
        druidCollector.register(registry);
    }

    @Autowired
    public void registryDruidStat(Collection<DataSource> dataSources) {
        NamingUtils.formatLog(log, "Registry Druid Stat Endpoint");
        for (DataSource dataSource : dataSources) {
            DruidDataSource druidDataSource = DataSourceUnwrapper.unwrap(dataSource, DruidDataSource.class);
            if (druidDataSource != null) {
                druidDataSource.setStatLogger(this.druidStatEndpoint);
            }
        }
    }
}
