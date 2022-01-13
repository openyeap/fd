package ltd.fdsa.starter.jdbc.controller;

import com.google.common.base.Strings;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.database.fql.JdbcFqlVisitor;
import ltd.fdsa.database.fql.antlr.FqlLexer;
import ltd.fdsa.database.fql.antlr.FqlParser;
import ltd.fdsa.database.service.JdbcApiService;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.conditions.Condition;
import ltd.fdsa.database.sql.domain.Placeholder;
import ltd.fdsa.database.sql.queries.Queries;
import ltd.fdsa.starter.jdbc.model.ViewResult;
import ltd.fdsa.starter.jdbc.model.ViewUpdate;
import ltd.fdsa.web.controller.BaseController;
import ltd.fdsa.web.enums.HttpCode;
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
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/v2")
@Slf4j
public class JdbcApiController extends BaseController {

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

    @RequestMapping(value = "/models", method = RequestMethod.GET, produces = "application/json")
    public Result<Object> getModels() {
        var result = this.service.getNamedTables().values().stream().map(table -> ViewResult
                .builder()
                .name(table.getAlias())
                .content(table.getSchema().getName())
                .remark(table.getRemark())
                .build()).toArray();
        return Result.success(result);
    }

    @RequestMapping(value = "/{model}/columns", method = RequestMethod.GET, produces = "application/json")
    public Result<Object> getColumns(@PathVariable String model) {
        var table = this.service.getNamedTables().get(model);
        if (table == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data resource！");
        }
        var result = this.service.getNamedColumns().get(table.getAlias()).values().stream().map(column -> ViewResult
                .builder()
                .name(column.getAlias())
                .content(column.getColumnDefinition())
                .remark(column.getRemark())
                .build()).toArray();
        return Result.success(result);
    }

    @RequestMapping(value = "/{model}/keys", method = RequestMethod.GET, produces = "application/json")
    public Result<Object> getKeys(@PathVariable String model) {
        var table = this.service.getNamedTables().get(model);
        if (table == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data resource！");
        }
        var result = this.service.getNamedKeyColumns().get(table.getAlias());
        return Result.success(result);
    }

