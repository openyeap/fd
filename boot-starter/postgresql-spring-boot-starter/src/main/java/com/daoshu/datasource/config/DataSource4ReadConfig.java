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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = DataSource4ReadConfig.PACKAGE, sqlSessionFactoryRef = "readSqlSessionFactory")
@ConditionalOnProperty("spring.datasource.druid.read.url")
public class DataSource4ReadConfig {

	static final String PACKAGE = "com.daoshu.**.readMappers";

	@Value("${mybatis.mapperLocations:classpath:mybatisMappers/*.xml}")
	String mapper_location;

	@Value("${mybatis.config-location:classpath:mybatis-config.xml}")
	String config_location;

	@Bean(name = "readDataSource")
	@ConfigurationProperties("spring.datasource.druid.read")
	public DataSource dataSourceWrite(){
	    return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "readTransactionManager")
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(dataSourceWrite());
    }

    @Bean(name = "readSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("readDataSource") DataSource masterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver pr=new PathMatchingResourcePatternResolver();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setConfigLocation(pr.getResource(config_location));
        sessionFactory.setMapperLocations(pr.getResources(mapper_location));
        return sessionFactory.getObject();
    }
}
