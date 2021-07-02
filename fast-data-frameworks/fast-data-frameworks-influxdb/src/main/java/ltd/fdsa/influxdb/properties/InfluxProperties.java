package ltd.fdsa.influxdb.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = InfluxProperties.PREFIX)
@ToString
public class InfluxProperties {
    public static final String PREFIX = "project.influx";
    /**
     * 数据保存策略.
     */
    private String retentionPolicy = "autogen";

    /**
     * default influx url.
     */
    private String url;
    /**
     * default influx database.
     */
    private String org = "org";
    /**
     * default influx bucket.
     */
    private String bucket = "bucket";
    /**
     * default influx username.
     */
    private String username;
    /**
     * default influx token. if has username the token is password with username
     */
    private char[] token;
}
