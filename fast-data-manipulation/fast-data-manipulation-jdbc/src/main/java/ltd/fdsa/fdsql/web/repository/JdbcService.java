package ltd.fdsa.fdsql.web.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import ltd.fdsa.fdsql.web.config.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class JdbcService {

    @Autowired
    Connection conn;


    public JSONObject listAllTablesFields() {

        if (conn == null) {
            return null;
        }
        JSONObject result = new JSONObject(true);
        JSONObject paths = new JSONObject(true);
        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            DatabaseMetaData meta = conn.getMetaData();
            // 目录名称; 数据库名; 表名称; 表类型;
            String schemaPattern = "public";
            rs = meta.getTables(null, schemaPattern, null, new String[]{"TABLE"});
            while (rs.next()) {
                JSONObject insertResult = new JSONObject(true);
                JSONObject updateResult = new JSONObject(true);
                JSONObject deleteResult = new JSONObject(true);
                JSONObject selectResult = new JSONObject(true);
                JSONObject post = new JSONObject(true);
                JSONArray parameters = new JSONArray();
                JSONObject responses = JSON.parseObject(
                        "{ \"200\": { \"description\": \"OK\" }, \"201\": { \"description\": \"Created\" }, \"401\": { \"description\": \"Unauthorized\" }, \"403\": { \"description\": \"Forbidden\" }, \"404\": { \"description\": \"Not Found\" } }");
                String tableName = rs.getString("TABLE_NAME");
                rs2 = meta.getColumns(null, schemaPattern, tableName, null);
                while (rs2.next()) {
                    JSONObject fields = new JSONObject(true);
                    fields.put("name", rs2.getString("COLUMN_NAME"));
                    fields.put("in", "query");
                    fields.put("description", rs2.getString("COLUMN_NAME"));
                    fields.put("required", false);
                    fields.put("type", rs2.getString("TYPE_NAME"));
                    parameters.add(fields);
                }
                insertResult.put("tags", JSONArray.parse("[\"jdbc-controller\"]"));
                insertResult.put("operationId",
                        "/insert" + tableName.substring(0, 1).toUpperCase() + tableName.substring(1));
                insertResult.put("consumes", JSONArray.parse("[\"application/json\"]"));
                insertResult.put("produces", JSONArray.parse("[\"*/*\"]"));
                insertResult.put("parameters", parameters);
                insertResult.put("responses", responses);
                post.put("post", insertResult);
                paths.put("/insert_" + tableName, post);
            }
            result.put("swagger", "2.0");
            result.put("info", JSON.parseObject(
                    "{\"description\": \"CURD.\",\"version\": \"1.0\",\"title\": \"CURD RESTful APIs\",\"contact\": {\"name\": \"新智道枢\"}}"));
            result.put("host", "localhost:8096");
            result.put("basePath", "/airport-api");
            result.put("tags",
                    JSONArray.parse("[{\"name\": \"jdbc-controller\",\"description\": \"Jdbc Controller\"}]"));
            result.put("paths", paths);
        } catch (Exception e) {

        } finally {
//			close(conn, null, rs);
//			close(conn, null, rs2);
        }
        return result;
    }

    public List<String> listAllTables() {

        if (conn == null) {
            return null;
        }
        List<String> result = new ArrayList<>();
        ResultSet rs = null;
        try {
            conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            DatabaseMetaData meta = conn.getMetaData();
            // 目录名称; 数据库名; 表名称; 表类型;
            String schemaPattern = "public";
            rs = meta.getTables(null, schemaPattern, null, new String[]{"TABLE"});
            while (rs.next()) {
                result.add(rs.getString("TABLE_NAME"));
            }
        } catch (Exception e) {

        } finally {
//			close(conn, null, rs);
        }
        return result;
    }

    public List<String> listAllFields(String tableName) {

        if (conn == null) {
            return null;
        }
        List<String> result = new ArrayList<>();
        ResultSet rs = null;
        try {
            DatabaseMetaData meta = conn.getMetaData();
            String schemaPattern = "public";
            rs = meta.getColumns(null, schemaPattern, tableName, null);
            while (rs.next()) {
                result.add(rs.getString("COLUMN_NAME") + ":" + rs.getString("TYPE_NAME"));
            }
        } catch (Exception e) {

        } finally {
//			close(conn, null, rs);
        }
        return result;
    }

    public List<String> listAllTablesFields(String... fields) {
        // 如果字段为空, 则返回null
        if (fields == null || fields.length == 0) {
            return null;
        }
        // 如果表结构(视图)为空, 则返回null
        List<String> tvs = listAllTables();
        if (tvs == null || tvs.size() == 0) {
            return null;
        }

        List<String> result = new CopyOnWriteArrayList<>();
        // 并发筛选包含特定字段的表结构
        tvs.parallelStream().forEach(tv -> {
            List<String> fieldsList = listAllFields(tv);
            if (fieldsList != null && fieldsList.size() > 0) {
                if (fieldsList.containsAll(Arrays.asList(fields))) {
                    result.add(tv);
                }
            }
        });

        return result;
    }

    @Autowired
    private SwaggerProperties swaggerProperties;

    public JSONObject createTablesController(String host, String basePath) {
        if (conn == null) {
            return null;
        }
        JSONObject json = new JSONObject(true);
        json.put("swagger", "2.0"); // swagger版本
        JSONObject jsonInfo = new JSONObject(); //api描述
        jsonInfo.put("description", swaggerProperties.getDescription());
        jsonInfo.put("version", swaggerProperties.getVersion());
        jsonInfo.put("title", swaggerProperties.getTitle());
        jsonInfo.put("contact", swaggerProperties.getContact());
        jsonInfo.put("license", swaggerProperties.getLicense());
        json.put("info", jsonInfo);
        json.put("host", host); // 127.0.0.1:8080
        json.put("basePath", "/" + basePath); // /demo
        try {
            //api 接口列表
            JSONArray tags = new JSONArray();
            ResultSet rsTable = getTables();
            while (rsTable.next()) {
                JSONObject tag = new JSONObject();
                String tableName = rsTable.getString("TABLE_NAME");
                tag.put("name", tableName);
                tag.put("description", tableName + "Controller");
                tags.add(tag);
            }
            json.put("tags", tags);
            //api 路径
            rsTable.beforeFirst();
            JSONObject paths = new JSONObject();
            while (rsTable.next()) {
                String tableName = rsTable.getString("TABLE_NAME");
                //查列表
                Map<String, Object> map = new HashMap<>();
                map.put("get", getTableList(tableName));
                //新增
                map.put("put", putTableInfo(tableName));
                //修改
                map.put("post", postTableInfo(tableName));
                //删除
                map.put("delete", deleteTableInfo(tableName));

                //加入paths路径中
                paths.put("/api/" + tableName, map);
            }
            json.put("paths", paths);
        } catch (Exception e) {
            json.put("tags", new JSONArray());
            json.put("paths", new JSONObject());
        }
        return json;
    }

    /**
     * 获取列表   /user  get
     *
     * @param tableName
     * @return
     */
    private JSONObject getTableList(String tableName) {
        JSONObject json = new JSONObject(true);
        //增加tags
        List<Object> list = new ArrayList<>();
        list.add(tableName);
        JSONArray tags = new JSONArray(list);
        json.put("tags", tags);
        json.put("summary", "get" + tableName + "List");
        json.put("operationId", "get" + tableName + "ListUsingGET");

        //增加produces
        list = new ArrayList<>();
        list.add("*/*");
        JSONArray produces = new JSONArray(list);
        json.put("produces", produces);
        //增加参数
        JSONArray parameters = new JSONArray();
        parameters.add(getParameter("select", "query", "列名逗号隔开，不传默认为*", false, "*"));
        parameters.add(getParameter("query", "query", "查询条件", false, ""));
        parameters.add(getParameter("order", "query", "排序规则", false, ""));
        parameters.add(getParameter("group", "query", "分组规则", false, ""));
        parameters.add(getParameter("having", "query", "分组条件", false, ""));
        parameters.add(getParameter("page", "query", "分页-页", false, 0, "integer", "int32"));
        parameters.add(getParameter("size", "query", "分页-行数", false, 20, "integer", "int32"));
        json.put("parameters", parameters);

        //增加response
        JSONObject response = new JSONObject();
        response.put("200", get200());
        response.put("401", get401());
        response.put("403", get403());
        response.put("404", get404());
        json.put("responses", response);
        json.put("deprecated", false);
        return json;
    }

    /**
     * 新增  /user  put
     * 暂时未增加传入参数与数据库字段校验，后续考虑！~
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    private JSONObject putTableInfo(String tableName) {
        JSONObject json = new JSONObject(true);
        //增加tags
        List<Object> list = new ArrayList<>();
        list.add(tableName);
        JSONArray tags = new JSONArray(list);
        json.put("tags", tags);
        json.put("summary", "add" + tableName);
        json.put("operationId", "add" + tableName + "UsingPUT");
        //增加consumes
        list = new ArrayList<>();
        list.add("application/json");
        JSONArray consumes = new JSONArray(list);
        json.put("consumes", consumes);
        //增加produces
        list = new ArrayList<>();
        list.add("*/*");
        JSONArray produces = new JSONArray(list);
        json.put("produces", produces);
        //增加参数
        JSONArray parameters = new JSONArray();
        parameters.add(putParameter("data", "body", "data", true));
        json.put("parameters", parameters);
        //增加response
        JSONObject response = new JSONObject();
        response.put("200", get200());
        response.put("201", get201());
        response.put("401", get401());
        response.put("403", get403());
        response.put("404", get404());
        json.put("responses", response);
        json.put("deprecated", false);
        return json;
    }

    private JSONObject postTableInfo(String tableName) {
        JSONObject json = new JSONObject(true);
        //增加tags
        List<Object> list = new ArrayList<>();
        list.add(tableName);
        JSONArray tags = new JSONArray(list);
        json.put("tags", tags);
        json.put("summary", "upd" + tableName);
        json.put("operationId", "upd" + tableName + "UsingPost");
        //增加consumes
        list = new ArrayList<>();
        list.add("application/json");
        JSONArray consumes = new JSONArray(list);
        json.put("consumes", consumes);
        //增加produces
        list = new ArrayList<>();
        list.add("*/*");
        JSONArray produces = new JSONArray(list);
        json.put("produces", produces);
        //增加参数
        JSONArray parameters = new JSONArray();
        parameters.add(getParameter("table", "path", "table", true, null, "string", null));
        parameters.add(getParameter("where", "query", "where", false, null, "string", null));
        parameters.add(putParameter("data", "body", "data", true));
        json.put("parameters", parameters);
        //增加response
        JSONObject response = new JSONObject();
        response.put("200", get200());
        response.put("201", get201());
        response.put("401", get401());
        response.put("403", get403());
        response.put("404", get404());
        json.put("responses", response);
        json.put("deprecated", false);
        return json;
    }

    private JSONObject deleteTableInfo(String tableName) {
        JSONObject json = new JSONObject(true);
        //增加tags
        List<Object> list = new ArrayList<>();
        list.add(tableName);
        JSONArray tags = new JSONArray(list);
        json.put("tags", tags);
        json.put("summary", "del" + tableName);
        json.put("operationId", "del" + tableName + "UsingDELETE");
        //增加produces
        list = new ArrayList<>();
        list.add("*/*");
        JSONArray produces = new JSONArray(list);
        json.put("produces", produces);
        //增加参数
        JSONArray parameters = new JSONArray();
        parameters.add(getParameter("table", "path", "table", true, null, "string", null));
        parameters.add(getParameter("where", "query", "where", false, null, "string", null));
        json.put("parameters", parameters);
        //增加response
        JSONObject response = new JSONObject();
        response.put("200", get200());
        response.put("204", get204());
        response.put("401", get401());
        response.put("403", get403());
        json.put("responses", response);
        json.put("deprecated", false);
        return json;
    }

    private JSONObject getParameter(String name, String in, String description, boolean required, Object defVal) {
        return getParameter(name, in, description, required, defVal, "string", null);
    }

    private JSONObject getParameter(String name, String in, String description, boolean required, Object defVal, String type, String format) {
        JSONObject parameter = new JSONObject(true);
        parameter.put("name", name); // 传入参数名字
        parameter.put("in", in); //传入模式
        parameter.put("description", description); //描述
        parameter.put("required", required); //是否必传
        if (defVal != null && !"".equals(defVal)) {
            parameter.put("default", defVal);
        }
        parameter.put("type", type); //数据类型
        if (format != null && !"".equals(format)) {
            parameter.put("format", format); //格式化
        }
        return parameter;
    }

    private JSONObject putParameter(String name, String in, String description, boolean required) {
        JSONObject json = getParameter(name, in, description, required, null);
        Map<String, String> map = new HashMap<>();
        map.put("type", "object");
        json.put("schema", map);
        return json;
    }

    private JSONObject get200() {
        JSONObject json = new JSONObject();
        json.put("description", "OK");
//        JSONObject jsonSchema = new JSONObject();
//        jsonSchema.put("type", "string");
//        json.put("schema", jsonSchema);
        return json;
    }

    private JSONObject get201() {
        JSONObject json = new JSONObject();
        json.put("description", "Created");
        return json;
    }

    private JSONObject get204() {
        JSONObject json = new JSONObject();
        json.put("description", "No Content");
        return json;
    }

    private JSONObject get401() {
        JSONObject json = new JSONObject();
        json.put("description", "Unauthorized");
        return json;
    }

    private JSONObject get403() {
        JSONObject json = new JSONObject();
        json.put("description", "Forbidden");
        return json;
    }

    private JSONObject get404() {
        JSONObject json = new JSONObject();
        json.put("description", "Not Found");
        return json;
    }

    public ResultSet getTables() throws SQLException {
        if (conn == null) {
            return null;
        }
        ResultSet rs = null;
        conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        DatabaseMetaData meta = conn.getMetaData();
        // 目录名称; 数据库名; 表名称; 表类型;
        String schemaPattern = "public";
        rs = meta.getTables(null, schemaPattern, null, new String[]{"TABLE"});
        return rs;
    }

    public ResultSet getColumns(String tableName) throws SQLException {
        if (conn == null) {
            return null;
        }
        ResultSet rs = null;
        conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        DatabaseMetaData meta = conn.getMetaData();
        // 目录名称; 数据库名; 表名称; 表类型;
        String schemaPattern = "public";
        rs = meta.getColumns(null, schemaPattern, tableName, null);
        return rs;
    }

}
