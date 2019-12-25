package ltd.fdsa.fdsql.web.config;

import lombok.Data;
import ltd.fdsa.fdsql.web.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Classname TablesFilterConfig
 * @Description TODO
 * @Date 2019/12/24 16:27
 * @Author 高进
 */
@Data
@Component
@ConfigurationProperties(TablesFilterConfig.prefix)
public class TablesFilterConfig {

    public final static String prefix = "spring.daoshu.tables";

    /**
     * special _  replace *_
     */
    private String[] prefixMatches;

    /**
     * special _  replace *_
     */
    private String[] prefixNotMatches;

    /**
     * special _  replace *_
     */
    private String[] suffixMatches;

    /**
     * special _  replace *_
     */
    private String[] suffixNotMatches;

    /**
     * special _  replace *_
     */
    private String[] containers;

    @Override
    public String toString() {
        return "TablesFilterConfig{" +
                "prefixMatches='" + Arrays.toString(prefixMatches) + "'\n" +
                "prefixNotMatches='" + Arrays.toString(prefixNotMatches) + "'\n" +
                "suffixMatches='" + Arrays.toString(suffixMatches) + "'\n" +
                "suffixNotMatches='" + Arrays.toString(suffixNotMatches) + "'\n" +
                "}";
    }

    @Autowired
    private Repository repository;

    public List<TablesRemark> queryTables() {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT relname AS tablename, ");
        sql.append(" cast( obj_description ( relfilenode, 'pg_class' ) AS VARCHAR ) AS COMMENT ");
        sql.append(" FROM pg_class ");
        sql.append(" WHERE relname IN ( SELECT tablename FROM pg_tables WHERE schemaname = 'public' AND position( '_2' IN tablename ) = 0 ) ");

        boolean hasMatches = false;
        sql.append(" and (1=2 ");
        if (prefixMatches != null) {
            for (String prefix : prefixMatches) {
                sql.append(" or relname like '" + prefix + "%' ");
                if(prefix.contains("*")) {
                    sql.append(" ESCAPE '*' ");
                }
            }
        }
        if (prefixNotMatches != null) {
            for (String prefix : prefixNotMatches) {
                sql.append(" or relname not like '" + prefix + "%' ");
                if(prefix.contains("*")) {
                    sql.append(" ESCAPE '*' ");
                }
            }
        }
        if (suffixMatches != null) {
            for (String suffix : suffixMatches) {
                sql.append(" or relname like '%" + suffix + "' ");
                if(suffix.contains("*")) {
                    sql.append(" ESCAPE '*' ");
                }
            }
        }
        if (suffixNotMatches != null) {
            for (String suffix : suffixNotMatches) {
                sql.append(" or relname not like '%" + suffix + "' ");
                if(suffix.contains("*")) {
                    sql.append(" ESCAPE '*' ");
                }
            }
        }
        if(containers != null) {
            for(String container : containers) {
                sql.append(" or relname like '%" + container + "%' ");
                if(container.contains("*")) {
                    sql.append(" ESCAPE '*' ");
                }
            }
        }

        sql.append(")");

        Object obj = repository.query(sql.toString());
        if (obj == null) {
            return null;
        }
        List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) obj;
        List<TablesRemark> tables = new ArrayList<>();
        for (HashMap<String, Object> map : list) {
            String tableName = (map.get("tablename") == null ? null : map.get("tablename").toString());
            if (tableName == null) {
                continue;
            }
            TablesRemark tablesRemark = new TablesRemark();
            tablesRemark.setTableName(tableName);
            tablesRemark.setComment(map.get("comment") == null ? "" : map.get("comment").toString());
            tables.add(tablesRemark);
        }
        return tables;
    }

    public List<ColumnRemark> queryColumns(String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT col_description ( a.attrelid, a.attnum ) AS COMMENT, ");
        sql.append(" a.attname AS NAME ");
        sql.append(" FROM pg_class AS c, pg_attribute AS a ");
        sql.append(" WHERE c.relname = '" + tableName + "' ");
        sql.append(" AND a.attrelid = c.oid AND a.attnum > 0 ");
        Object obj = repository.query(sql.toString());
        if (obj == null) {
            return null;
        }
        List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) obj;
        List<ColumnRemark> columns = new ArrayList<>();
        for (HashMap<String, Object> map : list) {
            String colName = (map.get("name") == null ? null : map.get("name").toString());
            if (colName == null) {
                continue;
            }
            ColumnRemark columnRemark = new ColumnRemark();
            columnRemark.setColName(colName);
            columnRemark.setComment(map.get("comment") == null ? "" : map.get("comment").toString());
            columns.add(columnRemark);
        }
        return columns;
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
