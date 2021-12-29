package ltd.fdsa.influxdb.service;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.var;
import ltd.fdsa.influxdb.model.Region;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class FluxQuery {
    String flux;

    FluxQuery(String flux) {
        this.flux = flux;
    }

    public static FluxQueryBuilder builder() {
        return new FluxQueryBuilder();
    }

    public static class FluxQueryBuilder {
        private Region region;
        private String[] cells;
        private String[] includes;
        private String measurement;
        private String bucket;
        private Map<String, String> filters;
        private String from;
        private String stop;

    /*
import "experimental/geo"

from(bucket: "bucket")
  |> range(start: -1h, stop: now())
  |> filter(fn: (r) => r["_measurement"] == "devices")
  |> filter(fn: (r) => r["_field"] == "lon" or r["_field"] == "lat" or  r["_field"] == "s2_cell_id")
  |> geo.shapeData(latField: "lat", lonField: "lon", level: 30)
  |> geo.filterRows(
    region: {lat: 10.0, lon: 10.0, radius: 10.0},
    strict: true
  )
  |> yield()
    */

        FluxQueryBuilder() {
        }

        public FluxQueryBuilder filter(String measurement, String bucket, Map<String, String> filters, String from, String stop) {
            this.measurement = measurement;
            this.bucket = bucket;
            this.filters = filters;
            this.from = from;
            this.stop = stop;
            return this;
        }

        public FluxQueryBuilder region(Region region) {
            this.region = region;
            return this;
        }

        public FluxQueryBuilder cells(String... cells) {
            this.cells = cells;
            return this;
        }

        public FluxQueryBuilder includes(String... includes) {
            this.includes = includes;
            return this;
        }

        public FluxQuery build() {
            StringBuilder builder = new StringBuilder();
            //        range(start: -12h, stop: -15m)
            //        range(start: 2018-05-22T23:30:00Z, stop: 2018-05-23T00:00:00Z)
            //        range(start: 1527031800, stop: 1527033600)
            if (Strings.isNullOrEmpty(this.from)) {
                this.from = "-5m";
            }
            if (Strings.isNullOrEmpty(stop)) {
                this.stop = "now()";
            }
            builder.append(MessageFormat.format("from(bucket: \"{0}\")\n  |> range(start: {1}, stop: {2})\n", this.bucket, this.from, this.stop));
            builder.append(MessageFormat.format("  |> filter(fn: (r) => r[\"_measurement\"] == \"{0}\")\n", measurement));
            if (this.includes != null && this.includes.length > 0) {
                var columns = Arrays.stream(this.includes).map(i -> {
                    return MessageFormat.format("r[\"_field\"] == \"{0}\"", i);
                }).collect(Collectors.joining(" or "));
                builder.append(MessageFormat.format("  |> filter(fn: (r) => {0})\n", columns));

            }
            if (this.filters != null && this.filters.size() > 0) {
                for (var entry : filters.entrySet()) {
                    builder.append(MessageFormat.format("  |> filter(fn: (r) => r.{0} == \"{1}\")\n", entry.getKey(), entry.getValue()));
                }
            }
            if (cells != null && cells.length > 0) {
                builder.insert(0, "import \"strings\"\n\n");
                var filters = Arrays.stream(cells).map(cell -> {
//                    return MessageFormat.format("strings.hasPrefix(v: r.s2_cell_id, prefix: \"{0}\")", cell);
                    return MessageFormat.format("r.s2_cell_id =~ /^{0}/", cell);
                }).collect(Collectors.joining(" or "));
                builder.append(MessageFormat.format("  |> filter(fn: (r) => {0})\n", filters));
            }
            if (region != null) {
                // 使用influxdb原生体验功能
                builder.insert(0, "import \"experimental/geo\"\n\n");
                builder.append("  |> geo.shapeData(latField: \"lat\", lonField: \"lon\", level: 30)\n");
                builder.append(MessageFormat.format("  |> geo.filterRows(\n" +
                        "    region: {0},\n" +
                        "    strict: {1}\n" +
                        "  )\n", region.toRegion(), region.getStrict()));
            }
            builder.append("  |> yield()");
            return new FluxQuery(builder.toString());
        }
    }
}
