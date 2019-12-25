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
     * 前缀匹配
     */
    private String[] prefixMatches;

    /**
     * 前缀不匹配
     */
    private String[] prefixNotMatches;

    /**
     * 后缀匹配
     */
    private String[] suffixMatches;

    /**
     * 后缀不匹配
     */
    private String[] suffixNotMatches;

    /**
     * 包含
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
        sql.append(" and (1=1 ");
        if (prefixMatches != null) {
            for (String prefix : prefixMatches) {
                sql.append(" or relname like '" + prefix + "%'");
            }
        }
        if (prefixNotMatches != null) {
            for (String prefix : prefixNotMatches) {
                sql.append(" or relname not like '" + prefix + "%' ");
            }
        }
        if (suffixMatches != null) {
            for (String suffix : suffixMatches) {
                sql.append(" or relname like '%" + suffix + "' ");
            }
        }
        if (suffixNotMatches != null) {
            for (String suffix : suffixNotMatches) {
                sql.append(" or relname not like '%" + suffix + "' ");
            }
        }
        if(containers != null) {
            for(String container : containers) {
                sql.append(" or relname like '%" + container + "%' ");
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

    @Data
    public static class TablesRemark {
        private String tableName;
        private String comment;
    }
}
