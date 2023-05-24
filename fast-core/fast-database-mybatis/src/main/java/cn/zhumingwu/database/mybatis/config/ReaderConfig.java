package cn.zhumingwu.database.mybatis.config;

import cn.zhumingwu.database.mybatis.handler.LongArrayTypeHandler;
import cn.zhumingwu.database.mybatis.handler.StringArrayTypeHandler;
import cn.zhumingwu.database.mybatis.interceptor.DecryptResultFieldInterceptor;
import cn.zhumingwu.database.mybatis.interceptor.EncryptParameterInterceptor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.core.util.NamingUtils;
import cn.zhumingwu.database.config.DataSourceConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "**.mybatis.mapper.reader.**", annotationClass = Mapper.class, sqlSessionFactoryRef = ReaderConfig.READ_SQL_SESSION_FACTORY)
@Slf4j
public class ReaderConfig {
    public static final String READ_SQL_SESSION_FACTORY = "readSqlSessionFactory";
    @Value("${mybatis.mapperLocations:classpath:mybatisMappers/**/*.xml}")
    String mapperLocation;

    @Value("${mybatis.config-location:classpath:mybatis-config.xml}")
    String configLocation;

    @Value("${project.mybatis.data.security:false}")
    boolean security;

    @Bean(name = READ_SQL_SESSION_FACTORY)
    @ConditionalOnMissingBean(name = READ_SQL_SESSION_FACTORY)
    public SqlSessionFactory readSqlSessionFactory(@Qualifier(DataSourceConfig.READER_DATASOURCE) DataSource dataSource) throws Exception {
        NamingUtils.formatLog(log,"ReadSqlSessionFactory start=");
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setConfigLocation(resourcePatternResolver.getResource(configLocation));
        sessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources(mapperLocation));
        sessionFactoryBean.setTypeHandlers(new LongArrayTypeHandler(), new StringArrayTypeHandler());
        var sessionFactory = sessionFactoryBean.getObject();
        if (security) {
            var configuration = sessionFactory.getConfiguration();
            configuration.addInterceptor(new EncryptParameterInterceptor());
            configuration.addInterceptor(new DecryptResultFieldInterceptor());
        }
        return sessionFactory;

    }

//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer(){
//        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
//        //可以通过环境变量获取你的mapper路径,这样mapper扫描可以通过配置文件配置了
//        scannerConfigurer.setBasePackage("com.yourpackage.*.mapper");
//        return scannerConfigurer;
//    }
}
