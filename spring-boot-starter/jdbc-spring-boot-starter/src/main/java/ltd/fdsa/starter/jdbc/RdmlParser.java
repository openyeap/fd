package ltd.fdsa.starter.jdbc;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

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
