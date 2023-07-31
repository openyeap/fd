package cn.zhumingwu.dataswitch.writer.jdbc;

import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.dataswitch.core.model.Record;
import cn.zhumingwu.dataswitch.core.pipeline.Writer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class JdbcWriter implements Writer {
    String driver;
    String url;
    String user;
    String password;
    String sql;
    String table;
    Connection conn;
    Set<String> scheme;

    @Override
    public void init() {
        this.url = this.context().getString("url");
        this.driver = this.context().getString("driver");
        this.user = this.context().getString("username");
        this.password = this.context().getString("password");
        this.sql = this.context().getString("sql");
        this.table = this.context().getString("table");
        try {
            Class.forName(driver);
            this.conn = DriverManager.getConnection(url, user, password);
//                return Result.success();
        } catch (SQLException e) {
//                return Result.error(e);
        } catch (ClassNotFoundException e) {
//                return Result.error(e);
        }
    }


    @Override
    public void execute(Record... records) {
        if (!this.isRunning()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append(this.table);
        sb.append("\n(");
        sb.append(String.join(",", this.scheme));
        sb.append(")\nvalues\n");
        var values = Arrays.stream(records)
                .map(record -> {
                    return "(" + record.toNormalMap().entrySet().stream()
                            .map(m -> m == null ? "null" : "'" + m.toString() + "'")
                            .collect(Collectors.joining(",")) + ")";
                }).collect(Collectors.joining(","));
        sb.append(values);
        sb.append(";");

        try (var stmt = conn.createStatement()) {
            // STEP 4: Execute a query
            log.info("Creating statement...");
            stmt.addBatch(sb.toString());
            stmt.executeBatch();
        } catch (Exception se) {
            log.error("JdbcTargetPipeline.executeBatch", se);
        }
        for (var item : this.nextSteps()) {
            item.execute(records);
        }
    }
}
