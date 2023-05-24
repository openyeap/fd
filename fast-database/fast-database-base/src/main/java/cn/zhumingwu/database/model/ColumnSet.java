package cn.zhumingwu.database.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ColumnSet {

    Map<String, ColumnInfo> map = new HashMap<>();


    public int size() {
        return this.map.size();
    }


    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    public ColumnInfo get(Object key) {
        return this.map.get(key);
    }

    public ColumnInfo remove(Object key) {
        return this.map.remove(key);
    }

    public void clear() {
        this.map.clear();
    }

    public Set<Map.Entry<String, ColumnInfo>> entrySet() {
        return this.map.entrySet();
    }

    public ColumnInfo[] values() {
        return this.map.values().toArray(new ColumnInfo[0]);
    }

    public void putAll(Map<? extends String, ? extends ColumnInfo> m) {
        this.map.putAll(m);
    }

    public ColumnInfo put(String key, String alias, String code, boolean numerical) {
        return this.map.put(key, new ColumnInfo(key, alias, code, numerical));
    }

    public ColumnInfo put(ColumnInfo columnInfo) {
        return this.map.put(columnInfo.name, columnInfo);
    }
}
