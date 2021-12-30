package ltd.fdsa.database.mybatis.config;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.database.config.DataSourceConfig;
import ltd.fdsa.database.mybatis.handler.LongArrayTypeHandler;
import ltd.fdsa.database.mybatis.handler.StringArrayTypeHandler;
import ltd.fdsa.database.mybatis.interceptor.DecryptResultFieldInterceptor;
import ltd.fdsa.database.mybatis.interceptor.EncryptParameterInterceptor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "**.mybatis.mapper.writer.**", annotationClass = Mapper.class, sqlSessionFactoryRef = WriterConfig.WRITE_SQL_SESSION_FACTORY)
@Slf4j
public class WriterConfig {
    public static final String WRITE_SQL_SESSION_FACTORY = "writeSqlSessionFactory";

    @Value("${mybatis.mapperLocations:classpath:mybatisMappers/**/*.xml}")
    String mapperLocation;

    @Value("${mybatis.config-location:classpath:mybatis-config.xml}")
    String configLocation;

    @Value("${project.mybatis.data.security:false}")
    boolean security;

    @Bean(name = WRITE_SQL_SESSION_FACTORY)
    @ConditionalOnMissingBean(name = WRITE_SQL_SESSION_FACTORY)
    @Primary
    public SqlSessionFactory writeSqlSessionFactory(@Qualifier(DataSourceConfig.WRITER_DATASOURCE) DataSource dataSource) throws Exception {
        NamingUtils.formatLog(log, "WriteSqlSessionFactory Start");
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
}
