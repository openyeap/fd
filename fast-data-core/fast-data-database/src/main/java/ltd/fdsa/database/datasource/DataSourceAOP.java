package ltd.fdsa.database.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

/**
 * 切换数据源Advice
 */
@Slf4j
@Aspect
@Order(-1) // 保证该AOP在@Transactional之前执行
public class DataSourceAOP {

    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, DataSource ds) throws Throwable {
        String dataSourceName = ds.name();
        try {
            DataSourceKey dataSourceKey = DataSourceKey.valueOf(dataSourceName);
            DataSourceHolder.setDataSourceKey(dataSourceKey);
        } catch (Exception e) {
            log.error("数据源[{}]不存在，使用默认数据源 > {}", ds.name(), point.getSignature());
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, DataSource ds) {
        log.debug("Revert DataSource : {transIdo} > {}", ds.name(), point.getSignature());
        DataSourceHolder.clearDataSourceKey();
    }
}
