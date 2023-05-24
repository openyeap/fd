package cn.zhumingwu.database.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class Node {
    @Getter
    Map<String, Object> value;
    @Getter
    List<Map<String, Object>> values;
    @Getter
    final String name;
    @Getter
    boolean changed = false;

    public Node(Map<String, Object> value, String name) {
        this.name = name;
        this.value = value;
        this.values = null;
    }

    public Node(List<Map<String, Object>> values, String name, int index) {
        this.name = name;
        this.values = values;
        this.value = values.get(index);
    }

    public Object getObject() {
        if (this.values != null) {
            return this.values;
        }
        return this.value;
    }

    public boolean isList() {
        return !(this.values == null);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (this.values != null) {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.values);
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.value);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}