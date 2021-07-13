package ltd.fdsa.database.jpa.config;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.database.config.DataSourceConfig;
import ltd.fdsa.database.datasource.DataSourceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@Configuration
//@ScanJpaRepositories(basePackages = {
////        "${project.jpa.reader.packages}",
//        "ltd.fdsa.database.reader"
//},
//        entityManagerFactoryRef = ReaderConfig.READER_ENTITY_MANAGER_FACTORY,
//        transactionManagerRef = ReaderConfig.readerTransactionManager)
@EnableJpaRepositories(basePackages = {"**.jpa.repository.reader.**"}, entityManagerFactoryRef = ReaderConfig.READER_ENTITY_MANAGER_FACTORY, transactionManagerRef = ReaderConfig.READER_TRANSACTION_MANAGER)
//@AutoConfigureAfter(DataSourceConfig.class)
@Slf4j
public class ReaderConfig {
    public static final String READER_ENTITY_MANAGER_FACTORY = "readerEntityManagerFactory";
    public static final String READER_TRANSACTION_MANAGER = "readerTransactionManager";

    @Value("${project.jpa.entity.packages:**.jpa.entity.**}")
    private String[] entityPackages;

    // 配置实体管理器工厂
    @Bean(name = ReaderConfig.READER_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean readerEntityManagerFactory(@Qualifier(DataSourceConfig.READER_DATASOURCE) DataSource dataSource, DataSourceProperties properties) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(properties.isShowSql());
        // adapter.setGenerateDdl(properties.isGenerateDdl());
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
    @Bean(name = ReaderConfig.READER_TRANSACTION_MANAGER)
    public PlatformTransactionManager readerTransactionManager(@Qualifier(ReaderConfig.READER_ENTITY_MANAGER_FACTORY) EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        // 配置实体管理器工厂
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
