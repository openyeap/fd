package ltd.fdsa.starter.logger.influxdb;

import lombok.Data;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

@Data
@ToString
public class InfluxDbSerie {

    private String id;
    private String name;
    private String timeUnit;

    public TimeUnit getRawTimeUnit() {
        return TimeUnit.valueOf(timeUnit);
    }
}
