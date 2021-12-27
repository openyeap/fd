package ltd.fdsa.starter.jdbc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Data
@Component
@ConfigurationProperties(JdbcApiProperties.PREFIX)
public class JdbcApiProperties {
    public static final String PREFIX = "project";
    private boolean enabled = true;

    private LinkedHashMap<String, DatabaseRule> databases;

    @Data
    public static class DatabaseRule {
        private String catalog;
        private String schema;
        private String url;
        private String driverClassName = "org.postgresql.Driver";
        private String username;
        private String password;
        private LinkedHashMap<String, TableNameRule> tables;

    }

    @Data
    public static class TableNameRule {
        private NameFix[] removes;
        private NameFix[] appends;
        private LinkedHashMap<String, String> replaces;
        private ColumnNameRule column;
        private Acl[] acl;
    }

    @Data
    public static class ColumnNameRule {
        private NameFix[] removes;
        private NameFix[] appends;
        private LinkedHashMap<String, String> replaces;
    }

    @Data
    public static class NameFix {
        private String prefix;
        private String suffix;
    }

    public static enum Acl {
        QueryList, QueryByKey,
        Create, Update, UpdateByKey, Delete,
        DeleteByKey;
    }
}
