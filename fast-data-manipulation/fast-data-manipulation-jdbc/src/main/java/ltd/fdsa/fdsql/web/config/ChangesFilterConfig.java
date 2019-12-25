package ltd.fdsa.fdsql.web.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Classname ChangesFilterConfig
 * @Description TODO
 * @Date 2019/12/25 9:33
 * @Author 高进
 */
@Data
@Component
@Log4j2
@ConfigurationProperties(ChangesFilterConfig.prefix)
public class ChangesFilterConfig implements ApplicationListener<ApplicationStartedEvent> {
    public final static String prefix = "spring.daoshu.changes";

    private List<Map<String, String>> append;

    private String[] remove;

    private Map<String, String> replace;

    /**
     * 记录数据库表与新表对照关系
     * 如果替换出现重复的话，暂时不考虑
     */
    private static List<TablesChange> tablesChangeList;

    @Autowired
    private TablesFilterConfig tablesFilterConfig;

    public List<TablesFilterConfig.TablesRemark> queryTables() {
        tablesChangeList = new ArrayList<>();
        List<TablesFilterConfig.TablesRemark> tablesRemarks = tablesFilterConfig.queryTables();
        for(TablesFilterConfig.TablesRemark tablesRemark : tablesRemarks) {
            String oldTableName = tablesRemark.getTableName();
            String newTableName = getNewTableName(oldTableName);
            //数据库名字替换成新名字
            tablesRemark.setTableName(newTableName);
            //保存数据库名字与新名字的对照关系
            TablesChange tablesChange = new TablesChange();
            tablesChange.setOldTableName(oldTableName);
            tablesChange.setNewTableName(newTableName);
            tablesChangeList.add(tablesChange);
        }
        return tablesRemarks;
    }

    public List<TablesChange> getTablesChangeList() {
        return tablesChangeList;
    }

    public List<TablesFilterConfig.ColumnRemark> getColumnsList(String tableName){
        if(StringUtils.isEmpty(tableName)) {
            return null;
        }
        String dsTableName = getDsTableName(tableName);
        return tablesFilterConfig.queryColumns(dsTableName);
    }

    /**
     * 前端传入的新表名称
     * @param tableName
     * @return null-->未找到匹配的表
     */
    public String getDsTableName(String tableName) {
        if(tablesChangeList != null) {
            for(TablesChange tablesChange : tablesChangeList) {
                if(tableName.equals(tablesChange.getNewTableName())) {
                    return tablesChange.getOldTableName();
                }
            }
        }
        return null;
    }

    private final static String PREFIX = "prefix";
    private final static String SUFFIX = "suffix";

    private String getNewTableName(String tableName) {
        String newTableName = tableName;
        if (append != null) {
            for (Map<String, String> map : append) {
                if (map != null) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String key = entry.getKey();
                        if(!key.contains(".")) {
                            continue;
                        }
                        if(!tableName.startsWith(key.split("\\.")[0])) {
                            continue;
                        }
                        if (PREFIX.equals(key.split("\\.")[1])) {
                            newTableName = entry.getValue() + newTableName;
                        } else if (SUFFIX.equals(key.split("\\.")[1])) {
                            newTableName = newTableName + entry.getValue();
                        }
                    }
                }
            }
        }
        if (remove != null) {
            for (String rm : remove) {
                newTableName = newTableName.replaceAll(rm, "");
            }
        }
        if (replace != null) {
            for (Map.Entry<String, String> entry : replace.entrySet()) {
                newTableName = newTableName.replaceAll(entry.getKey(), entry.getValue());
            }
        }
        return newTableName;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("==========ApplicationStartedEvent DatabaseRegister start===========");
        queryTables();
        log.info("==========ApplicationStartedEvent DatabaseRegister end===========");
    }

    @Data
    public static class TablesChange {
        private String oldTableName;
        private String newTableName;
    }

}
