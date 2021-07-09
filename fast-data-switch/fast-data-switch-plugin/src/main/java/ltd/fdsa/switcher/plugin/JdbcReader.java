package ltd.fdsa.switcher.plugin;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.switcher.core.pipeline.Reader;
import ltd.fdsa.switcher.core.config.Configuration;
import ltd.fdsa.switcher.core.pipeline.impl.AbstractPipeline;
import ltd.fdsa.web.enums.HttpCode;
import ltd.fdsa.web.view.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class JdbcReader extends AbstractPipeline implements Reader {
    String driver;
    String url;
    String user;
    String password;
    String sql;
    Map<String, String> scheme;
    Connection conn;


    @Override
    public Result<String> init(Configuration configuration) {
        var result = super.init(configuration);
        if (result.getCode() == 200) {
            try {
                this.url = config.getString("url");
                this.driver = config.getString("driver");
                this.user = config.getString("username");
                this.password = config.getString("password");
                this.sql = config.getString("sql");
                this.scheme = new HashMap<>(64);
                Class.forName(driver);
                this.conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                return Result.error(e);
            } catch (ClassNotFoundException e) {
                return Result.error(e);
            }
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(String.format("select * from (%s) a where 1=2", sql));
                ResultSetMetaData metaData = rs.getMetaData(); // 获取键名
                int columnCount = metaData.getColumnCount(); // 获取行的数量
                for (int i = 1; i <= columnCount; i++) {
                    scheme.put(metaData.getColumnName(i), metaData.getColumnTypeName(i));
                }
                return Result.success();
            } catch (SQLException e) {
                log.error("JdbcSourcePipeline.getColumn", e);
                return Result.error(e);
            }
        }
        return Result.fail(HttpCode.EXPECTATION_FAILED, result.getMessage());
    }

    @Override
    public Map<String, String> scheme() {
        return this.scheme;
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            while (this.isRunning()) {
                List<Map<String, Object>> list = new ArrayList<>();
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        Map<String, Object> rowData = new HashMap<String, Object>(this.scheme.size());
                        for (var key : this.scheme.keySet()) {
                            rowData.put(key, rs.getObject(key)); // 获取键名及值
                        }
                        list.add(rowData);
                    }
                    // STEP 6: Clean-up environment
                    rs.close();
                } catch (SQLException e) {
                    log.error("JdbcSourcePipeline.process", e);
                }
                super.collect(list.toArray(new HashMap[list.size()]));
            }
        }

    }

}
