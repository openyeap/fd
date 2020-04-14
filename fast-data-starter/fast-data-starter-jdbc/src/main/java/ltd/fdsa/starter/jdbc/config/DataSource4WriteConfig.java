package ltd.fdsa.starter.jdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder; 
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
@MapperScan(basePackages = "**.writeMappers", sqlSessionFactoryRef = "writeSqlSessionFactory") 
@Slf4j  
public class DataSource4WriteConfig {
	@Value("${mybatis.mapperLocations:classpath:mybatisMappers/*.xml}")
	String mapper_location; 
	@Value("${mybatis.config-location:classpath:mybatis-config.xml}")
	String config_location;
 
   
    @Primary
    @Bean(name = { "writeDataSource" }, destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource")
	public DataSource writeDataSource() {
		log.info("==========DruidDataSourceBuilder start===========");
//		return DataSourceBuilder.create().type(dataSourceType).build();
	    return DruidDataSourceBuilder.create().build();

	}

	@Bean
    @Primary
	public DataSourceTransactionManager masterTransactionManager(@Qualifier("writeDataSource") DataSource dataSource) {
		log.info("==========TransactionManager start===========");
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "writeSqlSessionFactory")
    @Primary
	public SqlSessionFactory writeSqlSessionFactory(@Qualifier("writeDataSource") DataSource dataSource) throws Exception {
		log.info("==========writeSqlSessionFactory start===========");
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		PathMatchingResourcePatternResolver pr = new PathMatchingResourcePatternResolver();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setConfigLocation(pr.getResource(config_location));
		sessionFactory.setMapperLocations(pr.getResources(mapper_location));
		return sessionFactory.getObject();
	} 

}
