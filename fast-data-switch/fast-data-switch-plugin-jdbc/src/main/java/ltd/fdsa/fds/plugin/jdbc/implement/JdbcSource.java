package ltd.fdsa.fds.plugin.jdbc.implement;

import java.sql.*;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.fds.core.AbstractPlugin;
import ltd.fdsa.fds.core.DataSource;
import ltd.fdsa.fds.core.RecordCollector;
import ltd.fdsa.fds.core.config.Configuration; 
import ltd.fdsa.fds.model.Fields;

@Slf4j
public class JdbcSource extends AbstractPlugin implements DataSource {
	String dirver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost/test";
	String user = "username";
	String password = "password";
	String sql = "select 1";

	@Override
	public void prepare(Configuration context) {
		this.url = context.getString("datasource.url", this.url);
		this.dirver = context.getString("datasource.driver", this.dirver);
		this.user = context.getString("datasource.user", this.user);
		this.password = context.getString("datasource.password", this.password);
		this.sql = context.getString("datasource.sql", this.sql);
	}

	@Override
	public Fields getFields() {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(dirver);

			// STEP 3: Open a connection
			System.out.println("Connecting to datasource...");
			conn = DriverManager.getConnection(url, user, password);

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();// 获取键名
			int columnCount = metaData.getColumnCount();// 获取行的数量
			List<String> list = new ArrayList<String>(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				list.add(metaData.getColumnName(i));
			}

			// STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
			return new Fields(list.toArray(new String[0]));
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
		return new Fields();

	}

	@Override
	public void read(RecordCollector collector) {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(dirver);

			// STEP 3: Open a connection
			System.out.println("Connecting to datasource...");
			conn = DriverManager.getConnection(url, user, password);

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();// 获取键名
			int columnCount = metaData.getColumnCount();// 获取行的数量
			if (columnCount < 1) {
				rs.close();
				return;
			}
			// STEP 5: Extract data from result set
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(metaData.getColumnName(i), rs.getObject(i));// 获取键名及值
				}
				collector.send(rowData);
			}
			// STEP 6: Clean-up environment
			rs.close();
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

	@Override
	public List<Configuration> splitTask() {
		// TODO Auto-generated method stub
		return null;
	}

}
