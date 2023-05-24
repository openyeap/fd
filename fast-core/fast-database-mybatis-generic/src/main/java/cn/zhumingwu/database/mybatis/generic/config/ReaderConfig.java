package cn.zhumingwu.database.mybatis.generic.config;

import cn.zhumingwu.core.util.NamingUtils;
import cn.zhumingwu.database.config.DataSourceConfig;
import cn.zhumingwu.database.mybatis.generic.annotation.MybatisGenericMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "**.mybatis.generic.mapper.reader.**", annotationClass = MybatisGenericMapper.class, sqlSessionFactoryRef = "readSqlSessionFactory")
@Slf4j
public class ReaderConfig {

    @Value("${mybatis.mapperLocations:classpath:mybatisMappers/**/*.xml}")
    String mapperLocation;

    @Value("${mybatis.config-location:classpath:mybatis-config.xml}")
    String configLocation;

    @Value("${project.mybatis.data.security:false}")
    boolean security;

    @Bean(name = "readSqlSessionFactory")
    @ConditionalOnMissingBean(name = "readSqlSessionFactory")
    public SqlSessionFactory readSqlSessionFactory(@Qualifier(DataSourceConfig.READER_DATASOURCE) DataSource dataSource) throws Exception {
        NamingUtils.formatLog(log,"ReadSqlSessionFactory start For GENERIC");
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setConfigLocation(resourcePatternResolver.getResource(configLocation));
        sessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources(mapperLocation));

        //        sessionFactoryBean.setTypeHandlers(new LongArrayTypeHandler(), new StringArrayTypeHandler());
        sessionFactoryBean.setDefaultEnumTypeHandler(EnumOrdinalTypeHandler.class);
        var sessionFactory = sessionFactoryBean.getObject();
        if (security) {
            var configuration = sessionFactory.getConfiguration();
//            configuration.addInterceptor(new EncryptParameterInterceptor());
//            configuration.addInterceptor(new DecryptResultFieldInterceptor());
        }
        return sessionFactory;

    }
}
