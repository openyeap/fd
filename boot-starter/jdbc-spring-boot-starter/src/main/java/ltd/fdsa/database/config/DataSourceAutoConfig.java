package ltd.fdsa.database.config;
//package com.daoshu.datasource.config;
//
//import java.util.Properties;
//
//import javax.sql.DataSource;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.AutoConfigureBefore;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
//import com.daoshu.datasource.aop.DataSourceAOP;
//import com.daoshu.datasource.constant.DataSourceKey;
//import com.daoshu.datasource.util.DynamicDataSource;
//
//@Configuration
//@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
////@ConditionalOnProperty(name = { "spring.datasource.druid.write.url" })
//@Import(DataSourceAOP.class)
//public class DataSourceAutoConfig {
//	@Bean
//	@ConfigurationProperties("spring.datasource.druid.write")
//	public DataSource dataSourceMaster() {
//		return DruidDataSourceBuilder.create().build();
//	}
//
//	@Autowired
//	Environment env;
//
//	@Bean
//	public DataSource dataSource() {
//		DynamicDataSource dataSource = new DynamicDataSource();
//		String prefix = "spring.datasource.druid.";
//		DruidDataSource ds = (DruidDataSource) dataSourceMaster();
//		dataSource.addDataSource("write", ds);
//		dataSource.setDefaultTargetDataSource(ds);
//
//		String[] list = new String[] { "read", "other" };
//
//		for (String item : list) {
//			ds = new DruidDataSource();
//			Properties properties = env.getProperty(prefix + item, Properties.class);
//			ds.configFromPropety(properties);
//			dataSource.addDataSource(item, ds);
//		}
//		return dataSource;
//	}
//
//	@Bean(name = "sqlSessionFactory")
//	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
//    	final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//		sessionFactory.setDataSource(dataSource);
//        PathMatchingResourcePatternResolver pr=new PathMatchingResourcePatternResolver();
//        sessionFactory.setConfigLocation(pr.getResource(""));
//        sessionFactory.setMapperLocations(pr.getResources("classpath*:com/daoshu/**/*.xml"));
//        return sessionFactory.getObject();	
//	}
//
//	@Bean // 将数据源纳入spring事物管理
//	public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
//		return new DataSourceTransactionManager(dataSource);
//	}
//
//}
