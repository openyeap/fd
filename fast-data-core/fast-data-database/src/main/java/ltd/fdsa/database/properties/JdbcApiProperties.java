package ltd.fdsa.database.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;

@Data
@ConfigurationProperties(JdbcApiProperties.PREFIX)
public class JdbcApiProperties {
    public static final String PREFIX = "project.rest-api";
    private boolean enabled = true;

    private String title = "rest-ful api for database";
    private String description = "It is simple to open the rest-ful api for database";
    private String email = "zhumingwu@zhumingwu.cn";
    private String name = "zhumingwu";
    private String url = "http://blog.zhumingwu.cn";
    private String version = "1.1.0";
    private LinkedHashMap<String, TableNameRule> tables = new LinkedHashMap<String, TableNameRule>();
    private String catalog;
    private String schema;
    private LinkedHashMap<String, Acl[]> acl = new LinkedHashMap<String, Acl[]>();

    @Data
    public static class TableNameRule {
        private NameFix[] removes = new NameFix[0];
        private NameFix[] appends = new NameFix[0];
        private LinkedHashMap<String, String> replaces = new LinkedHashMap<String, String>();
        private ColumnNameRule column;

    }

    @Data
    public static class ColumnNameRule {
        private NameFix[] removes = new NameFix[0];
        private NameFix[] appends = new NameFix[0];
        private LinkedHashMap<String, String> replaces = new LinkedHashMap<String, String>();
    }

    @Data
    public static class NameFix {
        private String prefix;
        private String suffix;
    }

    public enum Acl {
        CREATE,
        QUERY_BY_KEY,
        UPDATE,
        UPDATE_BY_KEY,
        DELETE,
        DELETE_BY_KEY,
    }
}
