package ltd.fdsa.starter.jdbc.pojo;


import java.util.Map;

public class DefaultDyno implements Dyno {
    private final Map<String, Object> backingRow;

    public DefaultDyno(Map<String, Object> backingRow) {
        this.backingRow = backingRow;
    }

    @Override
    public Object obj(String column) {
        return this.backingRow.get(column.toLowerCase());
    }

    @Override
    public String str(String column) {
        return this.obj(column).toString();
    }

}
