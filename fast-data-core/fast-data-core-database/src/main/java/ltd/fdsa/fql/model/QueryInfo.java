package ltd.fdsa.fql.model;

import lombok.Data;
import lombok.Getter;
import ltd.fdsa.database.sql.domain.OrderDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class QueryInfo {
    final String name;
    final String alias;
    final String code;
    final List<FilterSet> filters;
    final ColumnSet columns;
    final Map<String, OrderDirection> orders;
    final int limit;
    final int offset;
    final boolean distinct;

    QueryInfo(String name, String alias, String code, ColumnSet columns, List<FilterSet> filters,
              Map<String, OrderDirection> orders, int limit, int offset, boolean distinct) {
        this.name = name;
        this.alias = alias;
        this.code = code;
        this.filters = filters;
        this.orders = orders;
        this.limit = limit;
        this.offset = offset;
        this.distinct = distinct;
        this.columns = columns;
    }

    public static QueryBuilder builder() {
        return new QueryBuilder();
    }

    public static class QueryBuilder {
        @Getter
        private String name;
        private String alias;
        private String code;
        private ColumnSet columns = new ColumnSet();
        int limit = Integer.MAX_VALUE;
        int offset = 0;
        boolean distinct = false;
        List<FilterSet> filters = new ArrayList<>();
        Map<String, OrderDirection> orders = new HashMap<>();

        QueryBuilder() {
        }

        public QueryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public QueryBuilder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public QueryBuilder code(String code) {
            this.code = code;
            return this;
        }


        public QueryBuilder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public QueryBuilder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public QueryBuilder distinct() {
            this.distinct = true;
            return this;
        }

        public QueryBuilder order(String column, OrderDirection direction) {
            if (!this.columns.containsKey(column)) {
                this.columns.put(column, column, column, false);
            }
            this.orders.put(column, direction);
            return this;
        }

        public QueryBuilder filters(FilterSet filterSet) {
            this.filters.add(filterSet);
            return this;
        }

        public QueryBuilder column(String name, String alias, String code, boolean numerical) {
            this.columns.put(name, alias, code, numerical);
            return this;
        }

        public QueryBuilder column(ColumnInfo columnInfo) {
            this.columns.put(columnInfo);
            return this;
        }

        public QueryInfo build() {

            return new QueryInfo(name, alias, code, columns, filters, orders, limit, offset, distinct);
        }
    }
}

