package ltd.fdsa.database.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
public class DataSourceProperties {
    public static final String PREFIX = "spring.datasource";
    private DruidDataSource master;
    private List<DruidDataSource> slaves = new ArrayList<>();

    private boolean ddl;

    private boolean sql;

    private String dialect ="org.hibernate.dialect.SQLiteDialect";

}
