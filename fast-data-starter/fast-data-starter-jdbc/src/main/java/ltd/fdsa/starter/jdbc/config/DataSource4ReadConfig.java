package ltd.fdsa.starter.jdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.starter.jdbc.constant.DataSourceKey;
import ltd.fdsa.starter.jdbc.util.DynamicDataSource;
import tk.mybatis.spring.annotation.MapperScan;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@MapperScan(basePackages = "**.readMappers", sqlSessionFactoryRef = "readSqlSessionFactory")
@Data
@Slf4j
public class DataSource4ReadConfig {
	@Value("${mybatis.mapperLocations:classpath:mybatisMappers/*.xml}")
	String mapper_location;
	@Value("${mybatis.config-location:classpath:mybatis-config.xml}")
	String config_location;

	private List<DruidDataSource> slaves = new ArrayList<>();

	@Bean("readDataSources")
	public List<DataSource> readDataSources(@Qualifier("writeDataSource") DataSource dataSource) {
		if (this.slaves == null || slaves.isEmpty()) {
			log.warn("==========slaves.isEmpty===========");
			List<DataSource> dataSources = new ArrayList<>(1);
			dataSources.add(dataSource);
			return dataSources;
		}
		int readSize = slaves.size();
		log.info("==========slaves.{}===========",readSize);		
		List<DataSource> dataSources = new ArrayList<>(readSize);
		slaves.forEach(item -> dataSources.add(item));
		return dataSources;
	}

	/**
	 * @param readDataSources
	 * @return
	 */
	@Bean(name = "readDataSource")
	@ConditionalOnClass({ EnableTransactionManagement.class })
	public DataSource roundRobinDataSouceProxy(@Qualifier("readDataSources") List<DataSource> readDataSources) {
		int readSize = readDataSources == null ? 0 : readDataSources.size();
		Map<Object, Object> targetDataSources = new HashMap<>(readSize);
		for (int i = 0; i < readSize; i++) {
			targetDataSources.put(i, readDataSources.get(i));
		}
		DynamicDataSource proxy = new DynamicDataSource(targetDataSources);
		proxy.setTargetDataSources(targetDataSources);
		return proxy;
	}

	@Bean(name = "readSqlSessionFactory")
	public SqlSessionFactory readSqlSessionFactory(@Qualifier("readDataSource") DataSource readDataSource)
			throws Exception {
		log.info("==========readSqlSessionFactory start===========");
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		PathMatchingResourcePatternResolver pr = new PathMatchingResourcePatternResolver();
		sessionFactory.setDataSource(readDataSource);
		sessionFactory.setConfigLocation(pr.getResource(config_location));
		sessionFactory.setMapperLocations(pr.getResources(mapper_location));
		return sessionFactory.getObject();
	}
}
