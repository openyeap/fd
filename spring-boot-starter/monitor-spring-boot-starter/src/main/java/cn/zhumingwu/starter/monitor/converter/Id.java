package cn.zhumingwu.starter.monitor.converter;

import com.alibaba.druid.support.json.JSONUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.var;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class Id {
    private final String name;
    private final Map<String, String> tags;
    private final Type type;
    private final String description;
    private final Double value;


    public static class Builder {
        private String name;
        private Type type;
        private Double value;

        private String description;
        private Map<String, String> tags;

        public Builder() {

        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder value(Double value) {
            this.value = value;
            return this;
        }

        public Builder value(int value) {
            this.value = Double.valueOf(value);
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder tags(String key, String val) {
            if (this.tags == null) {
                this.tags = new HashMap<>();
            }
            this.tags.putIfAbsent(key, val);
            return this;
        }

        public Id build() {
            return new Id(this.name, this.tags, this.type, this.description, this.value);
        }
    }

    @Override
    public String toString() {
        StringBuilder map = new StringBuilder();
        map.append("# HELP " + this.name + " " + this.description + "\n");
        map.append("# TYPE " + this.name + " " + this.type.name().toLowerCase() + "\n");
        var json = JSONUtils.toJSONString(this.tags);
        for (var key : this.tags.keySet()) {

        }
        map.append(this.name + json + " " + this.value + "\n");
        return map.toString();
    }
}
