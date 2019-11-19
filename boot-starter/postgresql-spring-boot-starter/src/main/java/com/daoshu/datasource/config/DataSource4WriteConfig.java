package com.daoshu.datasource.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = DataSource4WriteConfig.PACKAGE, sqlSessionFactoryRef = "writeSqlSessionFactory")
@ConditionalOnProperty("spring.datasource.druid.read.url")
public class DataSource4WriteConfig {
	static final String PACKAGE = "com.daoshu.**.writeMappers";

	@Value("${mybatis.mapperLocations:classpath:mybatisMappers/*.xml}")
	String mapper_location;

	@Value("${mybatis.config-location:classpath:mybatis-config.xml}")
	String config_location;

	@Primary
	@Bean(name = "writeDataSource")
	@ConfigurationProperties("spring.datasource.druid.write")
	public DataSource dataSourceWrite(){
	    return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "writeTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(dataSourceWrite());
    }

    @Bean(name = "writeSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("writeDataSource") DataSource masterDataSource)
            throws Exception {
    	final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver pr=new PathMatchingResourcePatternResolver();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setConfigLocation(pr.getResource(config_location));
        sessionFactory.setMapperLocations(pr.getResources(mapper_location));
        return sessionFactory.getObject();
    }
}
