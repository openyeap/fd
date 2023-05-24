package cn.zhumingwu.influxdb.test.model;

import cn.zhumingwu.influxdb.entity.GeoSupport;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
@Measurement(name = "Person")
public class Person extends GeoSupport {
    // 时间戳均是以UTC时保存,在保存以及提取过程中需要注意时区转换
    @Column(name = "time", timestamp = true)
    private Instant time;
    // 注解中添加tag = true,表示当前字段内容为tag内容
    @Column(name = "type", tag = true)
    private String type;
    @Column(name = "name")
    private String name;

}