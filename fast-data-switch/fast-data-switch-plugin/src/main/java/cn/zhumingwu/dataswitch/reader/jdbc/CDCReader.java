package cn.zhumingwu.dataswitch.reader.jdbc;

import io.debezium.engine.format.Json;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.dataswitch.core.model.Record;
import cn.zhumingwu.dataswitch.core.pipeline.Reader;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class CDCReader implements Reader {
    String driver;
    String url;
    String user;
    String password;
    String sql;
    Connection conn;

    @Override
    public void init() {
        try {
            this.url = this.context().getString("url");
            this.driver = this.context().getString("driver");
            this.user = this.context().getString("username");
            this.password = this.context().getString("password");
            this.sql = this.context().getString("sql");

            // Define the configuration for the Debezium Engine with MySQL connector...
            final Properties props = new Properties();
            props.setProperty("name", "engine");
            props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
            props.setProperty("offset.storage.file.filename", "/tmp/offsets.dat");
            props.setProperty("offset.flush.interval.ms", "60000");
            /* begin connector properties */
            props.setProperty("database.hostname", "10.168.4.49");
            props.setProperty("database.port", "5321");
            props.setProperty("database.user", "postgres");
            props.setProperty("database.password", "postgres");
            props.setProperty("database.server.id", "85744");
            props.setProperty("database.server.name", "my-app-connector");// 可以任意修改
            props.setProperty("database.serverTimezone", "UTC"); // 时区，建议使用UTC
            props.setProperty("database.whitelist", "dev");  //数据库名
            props.setProperty("table.whitelist", "dev.t_person"); // 库.表名
            props.setProperty("database.history", "io.debezium.relational.history.FileDatabaseHistory");
            props.setProperty("database.history.file.filename", "/path/to/storage/dbhistory.dat");
//            DebeziumEngine<ChangeEvent<String, String>> engine  =     DebeziumEngine.create(Json.class)
//                    .using(props)
//                    .notifying((record) -> {
//                        System.out.println(record);
//                    })
//                    .build();
// Create the engine with this configuration ...
            try (DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                    .using(props)
                    .notifying(record -> {
                        System.out.println(record);

                    }).build()
            ) {
                // Run the engine asynchronously ...
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(engine);

                // Do something else or wait for a signal or an event
            }
// Engine is stopped when the main code is finished
        } catch (IOException e) {
//                return Result.error(e);
        }
    }

    @Override
    public void execute(Record... records) {
        if (this.isRunning()) {
            for (var item : this.nextSteps()) {
                item.execute(records);
            }
        }
    }


    @Override
    public void start() {
        while (this.isRunning()) {
            List<Record> list = new ArrayList<>();
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
              var ss =   rs.getMetaData();
                ss.getColumnTypeName(1);
                while (rs.next()) {
//                    Record record = new Record();
//                    record.add(new Column(key, rs.getObject(key) ));
//                    list.add(record);
                }
                rs.close();
            } catch (SQLException e) {
                log.error("JdbcSourcePipeline.process", e);
            }
            this.execute(list.toArray(new Record[0]));
        }
    }
}
