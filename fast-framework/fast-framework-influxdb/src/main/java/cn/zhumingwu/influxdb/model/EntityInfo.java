package cn.zhumingwu.influxdb.model;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class EntityInfo {
    private String name;
    private Map<String, Field> fields;
    private Map<String, Field> tags;

    EntityInfo(String name, Map<String, Field> fields, Map<String, Field> tags) {
        this.name = name;
        this.fields = fields;
        this.tags = tags;
    }

    public static EntityInfoBuilder builder() {
        return new EntityInfoBuilder();
    }

    public static class EntityInfoBuilder {
        private String name;
        private final Map<String, Field> fields = new LinkedHashMap<>();
        private final Map<String, Field> tags = new LinkedHashMap<>();

        EntityInfoBuilder() {
        }

        public EntityInfoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EntityInfoBuilder field(String key, Field field) {
            this.fields.put(key, field);
            return this;
        }

        public EntityInfoBuilder tag(String key, Field field) {
            this.tags.put(key, field);
            return this;
        }

        public EntityInfo build() {
            return new EntityInfo(name, fields, tags);
        }

    }
}
