package cn.zhumingwu.base.service;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.ToString;
import cn.zhumingwu.base.util.NamingUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ToString
public class ServiceInfo {

    private String schema = "http";

    private String ip = "";

    private int port;

    private String name;

    private String id;

    private Map<String, String> metadata;

    public String getUrl() {
        return NamingUtils.format("{}://{}:{}", this.schema, this.ip, this.port);
    }

    public ServiceInfo() {
        // this.schema = schema;
        // this.ip = ip;
        // this.port = port;
        // this.name = name;
        // this.id = id;
        // this.metadata = metadata;
    }

    ServiceInfo(String schema, String ip, int port, String name, String id, Map<String, String> metadata) {
        this.schema = schema;
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.id = id;
        this.metadata = metadata;
    }

    public static ServiceInfoBuilder builder() {
        return new ServiceInfoBuilder();
    }

    public static class ServiceInfoBuilder {
        private String schema;
        private String ip;
        private int port;
        private String name;
        private String id;
        private Map<String, String> metadata;

        ServiceInfoBuilder() {
        }

        public ServiceInfoBuilder schema(String schema) {
            this.schema = schema;
            return this;
        }

        public ServiceInfoBuilder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public ServiceInfoBuilder port(int port) {
            this.port = port;
            return this;
        }

        public ServiceInfoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ServiceInfoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ServiceInfoBuilder metadata(Map<String, String> metadata) {
            if (this.metadata == null) {
                this.metadata = new LinkedHashMap<>();
            }
            this.metadata.putAll(metadata);
            return this;
        }

        public ServiceInfoBuilder withMetadata(String key, String data) {
            if (this.metadata == null) {
                this.metadata = new LinkedHashMap<>();
            }
            this.metadata.put(key, data);
            return this;
        }

        public ServiceInfo build() {
            if (Strings.isNullOrEmpty(this.id)) {
                this.id = NamingUtils.format("{}:{}/{}", this.ip, this.port, this.name);
            }
            return new ServiceInfo(schema, ip, port, name, id, metadata);
        }
    }
}
