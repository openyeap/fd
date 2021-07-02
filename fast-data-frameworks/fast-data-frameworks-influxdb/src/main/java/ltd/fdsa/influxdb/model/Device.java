package ltd.fdsa.influxdb.model;


import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Builder;
import lombok.Data;
import ltd.fdsa.influxdb.entity.InfluxEntity;

import java.time.Instant;

@Builder
@Data
@Measurement(name = "devices")
public class Device implements InfluxEntity {
    // 时间戳均是以UTC时保存,在保存以及提取过程中需要注意时区转换
    @Column(name = "time", timestamp = true)
    private Instant time;
    // 注解中添加tag = true,表示当前字段内容为tag内容
    @Column(name = "type", tag = true)
    private String type;
    @Column(name = "type")
    private String name;
    @Column(name = "point")
    private Location point;
    @Column(name = "area")
    private Polygon area;
}