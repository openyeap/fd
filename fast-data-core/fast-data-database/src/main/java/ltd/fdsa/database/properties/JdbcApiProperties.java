package ltd.fdsa.database.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;

@Data
@ConfigurationProperties(JdbcApiProperties.PREFIX)
public class JdbcApiProperties {
    public static final String PREFIX = "project.rest-api";
    private boolean enabled = true;
    private LinkedHashMap<String, TableNameRule> tables;
    private String catalog;
    private String schema;
    private LinkedHashMap<String, Acl[]> acl;

    @Data
    public static class TableNameRule {
        private NameFix[] removes;
        private NameFix[] appends;
        private LinkedHashMap<String, String> replaces;
        private ColumnNameRule column;

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

    public enum Acl {
        QUERY_LIST,
        QUERY_BY_KEY,
        UPDATE_BY_KEY,
        CREATE,
        UPDATE,
        DELETE,
        DELETE_BY_KEY
    }
}
