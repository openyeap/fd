package ltd.fdsa.fds.plugin.jdbc.implement;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.fds.core.AbstractPlugin;
import ltd.fdsa.fds.core.DataPipeLine;
import ltd.fdsa.fds.core.DataTarget;
import ltd.fdsa.fds.core.config.Configuration;

@Slf4j
public class JDBCTarget extends AbstractPlugin implements DataTarget {
	String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	String DB_URL = "jdbc:mysql://localhost/test";
	String USER = "username";
	String PASS = "password";
	String TABLE = "user";

	@Override
	public void prepare(Configuration context) {
		this.JDBC_DRIVER = context.getString("driver", JDBC_DRIVER);
		this.DB_URL = context.getString("url");
		this.USER = context.getString("user");
		this.PASS = context.getString("password");
		this.TABLE = context.getString("table");
	}

	@Override
	public void write(Map<String, Object> data) {

		StringBuffer sb = new StringBuffer();
		Map<String, Object> row = data;
		{
			String values = row.values().stream().map(m -> {
				return m.toString();
			}).collect(Collectors.joining(","));
			sb.append("insert into ");
			sb.append(this.TABLE);
			sb.append("(");
			sb.append(String.join(",", row.keySet()));
			sb.append(") values (");
			sb.append(values);
			sb.append(");");
		}
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			stmt.addBatch(sb.toString());
			stmt.executeBatch();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			log.debug(se.getLocalizedMessage());
		} catch (Exception e) {
			log.debug(e.getLocalizedMessage());
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
				log.debug(se2.getLocalizedMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				log.debug(se.getLocalizedMessage());
			}
		}
		System.out.println("Goodbye!");
	}

}
