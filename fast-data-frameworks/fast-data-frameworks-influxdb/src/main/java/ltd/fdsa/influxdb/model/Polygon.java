package ltd.fdsa.influxdb.model;

import lombok.Data;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

@Data
public class Polygon implements Region {

    private Location[] points;

    public String toString() {
        return "{points:[" + Arrays.stream(this.getPoints()).map(m -> MessageFormat.format("{lat:{0}, lon:{1}}", m.getLat(), m.getLon())).collect(Collectors.joining(",")) + "]";
    }

    @Override
    public String toRegion() {
        return this.toString();
    }

}
