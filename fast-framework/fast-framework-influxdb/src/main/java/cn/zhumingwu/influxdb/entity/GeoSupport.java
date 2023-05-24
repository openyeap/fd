package cn.zhumingwu.influxdb.entity;

import com.influxdb.annotations.Column;
import lombok.Data;
import cn.zhumingwu.influxdb.model.Location;

@Data
public abstract class GeoSupport implements InfluxEntity {

    @Column(name = "s2_cell_id", tag = true)
    private Location point;
}
