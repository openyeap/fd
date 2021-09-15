package ltd.fdsa.switcher.core.model;

import lombok.var;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Record {

    private final List<Column> columns;

    public Record(Column... columns) {
        this.columns = Arrays.stream(columns).collect(Collectors.toList());
    }

    public boolean Add(Column... columns) {
        if (columns.length == 1) {
            return this.columns.add(columns[0]);
        }
        if (columns.length > 1) {
            return this.columns.addAll(Arrays.stream(columns).collect(Collectors.toList()));
        }
        return true;
    }

    public int size() {
        return this.columns.size();
    }

    public boolean isEmpty() {
        return this.columns == null || this.columns.size() <= 0;
    }

    public Collection<Column> entrySet() {
        return this.columns;
    }
}
