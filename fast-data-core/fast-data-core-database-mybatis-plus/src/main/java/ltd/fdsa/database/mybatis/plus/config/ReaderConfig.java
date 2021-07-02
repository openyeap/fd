package ltd.fdsa.database.mybatis.plus.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.database.config.DataSourceConfig;
import ltd.fdsa.database.mybatis.plus.annotation.MybatisPlusMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "**.mybatis.plus.mapper.reader.**", annotationClass = MybatisPlusMapper.class, sqlSessionFactoryRef = ReaderConfig.READ_SQL_SESSION_FACTORY)
@Slf4j
public class ReaderConfig {
    public static final String READ_SQL_SESSION_FACTORY = "PLUS_READ_SQL_SESSION_FACTORY";
    @Value("${mybatis.mapperLocations:classpath:mybatisMappers/**/*.xml}")
    String mapperLocation;

    @Value("${mybatis.config-location:classpath:mybatis-config.xml}")
    String configLocation;

    @Value("${project.mybatis.data.security:false}")
    boolean security;

    @Bean(name = READ_SQL_SESSION_FACTORY)
    public SqlSessionFactory readSqlSessionFactory(@Qualifier(DataSourceConfig.READER_DATASOURCE) DataSource dataSource) throws Exception {
        NamingUtils.formatLog(log,"ReadSqlSessionFactory start For PLUS");
        final MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();

        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setConfigLocation(resourcePatternResolver.getResource(configLocation));
        sessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources(mapperLocation));
        var globalConfig = new GlobalConfig();
        globalConfig.setBanner(false);
        sessionFactoryBean.setGlobalConfig(globalConfig);
        var sessionFactory = sessionFactoryBean.getObject();
        if (security) {
            var configuration = sessionFactory.getConfiguration();
//            configuration.addInterceptor(new EncryptParameterInterceptor());
//            configuration.addInterceptor(new DecryptResultFieldInterceptor());
        }
        return sessionFactory;

    }
}
