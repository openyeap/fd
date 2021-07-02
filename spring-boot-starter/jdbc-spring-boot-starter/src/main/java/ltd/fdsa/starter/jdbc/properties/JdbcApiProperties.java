package ltd.fdsa.starter.jdbc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Data
@Component
@ConfigurationProperties(JdbcApiProperties.PREFIX)
public class JdbcApiProperties {
    public static final String PREFIX = "project.tables";

    private boolean enabled = true;
    /**
     * special _ replace *_
     */
    private String[] prefixMatches;

    /**
     * special _ replace *_
     */
    private String[] prefixNotMatches;

    /**
     * special _ replace *_
     */
    private String[] suffixMatches;

    /**
     * special _ replace *_
     */
    private String[] suffixNotMatches;

    /**
     * special _ replace *_
     */
    private String[] containers;

    @Override
    public String toString() {
        return "TablesFilterConfig{"
                + "prefixMatches='"
                + Arrays.toString(prefixMatches)
                + "'\n"
                + "prefixNotMatches='"
                + Arrays.toString(prefixNotMatches)
                + "'\n"
                + "suffixMatches='"
                + Arrays.toString(suffixMatches)
                + "'\n"
                + "suffixNotMatches='"
                + Arrays.toString(suffixNotMatches)
                + "'\n"
                + "}";
    }

    @Data
    public static class TablesRemark {
        private String tableName;
        private String comment;
    }

    @Data
    public static class ColumnRemark {
        private String colName;
        private String comment;
    }
}
