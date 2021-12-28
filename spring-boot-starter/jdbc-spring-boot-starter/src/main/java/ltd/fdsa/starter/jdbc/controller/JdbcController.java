package ltd.fdsa.starter.jdbc.controller;

import com.google.common.base.Strings;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.database.sql.dialect.Dialects;
import ltd.fdsa.database.sql.domain.Placeholder;
import ltd.fdsa.database.sql.queries.Queries;
import ltd.fdsa.database.sql.utils.Indentation;
import ltd.fdsa.database.fql.antlr.FqlLexer;
import ltd.fdsa.database.fql.antlr.FqlParser;
import ltd.fdsa.starter.jdbc.RdmlParser;
import ltd.fdsa.starter.jdbc.model.DBResult;
import ltd.fdsa.database.service.JdbcApiService;
import ltd.fdsa.web.controller.BaseController;
import ltd.fdsa.web.enums.HttpCode;
import ltd.fdsa.database.fql.JdbcFqlVisitor;
import ltd.fdsa.web.view.Result;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/v2")
@Slf4j
public class JdbcController extends BaseController {

    @Autowired
    JdbcApiService service;

    @RequestMapping(value = "/api-docs", method = RequestMethod.GET)
    public Swagger getApiDocs(@RequestParam(value = "group", required = false) String group, HttpServletRequest request) {
        UriComponents components = UriComponentsBuilder.fromHttpRequest(new ServletServerHttpRequest(request)).build();
        String host = components.getHost();
        if (components.getPort() > 0) {
            host += ":" + components.getPort();
        }
        if (Strings.isNullOrEmpty(group)) {
            group = "/";
        } else {
            group = "/" + group;
        }
        Swagger swagger = this.service.getApiDocs(host, group);
        return swagger;
    }


    @RequestMapping(value = "/tables", method = RequestMethod.GET, produces = "application/json")
    public Result getTables() {
        return Result.success(this.service.getNamedTables().values());
    }

    @RequestMapping(value = "/{table}/columns", method = RequestMethod.GET, produces = "application/json")
    public Result getColumns(@PathVariable String table) {
        var list = this.service.getNamedColumns().get(table).values().stream().map(t ->
                DBResult.builder()
                        .name(t.getName())
                        .content(t.getColumnDefinition())
                        .build()
        ).collect(Collectors.toList());
        return Result.success(list);
    }

    @RequestMapping(value = "/create/{table}", method = RequestMethod.PUT, produces = "application/json")
    public Result createTable(@PathVariable String table, @RequestBody Map<String, Object> data) {

        try {
            if (data == null || data.size() == 0) {
                return Result.fail(400, "新增加数据不能为空！");
            }
            var tb = this.service.getNamedTables().get(table);
            if (tb == null) {
                return Result.fail(HttpCode.NOT_FOUND, "没有相关数据！");
            }
            // 数据库字段名
            var first = this.service.getNamedColumns().get(tb);
            if (first == null) {
                return Result.fail(HttpCode.NOT_FOUND, "没有相关数据！");
            }

            var sql = Queries.insertInto(tb);
            var map = new LinkedHashMap<String, Object>();

            for (var entry : data.entrySet()) {

                var column = first.get(entry.getKey());
                if (column == null) {
                    continue;
                }
                switch (column.getColumnDefinition().getDefinitionName()) {
                    case "DATE":
                        map.put(entry.getKey(), new SimpleDateFormat("yyyy-MM-dd").parse(entry.getValue().toString()));
                        break;
                    case "TIMESTAMP":
                    case "DATETIME":
                        map.put(entry.getKey(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entry.getValue().toString()));
                        break;
                    case "TIME":
                        map.put(entry.getKey(), new SimpleDateFormat("HH:mm:ss").parse(entry.getValue().toString()));
                        break;
                    case "BOOLEAN":
                    case "BOOL":
                        map.put(entry.getKey(), Boolean.valueOf(entry.getValue().toString()));
                        break;
                    default:
                        System.out.println(column.getColumnDefinition().getDefinitionName());
                        log.warn("没有考虑到的类型：{}", column.getColumnDefinition().getDefinitionName());
                        map.put(entry.getKey(), entry.getValue());
                }
                sql.set(column, Placeholder.placeholder(column));

            }
            log.warn(sql.build(Dialects.POSTGRE, Indentation.indent(true)));
            return Result.success(this.service.create(sql.build(Dialects.POSTGRE, Indentation.indent(true)), map));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

    @RequestMapping(value = "/{table}", method = RequestMethod.PUT, produces = "application/json")
    public Result create(@PathVariable String table, @RequestBody Map<String, Object> data) {
        try {
            if (data == null || data.size() == 0) {
                return Result.fail(HttpCode.BAD_REQUEST, "Put data can not be empty");
            }
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + table + " ( ");
            int i = 0;
            int size = data.size();
            Object[] values = new String[size];
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
            return Result.success(this.service.update(sql.toString(), values));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

    @RequestMapping(value = "/{table}", method = RequestMethod.DELETE, produces = "application/json")
    public Result delete(@PathVariable String table, @RequestParam(defaultValue = "") String where) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" delete from " + table + " ");
            sql.append(" where " + where);
            return Result.success(this.service.update(sql.toString()));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

    @RequestMapping(value = "/{table}", method = RequestMethod.POST, produces = "application/json")
    public Result update(@PathVariable String table, @RequestParam(defaultValue = "") String where, @RequestBody Map<String, Object> data) {
        try {
            if (data == null || data.size() == 0) {
                return Result.fail(400, "data不能为空");
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
            return Result.success(this.service.update(sql.toString(), list));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

    @RequestMapping(value = "/{table}", method = RequestMethod.GET, produces = "application/json")
    public Result query(@PathVariable String table,
                        @RequestParam(defaultValue = "*") String select,
                        @RequestParam(defaultValue = "") String query,
                        @RequestParam(defaultValue = "") String order,
                        @RequestParam(required = false, defaultValue = "0") int page,
                        @RequestParam(required = false, defaultValue = "20") int size) {

        try {
            StringBuffer sql = new StringBuffer();
            RdmlParser parser = new RdmlParser();
            sql.append(parser.parseSelect(select));
            sql.append("\n");
            sql.append("from ");
            sql.append(table);
            sql.append("\n");
            sql.append(parser.parseQuery(query));
            sql.append("\n");
            sql.append(parser.parseOrder(order));
            sql.append("\n");
            sql.append(parser.parsePage(page, size));
            log.info(sql.toString());
            return Result.success(this.service.query(sql.toString(), null));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST, produces = "application/json")
    public Result queryMulti(@RequestBody String query) {
        try {
            if (Strings.isNullOrEmpty(query)) {
                return Result.fail(400, "QUERY string can not be empty");
            }
            query = query.trim();
            if (!query.startsWith("{") && !query.endsWith("}")) {
                query = "{" + query + "}";
            }


            CharStream input = CharStreams.fromString(query);

            var lexer = new FqlLexer(input);
            var tokens = new CommonTokenStream(lexer);
            var parser = new FqlParser(tokens);
            var document = parser.document();
            var visitor = new JdbcFqlVisitor(this.service);
            return Result.success(visitor.visit(document).getObject());
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }
}
