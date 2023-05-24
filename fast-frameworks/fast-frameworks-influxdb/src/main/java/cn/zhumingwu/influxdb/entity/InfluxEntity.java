package cn.zhumingwu.influxdb.entity;

import java.time.Instant;

public interface InfluxEntity {
    Instant getTime();
    void setTime(Instant time);
}

