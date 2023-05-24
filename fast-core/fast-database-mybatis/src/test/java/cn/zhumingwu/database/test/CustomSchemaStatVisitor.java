package cn.zhumingwu.database.test;

import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDeleteStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGInsertStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGUpdateStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.assertj.core.util.Strings;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomSchemaStatVisitor extends PGSchemaStatVisitor {

    private Map<Character, Character> map = new HashMap<Character, Character>() {
        {
            put('a', 'c');
            put('b', 'd');
            put('c', 'e');
            put('d', 'f');
            put('e', 'g');
            put('f', 'l');
            put('g', 'm');
            put('h', 'n');
            put('i', 'a');
            put('j', 'b');
            put('k', 'o');
            put('l', 'p');
            put('m', 'q');
            put('n', 'r');
            put('o', 'h');
            put('p', 's');
            put('q', 'i');
            put('r', 'j');
            put('s', 'k');
            put('t', 'z');
            put('u', 't');
            put('v', 'u');
            put('w', 'v');
            put('x', 'w');
            put('y', 'x');
            put('z', 'y');

        }
    };

    @Override
    public boolean visit(SQLPropertyExpr x) {
        x.setName(encode(x.getName()));
        log.info("column>{}：{}", x.getOwner(), x.getName());
        return super.visit(x);
    }

    @Override
    public boolean visit(SQLExprTableSource x) {

        if (Strings.isNullOrEmpty(x.getAlias())) {
            x.setAlias(x.getTableName());
        }
        x.setSimpleName(encode(x.getTableName()));
        log.info("table>{}:{}", x.getAlias(), x.getTableName());
        return super.visit(x);
    }

    @Override
    public boolean visit(SQLIdentifierExpr x) {

        x.setName(encode(x.getName()));
        log.info("column>{}：{}", "", x.getName());
        return super.visit(x);
    }

    @Override
    public boolean visit(PGUpdateStatement x) {
        var table = (SQLExprTableSource) x.getTableSource();
        if (table == null) {
            return super.visit(x);
        }
        if (Strings.isNullOrEmpty(table.getAlias())) {
            table.setAlias(table.getTableName());
        }
        table.setSimpleName(encode(table.getTableName()));
        log.info("table>{}:{}", table.getAlias(), table.getTableName());
        return super.visit(x);
    }

    @Override
    public boolean visit(PGInsertStatement x) {
        var table = (SQLExprTableSource) x.getTableSource();
        if (table == null) {
            return super.visit(x);
        }
        if (Strings.isNullOrEmpty(table.getAlias())) {
            table.setAlias(table.getTableName());
        }
        table.setSimpleName(encode(table.getTableName()));
        log.info("table>{}:{}", table.getAlias(), table.getTableName());
        return super.visit(x);
    }

    @Override
    public boolean visit(PGDeleteStatement x) {
        var table = (SQLExprTableSource) x.getTableSource();
        if (table == null) {
            return super.visit(x);
        }
        if (Strings.isNullOrEmpty(table.getAlias())) {
            table.setAlias(table.getTableName());
        }
        table.setSimpleName(encode(table.getTableName()));
        log.info("table>{}:{}", table.getAlias(), table.getTableName());
        return super.visit(x);
    }

    @Override
    public boolean visit(SQLSelectQueryBlock x) {

        var group = x.getGroupBy();
        if (group == null) {
            return super.visit(x);
        }
        var having = (SQLBinaryOpExpr) group.getHaving();

        if (having == null) {
            return super.visit(x);
        }
        if (having.getLeft() instanceof SQLAggregateExpr) {
            var aggregate = (SQLAggregateExpr) having.getLeft();
            for (var argument : aggregate.getArguments()) {
                if (argument instanceof SQLPropertyExpr) {
                    var column = (SQLPropertyExpr) argument;
                    column.setName(encode(column.getName()));
                    log.info("column>{}：{}", column.getOwner(), column.getName());
                }
            }
        }
        if (having.getRight() instanceof SQLAggregateExpr) {
            var aggregate = (SQLAggregateExpr) having.getRight();
            for (var argument : aggregate.getArguments()) {
                if (argument instanceof SQLPropertyExpr) {
                    var column = (SQLPropertyExpr) argument;
                    column.setName(encode(column.getName()));
                    log.info("column>{}：{}", column.getOwner(), column.getName());
                    log.info("column>{}：{}", column.getOwner(), column.getName());
                }
            }
        }
        return super.visit(x);
    }

    String encode(String input) {

        StringBuffer stringBuffer = new StringBuffer();
        for (var c : input.toCharArray()) {
            if (map.containsKey(c)) {
                stringBuffer.append(map.get(c));
            } else {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }

}
