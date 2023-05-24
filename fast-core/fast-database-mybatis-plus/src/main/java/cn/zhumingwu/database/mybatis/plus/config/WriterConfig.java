package cn.zhumingwu.database.mybatis.plus.config;

import cn.zhumingwu.database.mybatis.plus.annotation.MybatisPlusMapper;
import cn.zhumingwu.database.mybatis.plus.interceptor.AutoUpdateHandler;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.core.util.NamingUtils;
import cn.zhumingwu.database.config.DataSourceConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "**.mybatis.plus.mapper.writer.**", annotationClass = MybatisPlusMapper.class, sqlSessionFactoryRef = WriterConfig.WRITE_SQL_SESSION_FACTORY)
@Slf4j
public class WriterConfig {
    public static final String WRITE_SQL_SESSION_FACTORY = "PLUS_WRITE_SQL_SESSION_FACTORY";

    @Value("${mybatis.mapperLocations:classpath:mybatisMappers/**/*.xml}")
    String mapperLocation;

    @Value("${mybatis.config-location:classpath:mybatis-config.xml}")
    String configLocation;

    @Value("${project.mybatis.data.security:false}")
    boolean security;

    @Bean(name = WRITE_SQL_SESSION_FACTORY)
    public SqlSessionFactory writeSqlSessionFactory(@Qualifier(DataSourceConfig.WRITER_DATASOURCE) DataSource dataSource) throws Exception {
        NamingUtils.formatLog(log, "WriteSqlSessionFactory Start For PLUS");
        final MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
        PathMatchingResourcePatternResolver pr = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setConfigLocation(pr.getResource(configLocation));
        sessionFactoryBean.setMapperLocations(pr.getResources(mapperLocation));
        var globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new AutoUpdateHandler());
        globalConfig.setBanner(false);
        sessionFactoryBean.setGlobalConfig(globalConfig);
//        sessionFactoryBean.setTypeHandlers(new LongArrayTypeHandler(), new StringArrayTypeHandler()); 
        var sessionFactory = sessionFactoryBean.getObject();
        if (security) {
            var configuration = sessionFactory.getConfiguration();
//            configuration.addInterceptor(new EncryptParameterInterceptor());
//            configuration.addInterceptor(new DecryptResultFieldInterceptor());
        }
        return sessionFactory;
    }

}
