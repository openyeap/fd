package ltd.fdsa.starter.jdbc.service;

import io.swagger.models.*;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import lombok.var;
import ltd.fdsa.database.entity.BaseEntity;
import ltd.fdsa.database.service.BaseService;
import ltd.fdsa.starter.jdbc.mappers.RowDataMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JdbcService extends BaseService<BaseEntity<Integer>, Integer> {


    public Object query(String sql, Object[] args) {
        var jdbcTemplate = new JdbcTemplate(this.writer);
        return jdbcTemplate.query(sql, args, new RowDataMapper());
    }

    public Object create(String sql, String[] data) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var jdbcTemplate = new JdbcTemplate(this.writer);
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection)
                            throws SQLException {

                        PreparedStatement ps =
                                jdbcTemplate
                                        .getDataSource()
                                        .getConnection()
                                        .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        for (int j = 0; j < data.length; j++) {
                            ps.setString(j + 1, data[j]);
                        }
                        return ps;
                    }
                },
                keyHolder);
        return keyHolder.getKeyList().get(0);
    }


    public int update(String sql, List<Object> list) {
        if (list == null || list.size() == 0) {
            var jdbcTemplate = new JdbcTemplate(this.writer);
            return jdbcTemplate.update(sql);
        }
        var jdbcTemplate = new JdbcTemplate(this.writer);
        return jdbcTemplate.update(sql, list.toArray());
    }

    public Swagger createTablesController(String host, String basePath) {

        Swagger swagger = new Swagger();
        {
            Info info = new Info();
            info.title("title");
            info.description("description");
            Contact contact = new Contact();
            contact.email("email");
            contact.name("name");
            contact.url("url");
            info.contact(contact);
            swagger.info(info);
        }
        swagger.host(host);
        swagger.basePath(basePath);
        Response response = new Response();
        response.description("成功");
        response.example("", "");
        for (var tag : getTagsFromDatabase()) {
            // api 接口列表
            swagger.tag(tag);

            // api 路径
            Path path = new Path();
            Operation getOneOperation = new Operation();
            getOneOperation.tag(tag.getName());
            getOneOperation.summary("获取" + tag.getName() + "清单");
            getOneOperation.description("" + tag.getDescription());
            getOneOperation.operationId("getOne");
            getOneOperation.consumes("*/*");
            getOneOperation.produces("*/*");
            getOneOperation.parameter(getParameter("id", "唯一编号", true, ""));

            getOneOperation.response(200, response);
            path.get(getOneOperation);
            Operation getOperation = new Operation();
            getOperation.tag(tag.getName());
            getOperation.summary("获取" + tag.getName() + "清单");
            getOperation.description("" + tag.getDescription());
            getOperation.operationId("test");
            getOperation.consumes("*/*");
            getOperation.produces("*/*");
            getOperation.parameter(getParameter("select", "列名逗号隔开，不传默认为*", false, "*"));
            getOperation.parameter(getParameter("where", "查询条件"));
            getOperation.parameter(getParameter("order", "排序规则", false, "desc"));
            getOperation.parameter(getParameter("group", "分组规则"));
            getOperation.parameter(getParameter("page", "分页-页", false, "0", "integer", "int32"));
            getOperation.parameter(getParameter("size", "分页-页", false, "20", "integer", "int32"));

            getOperation.response(200, response);
            path.get(getOperation);

            Operation postOperation = new Operation();
            postOperation.tag(tag.getName());
            postOperation.summary("获取" + tag.getName() + "清单");
            postOperation.description("" + tag.getDescription());
            postOperation.operationId("test");
            postOperation.consumes("application/json");
            postOperation.produces("*/*");
            postOperation.parameter(getParameter("select", "列名逗号隔开，不传默认为*", false, "*"));
            postOperation.parameter(getParameter("where", "查询条件"));
            postOperation.parameter(getParameter("order", "排序规则", false, "desc"));
            postOperation.parameter(getParameter("group", "分组规则"));
            postOperation.parameter(getParameter("page", "分页-页", false, "0", "integer", "int32"));
            postOperation.parameter(getParameter("size", "分页-页", false, "20", "integer", "int32"));

            postOperation.response(200, response);
            path.post(postOperation);

            swagger.path("/v2/api/" + tag.getName(), path);
        }
        return swagger;
    }


    private Parameter pathTableInfo(String tableName) {
        var parameter = new PathParameter();
        parameter.name("entity");
        parameter.description("实体资源名称");
        parameter.required(true);
        parameter.setDefaultValue("");
        parameter.type("string");
        return parameter;

//        parameters.add(putParameter("data", "body", "data", true));

    }

    private Parameter getBodyParameter(Object model) {
        var parameter = new BodyParameter();
        parameter.name("data");
        parameter.description("");
        Model m = new ArrayModel();
        m.setExample(model);
        parameter.schema(m);
        return parameter;
    }

    private Parameter getParameter(String name, String description, boolean required, String defaultValue) {
        return getParameter(name, description, required, defaultValue, "", "");
    }

    private Parameter getParameter(String name, String description, boolean required) {
        return getParameter(name, description, required, "", "", "");
    }

    private Parameter getParameter(String name, String description) {
        return getParameter(name, description, false, "", "", "");
    }

    private Parameter getParameter(String name) {
        return getParameter(name, name, false, "", "", "");
    }

    private Parameter getParameter(String name, String description, boolean required, String defaultValue, String type, String format) {

        var parameter = new PathParameter();
        parameter.name(name);
        parameter.description(description);
        parameter.required(required);
        parameter.setDefaultValue(defaultValue);
        parameter.type("integer");
        parameter.format("int32");
        return parameter;
    }

    List<Tag> getTagsFromDatabase() {

        List<Tag> result = new ArrayList<>();
        for (var table : this.listAllTables("", "")) {
            Tag tag = new Tag();
            tag.name(table.getName());
            tag.description(table.getDescription());
            result.add(tag);
        }
        return result;
    }
}