    @RequestMapping(value = "/{model}/{key}", method = RequestMethod.GET, produces = "application/json")
    public Result<Object> queryByKey(@PathVariable String model, @PathVariable String key) {
        var table = this.service.getNamedTables().get(model);
        if (table == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data resource！");
        }
        // 数据库字段名
        var columns = this.service.getNamedColumns().get(table.getAlias());
        if (columns == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data description！");
        }
        if (Strings.isNullOrEmpty(key)) {
            return Result.fail(HttpCode.BAD_REQUEST, "Condition can not be empty");
        }
        try {
            var sql = Queries.select(columns.values().toArray(new Column[0])).from(table);
            var map = new LinkedHashMap<String, Object>();

            var condition = Condition.emptyCondition();
            for (var entry : this.service.getNamedKeyColumns().get(table.getAlias())) {
                var column = columns.get(entry);
                if (column != null) {
                    condition = condition.and(column.eq(Placeholder.placeholder(column)));
                    map.put(column.getName(), convertColumnData(column.getColumnDefinition().getDefinitionName(), key));
                }
            }
            sql.where(condition);
            return Result.success(this.service.query(sql, map));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

    @RequestMapping(value = "/{model}", method = RequestMethod.PUT, produces = "application/json")
    public Result<Object> create(@PathVariable String model, @RequestBody Map<String, Object> data) {
        var table = this.service.getNamedTables().get(model);
        if (table == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data resource！");
        }
        // 数据库字段名
        var columns = this.service.getNamedColumns().get(table.getAlias());
        if (columns == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data description！");
        }
        if (data == null || data.size() == 0) {
            return Result.fail(HttpCode.BAD_REQUEST, "Put data can not be empty");
        }

        try {
            var sql = Queries.insertInto(table);
            var map = new LinkedHashMap<String, Object>();
            for (var entry : data.entrySet()) {
                var column = columns.get(entry.getKey());
                if (column == null) {
                    continue;
                }
                map.put(column.getName(),
                        convertColumnData(column.getColumnDefinition().getDefinitionName(), entry.getValue()));
                sql.set(column, Placeholder.placeholder(column));
            }

            return Result.success(this.service.update(sql, map));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

    @RequestMapping(value = "/{model}", method = RequestMethod.DELETE, produces = "application/json")
    public Result<Object> delete(@PathVariable String model, @RequestBody Map<String, Object> where) {
        var table = this.service.getNamedTables().get(model);
        if (table == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data resource！");
        }
        // 数据库字段名
        var columns = this.service.getNamedColumns().get(table.getAlias());
        if (columns == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data description！");
        }
        if (where == null || where.size() == 0) {
            return Result.fail(HttpCode.BAD_REQUEST, "Condition can not be empty");
        }
        try {
            var sql = Queries.deleteFrom(table);
            var map = new LinkedHashMap<String, Object>();
            var condition = Condition.emptyCondition();
            for (var entry : where.entrySet()) {
                var column = columns.get(entry.getKey());
                if (column == null) {
                    return Result.fail(HttpCode.BAD_REQUEST, entry.getKey() + "can not be found!");
                }
                map.put(column.getName(),
                        convertColumnData(column.getColumnDefinition().getDefinitionName(), entry.getValue()));
                condition = condition.and(column.eq(Placeholder.placeholder(column)));
            }
            sql.where(condition);
            return Result.success(this.service.update(sql, map));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

    @RequestMapping(value = "/{model}/{key}", method = RequestMethod.DELETE, produces = "application/json")
    public Result<Object> deleteByKey(@PathVariable String model, @PathVariable String key) {
        var table = this.service.getNamedTables().get(model);
        if (table == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data resource！");
        }
        // 数据库字段名
        var columns = this.service.getNamedColumns().get(table.getAlias());
        if (columns == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data description！");
        }
        if (Strings.isNullOrEmpty(key)) {
            return Result.fail(HttpCode.BAD_REQUEST, "Condition can not be empty");
        }
        try {
            var sql = Queries.deleteFrom(table);
            var map = new LinkedHashMap<String, Object>();

            var condition = Condition.emptyCondition();
            for (var entry : this.service.getNamedKeyColumns().get(table.getAlias())) {
                var column = columns.get(entry);
                if (column != null) {
                    condition = condition.and(column.eq(Placeholder.placeholder(column)));
                    map.put(column.getName(),
                            convertColumnData(column.getColumnDefinition().getDefinitionName(), key));
                }
            }
            sql.where(condition);
            return Result.success(this.service.update(sql, map));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

    @RequestMapping(value = "/{model}", method = RequestMethod.POST, produces = "application/json")
    public Result<Object> update(@PathVariable String model, @RequestBody ViewUpdate update) {
        var table = this.service.getNamedTables().get(model);
        if (table == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data resource！");
        }
        // 数据库字段名
        var columns = this.service.getNamedColumns().get(table.getAlias());
        if (columns == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data description！");
        }
        if (update == null) {
            return Result.fail(HttpCode.BAD_REQUEST, "Update data can not be empty");
        }
        var data = update.getData();
        if (data == null || data.size() == 0) {
            return Result.fail(HttpCode.BAD_REQUEST, "Update data can not be empty");
        }
        var where = update.getWhere();
        if (where == null || where.size() == 0) {
            return Result.fail(HttpCode.BAD_REQUEST, "Condition can not be empty");
        }

        try {

            var sql = Queries.update(table);
            var map = new LinkedHashMap<String, Object>();

            for (var entry : data.entrySet()) {
                var column = columns.get(entry.getKey());
                if (column == null) {
                    continue;
                }
                map.put(column.getName(),
                        convertColumnData(column.getColumnDefinition().getDefinitionName(), entry.getValue()));
                sql.set(column, Placeholder.placeholder(column));
            }

            var condition = Condition.emptyCondition();
            for (var entry : where.entrySet()) {
                var column = columns.get(entry.getKey());
                if (column == null) {
                    return Result.fail(HttpCode.BAD_REQUEST, entry.getKey() + "can not be found!");
                }
                map.put(column.getName(),
                        convertColumnData(column.getColumnDefinition().getDefinitionName(), entry.getValue()));
                condition = condition.and(column.eq(Placeholder.placeholder(column)));
            }
            sql.where(condition);

            return Result.success(this.service.update(sql, map));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

    @RequestMapping(value = "/{model}/{key}", method = RequestMethod.POST, produces = "application/json")
    public Result<Object> updateByKey(@PathVariable String model, @RequestBody Map<String, Object> data,
                                      @PathVariable String key) {
        var table = this.service.getNamedTables().get(model);
        if (table == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data resource！");
        }
        // 数据库字段名
        var columns = this.service.getNamedColumns().get(table.getAlias());
        if (columns == null) {
            return Result.fail(HttpCode.NOT_FOUND, "No data description！");
        }
        if (data == null || data.size() == 0) {
            return Result.fail(HttpCode.BAD_REQUEST, "Update data can not be empty");
        }

        if (Strings.isNullOrEmpty(key)) {
            return Result.fail(HttpCode.BAD_REQUEST, "Condition can not be empty");
        }

        try {
            var sql = Queries.update(table);
            var map = new LinkedHashMap<String, Object>();

            for (var entry : data.entrySet()) {
                var column = columns.get(entry.getKey());
                if (column == null) {
                    continue;
                }
                map.put(column.getName(),
                        convertColumnData(column.getColumnDefinition().getDefinitionName(), entry.getValue()));
                sql.set(column, Placeholder.placeholder(column));
            }

            var condition = Condition.emptyCondition();
            for (var entry : this.service.getNamedKeyColumns().get(table.getAlias())) {
                var column = columns.get(entry);
                if (column != null) {
                    condition = condition.and(column.eq(Placeholder.placeholder(column)));
                    map.put(column.getName(),
                            convertColumnData(column.getColumnDefinition().getDefinitionName(), key));
                }
            }
            sql.where(condition);
            return Result.success(this.service.update(sql, map));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST, produces = "application/json")
    public Result<Object> query(@RequestBody String query) {
        try {
            if (Strings.isNullOrEmpty(query)) {
                return Result.fail(HttpCode.BAD_REQUEST, "query can not be empty");
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

    private Object convertColumnData(String type, Object data) {
        type = type.toUpperCase(Locale.ROOT);
        try {
            switch (type) {
                case "DATE":
                    return new SimpleDateFormat("yyyy-MM-dd").parse(data.toString());
                case "TIMESTAMP":
                case "DATETIME":
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.toString());
                case "TIME":
                    return new SimpleDateFormat("HH:mm:ss").parse(data.toString());
                case "BOOLEAN":
                case "BOOL":
                    return Boolean.valueOf(data.toString());

                case "INT":
                case "INT4":
                case "INT8":
                case "LONG":
                    return Long.valueOf(data.toString());
                case "FLOAT":
                case "DOUBLE":
                    return Double.valueOf(data.toString());
                case "CHAR":
                case "BPCHAR":
                case "VARCHAR":
                    return data.toString();
                default:
                    log.warn("没有考虑到的类型：{},暂不处理", type);
                    return data;
            }
        } catch (Exception ex) {

        }
        return null;
    }

}
