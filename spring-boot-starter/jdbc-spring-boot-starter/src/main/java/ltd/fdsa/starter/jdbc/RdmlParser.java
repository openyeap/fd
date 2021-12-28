package ltd.fdsa.starter.jdbc;

import com.google.common.base.Strings;
import lombok.var;
import ltd.fdsa.database.sql.schema.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parser of the RDML
 *
 * <p>RDML is a RESTful data manipulation language version: 1.0 select=alias:field,alias:field
 * query=field.opt.value;field.opt.value,(field.opt.value;field.opt.value)
 * order=field.desc,field.asc page=0-n size=1-N
 */
public final class RdmlParser {

    public String parseSelect(String input) {
        if (Strings.isNullOrEmpty(input)) {
            return "select *";
        }
        String[] segments = input.split(",");
        List<String> list = new ArrayList<String>(segments.length);
        for (String segment : segments) {
            String[] fields = segment.split(":");
            if (fields.length == 2) {
                list.add(fields[1] + " as " + fields[0]);
            } else {
                list.add(segment);
            }
        }
        return "select \n" + String.join(",\n", list);
    }

    public String parseQuery(String input) {
        if (Strings.isNullOrEmpty(input)) {
            return "";
        }
        String sql = input.replace(",", " or ").replace(";", " and ").replace(".", " ");
        return "where " + sql;
    }

    public String parseOrder(String input) {
        if (Strings.isNullOrEmpty(input)) {
            return "";
        }
        String sql = input.replace(".", " ");
        return "order by " + sql;
    }



    public String parseSelect(Table table, String select) {

        var columns = "";
        if (Strings.isNullOrEmpty(select) || select.contains("*")) {
            columns = Arrays.stream(table.getColumns())
                    .map(m -> m.getName() + " as " + m.getAlias()).collect(Collectors.joining(","));
        } else {
            columns = Arrays.stream(table.getColumns()).filter(column -> select.contains(column.getAlias()))
                    .map(m -> m.getName() + " as " + m.getAlias()).collect(Collectors.joining(","));
        }

        return "select " + columns + " from " + table.getName();
    }


    public String parseQuery(Table table, String where) {
        if (Strings.isNullOrEmpty(where)) {
            return "";
        }

        StringBuilder sql = new StringBuilder();

        for (String item : where.split(";")) {
            item = item.trim();
            if (Strings.isNullOrEmpty(item)) {
                continue;
            }
            if (item.contains(":")) {
                var propertyArray = item.split(":");
                // 属性名
                var propertyName = propertyArray[0].trim();
                // 属性值
                var propertyValue = propertyArray[1].trim();
                // 数据库字段名
                var optionalColumn = Arrays.stream(table.getColumns()).filter(column -> column.getAlias().equals(propertyName)).findFirst();
                if (!optionalColumn.isPresent()) {
                    continue;
                }
                sql.append(" and ").append(optionalColumn.get().getName()).append("=").append("'").append(propertyValue).append("' ");
            } else {
                var propertyArray = item.split("!");
                // 属性名
                var propertyName = propertyArray[0].trim();
                // 属性值
                var propertyValue = propertyArray[1].trim();
                // 数据库字段名
                var optionalColumn = Arrays.stream(table.getColumns()).filter(column -> column.getAlias().equals(propertyName)).findFirst();
                if (!optionalColumn.isPresent()) {
                    continue;
                }
                sql.append(" and ").append(optionalColumn.get().getName()).append(" like ").append("'%").append(propertyValue).append("%'");

            }
        }
        return "where 1=1 " + sql.toString();
    }

    public String parseOrder(Table table, String order) {
        if (Strings.isNullOrEmpty(order)) {
            return "";
        }
        var orders = order.split(".");
        if (orders.length <= 0) {
            return "";
        }
        var orderBy = orders[0].trim();
        if (Strings.isNullOrEmpty(orderBy)) {
            return "";
        }
        var sort = "asc";
        if (orders.length == 2) {
            sort = sort.equals(orders[1]) ? "asc" : "desc";
        }
        var optionalColumn = Arrays.stream(table.getColumns()).filter(column -> column.getAlias().equals(orderBy)).findFirst();
        if (!optionalColumn.isPresent()) {
            return "";
        }
        return " order by " + optionalColumn.get().getName() + " " + sort;
    }

    public String parsePage(int page, int size) {
        if (page < 0 || size < 1) {
            return "";
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" limit ");
        sql.append(size);
        sql.append(" offset ");
        sql.append(page * size);
        return sql.toString();
    }
}
