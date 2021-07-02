package ltd.fdsa.starter.logger.influxdb;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InfluxDbSource {
    private String ip;
    private String port;
    private String database;
    private String user;
    private String password;
    private String create;
}
