package cn.zhumingwu.influxdb.model;

import lombok.Data;

@Data
public class Circle implements Region {

    private Float lat;

    private Float lon;

    private Float radius;

    public String toString() {
        return "{lat:" + this.getLat() + ", lon:" + this.getLon() + ", radius:" + this.getRadius() + "}";
    }

    @Override
    public String toRegion() {
        return this.toString();
    }

}
