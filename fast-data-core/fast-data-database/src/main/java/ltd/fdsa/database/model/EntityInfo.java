package ltd.fdsa.database.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EntityInfo {
    String name;
    Map<String, FieldInfo> fields;
    FieldInfo id;

    EntityInfo(String name, FieldInfo id, Map<String, FieldInfo> fields) {
        this.name = name;
        this.fields = fields;
        this.id = id;
    }



    public static EntityInfoBuilder builder() {
        return new EntityInfoBuilder();
    }

    public static class EntityInfoBuilder {
        private String name;
        private Map<String, FieldInfo> fields;
        private FieldInfo id;

        EntityInfoBuilder() {
            this.fields = new HashMap<>();

        }

        public EntityInfoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EntityInfoBuilder field(String key, FieldInfo fields) {
            this.fields.put(key, fields);
            return this;
        }

        public EntityInfoBuilder id(FieldInfo fields) {
            this.id = fields;
            return this;
        }


        public EntityInfo build() {
            return new EntityInfo(name, id, fields);
        }
    }
}
