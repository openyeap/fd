package cn.zhumingwu.database.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FilterSet {


    public enum Type {
        eq("eq"),
        gt("gt"),
        gte("gte"),
        lt("lt"),
        lte("lte"),
        neq("neq"),
        like("like"),
        start("start"),
        end("end"),
        in("in"),
        nin("nin"),
        ;


        // 成员变量
        private String name;
        // 构造方法
        private Type(String name) {
            this.name = name;
        }
        // get set 方法
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    private List<FilterInfo> filters = new ArrayList<>();

    public boolean add(FilterInfo filter) {
        return this.filters.add(filter);
    }

    public boolean add(String name, Type type, Object value) {
        return this.filters.add(new FilterInfo(name, type, value));
    }

    public boolean remove(FilterInfo o) {
        return this.filters.remove(o);
    }

    public void clear() {
        this.filters.clear();
    }

    public FilterInfo get(int index) {
        return this.get(index);
    }

    public int size() {
        return this.filters.size();
    }
}
