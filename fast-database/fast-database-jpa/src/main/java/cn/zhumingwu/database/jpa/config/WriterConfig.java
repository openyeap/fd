package cn.zhumingwu.database.jpa.config;

import cn.zhumingwu.database.config.DataSourceConfig;
import cn.zhumingwu.database.datasource.DataSourceProperties;
import cn.zhumingwu.database.jpa.registrar.UserIdAuditorAware;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.relational.core.dialect.AnsiDialect;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
@EnableJpaRepositories(basePackages = {"**.jpa.repository.writer.**"}, entityManagerFactoryRef = WriterConfig.WRITER_ENTITY_MANAGER_FACTORY, transactionManagerRef = WriterConfig.WRITER_TRANSACTION_MANAGER)
//@AutoConfigureAfter(DataSourceConfig.class)
@EnableJpaAuditing
@Slf4j
public class WriterConfig extends AbstractJdbcConfiguration {
    @Override
    public Dialect jdbcDialect(NamedParameterJdbcOperations operations) {
        return AnsiDialect.INSTANCE;

    }
    public static final String WRITER_ENTITY_MANAGER_FACTORY = "writerEntityManagerFactory";
    public static final String WRITER_TRANSACTION_MANAGER = "writerTransactionManager";

    @Value("${project.jpa.entity.packages:**.jpa.entity.**}")
    private String[] entityPackages;

    // 配置实体管理器工厂
    @Bean(name = WRITER_ENTITY_MANAGER_FACTORY)
    @Primary
    public LocalContainerEntityManagerFactoryBean writerEntityManagerFactory(@Qualifier(DataSourceConfig.WRITER_DATASOURCE) DataSource dataSource, DataSourceProperties properties) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(properties.isShowSql());
        adapter.setGenerateDdl(properties.isGenerateDdl());
        adapter.setDatabasePlatform(properties.getDialect());

        // 注入数据源
        entityManagerFactoryBean.setDataSource(dataSource);
        // 注入jpa厂商适配器
        entityManagerFactoryBean.setJpaVendorAdapter(adapter);
        // 设置扫描基本包
        entityManagerFactoryBean.setPackagesToScan(this.entityPackages);

        return entityManagerFactoryBean;
    }

    // 配置jpa事务管理器
    @Bean(name = WRITER_TRANSACTION_MANAGER)
    public PlatformTransactionManager writerTransactionManager(@Qualifier(WRITER_ENTITY_MANAGER_FACTORY) EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        // 配置实体管理器工厂
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    @Primary
    public UserIdAuditorAware userIdAuditorAware() {
        return new UserIdAuditorAware();
    }
}