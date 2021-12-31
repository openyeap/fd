package ltd.fdsa.influxdb.model;

import lombok.Data;

@Data
public class Box implements Region {

    private Float minLat;

    private Float maxLat;

    private Float minLon;

    private Float maxLon;

    public String toString() {
        return "{minLat:" + this.getMinLat() + ", maxLat:" + this.getMaxLat() + ", minLon:" + this.getMinLon() + ", maxLon:" + this.getMaxLon() + "}";
    }

    @Override
    public String toRegion() {
        return this.toString();
    }
}
