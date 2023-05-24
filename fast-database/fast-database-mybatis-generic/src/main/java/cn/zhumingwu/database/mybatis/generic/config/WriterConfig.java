package cn.zhumingwu.database.mybatis.generic.config;

import cn.zhumingwu.base.util.NamingUtils;
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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "**.mybatis.generic.mapper.writer.**", annotationClass = MybatisGenericMapper.class, sqlSessionFactoryRef = "writeSqlSessionFactory")
@Slf4j
public class WriterConfig {
    @Value("${mybatis.mapperLocations:classpath:mybatisMappers/**/*.xml}")
    String mapperLocation;

    @Value("${mybatis.config-location:classpath:mybatis-config.xml}")
    String configLocation;

    @Value("${project.mybatis.data.security:false}")
    boolean security;

    @Bean(name = "writeSqlSessionFactory")
    @ConditionalOnMissingBean(name = "writeSqlSessionFactory")
    @Primary
    public SqlSessionFactory writeSqlSessionFactory(@Qualifier(DataSourceConfig.WRITER_DATASOURCE) DataSource dataSource) throws Exception {
        NamingUtils.formatLog(log,"WriteSqlSessionFactory Start For GENERIC");
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
