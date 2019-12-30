package ltd.fdsa.fdsql.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ltd.fdsa.fdsql.parser.RdmlParser;
import ltd.fdsa.fdsql.web.repository.Repository;

@RestController
@RequestMapping("/api")
@Log4j2
public class ApiController {
    @Autowired
    private Repository repository;

    @RequestMapping(value = "/{table}", method = RequestMethod.PUT, produces = "application/json")
    public Object create(@PathVariable String table, @RequestBody Map<String, Object> data) {
        try {
            if (data == null || data.size() == 0) {
                return "data不能为空";
            }
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + table + " ( ");
            int i = 0;
            int size = data.size();
            String[] values = new String[size];
            for (Map.Entry<String, Object> map : data.entrySet()) {
                sql.append(map.getKey());
                values[i] = map.getValue().toString();
                i++;
                if (i < size) {
                    sql.append(",");
                }
            }
            sql.append(" ) ");
            sql.append(" values ( ");
            for (int j = 0; j < size; j++) {
                if (j == size - 1) {
                    sql.append("?");
                } else {
                    sql.append("?,");
                }
            }
            sql.append(" ) ");
            return repository.create(sql.toString(), values);
        } catch (Exception ex) {
            return ex;
        }
    }

    @RequestMapping(value = "/{table}", method = RequestMethod.DELETE, produces = "application/json")
    public Object delete(@PathVariable String table, @RequestParam(defaultValue = "") String where) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" delete from " + table + " ");
            sql.append(" where " + where);
            return repository.update(sql.toString(), null);
        } catch (Exception ex) {
            return ex;
        }
    }

    @RequestMapping(value = "/{table}", method = RequestMethod.POST, produces = "application/json")
    public Object update(@PathVariable String table, @RequestParam(defaultValue = "") String where, @RequestBody Map<String, Object> data) {
        try {
            if (data == null || data.size() == 0) {
                return "data不能为空";
            }
            StringBuffer sql = new StringBuffer();
            sql.append(" update " + table);
            sql.append(" set ");
            int i = 0;
            int size = data.size();
            List<Object> list = new ArrayList<>();
            for (Map.Entry<String, Object> map : data.entrySet()) {
                sql.append(map.getKey() + "= ? ");
                list.add(map.getValue());
                i++;
                if (i < size) {
                    sql.append(",");
                }
            }
            if (where != null && !"".equals(where)) {
                sql.append(" where " + where);
            }
            return repository.update(sql.toString(), list);
        } catch (Exception ex) {
            return ex;
        }
    }

    @RequestMapping(value = "/{table}", method = RequestMethod.GET, produces = "application/json")
    public Object query(@PathVariable String table, @RequestParam(defaultValue = "*") String select,
                        @RequestParam(defaultValue = "") String query, @RequestParam(defaultValue = "") String order,
                        @RequestParam(defaultValue = "") String group, @RequestParam(defaultValue = "") String having,
                        @RequestParam(required = false, defaultValue = "0") int page,
                        @RequestParam(required = false, defaultValue = "20") int size) {

        try {
            StringBuffer sql = new StringBuffer();
            RdmlParser parser = new RdmlParser();
            sql.append(parser.parseSelect(select));
            sql.append("\r\n");
            sql.append("from ");
            sql.append(table);
            sql.append("\r\n");
            sql.append(parser.parseQuery(query));
            sql.append("\r\n");
            sql.append(parser.parseOrder(order));
            sql.append("\r\n");
            sql.append(parser.parsePage(page, size));
            log.info(sql.toString());
            return repository.query(sql.toString());
        } catch (Exception ex) {
            return ex;
        }
    }
}