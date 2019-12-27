package ltd.fdsa.fdsql.web.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData; 
import java.sql.ResultSet; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
			rs = meta.getTables(null, schemaPattern, null, new String[] { "TABLE" });
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
			rs = meta.getTables(null, schemaPattern, null, new String[] { "TABLE" });
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
			rs = meta.getColumns(null,schemaPattern , tableName, null);
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

}
