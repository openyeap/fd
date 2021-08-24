package ltd.fdsa.switcher.core.model;

public class Schema {
    private byte size;

    private String name;

    private byte type;

    private boolean isNull;

    Schema(byte size, String name, byte type, boolean isNull) {
        this.size = size;
        this.name = name;
        this.type = type;
        this.isNull = isNull;
    }

    public static SchemaBuilder builder() {
        return new SchemaBuilder();
    }

    public byte getSize() {
        return this.size;
    }

    public String getName() {
        return this.name;
    }

    public byte getType() {
        return this.type;
    }

    public boolean isNull() {
        return this.isNull;
    }

    public void setSize(byte size) {
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setNull(boolean isNull) {
        this.isNull = isNull;
    } 
    public static class SchemaBuilder {
        private byte size;
        private String name;
        private byte type;
        private boolean isNull;

        SchemaBuilder() {
        }

        public SchemaBuilder size(byte size) {
            this.size = size;
            return this;
        }

        public SchemaBuilder name(String name) {
            this.name = name;
            return this;
        }

        public SchemaBuilder type(byte type) {
            this.type = type;
            return this;
        }

        public SchemaBuilder isNull(boolean isNull) {
            this.isNull = isNull;
            return this;
        }

        public Schema build() {
            return new Schema(size, name, type, isNull);
        }

        public String toString() {
            return "Schema.SchemaBuilder(size=" + this.size + ", name=" + this.name + ", type=" + this.type + ", isNull=" + this.isNull + ")";
        }
    }
}