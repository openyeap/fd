package ltd.fdsa.starter.jdbc.controller;

import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.starter.jdbc.RdmlParser;
import ltd.fdsa.starter.jdbc.model.DBResult;
import ltd.fdsa.starter.jdbc.service.JdbcService;
import ltd.fdsa.web.controller.BaseController;
import ltd.fdsa.web.view.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/v2")
@Slf4j
public class JdbcController extends BaseController {

    @Autowired
    JdbcService jdbcService;


    @RequestMapping(value = "/api-docs", method = RequestMethod.GET)
    public Object getApiDocs() {

        Swagger swagger = new Swagger();
        log.debug("=========================================================");
        log.warn(this.request.getRemoteHost());
        log.debug("=========================================================");
        Object result = jdbcService.createTablesController("10.168.4.14/system-demo", "");
        return result;
    }

    @RequestMapping(value = "/tables", method = RequestMethod.GET, produces = "application/json")
    public Object getTables() {

        List<Object> result = new ArrayList();
        this.jdbcService.listAllTables("", "public").forEach(t -> {
            result.add(DBResult.builder()
                    .name(t.getName())
                    .content(t.getSchema().getName())
                    .build());
        });
        return Result.success(result);
    }

    @RequestMapping(value = "/{name}/columns", method = RequestMethod.GET, produces = "application/json")
    public Object getColumns(@PathVariable String name) {
        List<Object> result = new ArrayList<>();
        this.jdbcService.listAllFields(name, "", "").forEach(
                t -> {
                    result.add(DBResult.builder()
                            .name(t.getName())
                            .content(t.getColumnDefinition())
                            .build());
                }
        );
        return Result.success(result);
    }

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
            return this.jdbcService.create(sql.toString(), values);
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
            return this.jdbcService.update(sql.toString(), null);
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
            return this.jdbcService.update(sql.toString(), list);
        } catch (Exception ex) {
            return ex;
        }
    }

    @RequestMapping(value = "/{table}", method = RequestMethod.GET, produces = "application/json")
    public Object query(@PathVariable String table,
                        @RequestParam(defaultValue = "*") String select,
                        @RequestParam(defaultValue = "") String query,
                        @RequestParam(defaultValue = "") String order,
                        @RequestParam(defaultValue = "") String group,
                        @RequestParam(defaultValue = "") String having,
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
            return this.jdbcService.query(sql.toString(), null);
        } catch (Exception ex) {
            return ex;
        }
    }
}
