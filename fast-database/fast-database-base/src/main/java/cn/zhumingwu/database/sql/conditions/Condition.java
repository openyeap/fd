package cn.zhumingwu.database.sql.conditions;

import cn.zhumingwu.database.sql.domain.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.var;
import cn.zhumingwu.database.sql.domain.*;
import cn.zhumingwu.database.sql.utils.Indentation;

import java.util.ArrayList;
import java.util.List;

import static cn.zhumingwu.database.sql.domain.ConcatenationType.AND;
import static cn.zhumingwu.database.sql.domain.ConcatenationType.OR;
import static cn.zhumingwu.database.sql.domain.ConditionType.WHERE;
import static cn.zhumingwu.database.sql.domain.ConditionType.WHERE_NOT;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@Getter
@ToString
public abstract class Condition {
    @Setter
    private ConditionType type;

    @Setter
    private ConcatenationType concatenation;

    private List<Integer> placeholderSqlTypes;

    protected abstract String doBuild(BuildingContext context, Indentation indentation);

    public String build(BuildingContext context, boolean concatenate, Indentation indentation) {
        var builder = new StringBuilder();
        if (concatenate) {
            builder.append(concatenation == AND ? context.getDialect().getLabels().getAnd() : context.getDialect().getLabels().getOr());
        }
        if (type == WHERE_NOT) {
            builder.append(" ").append(context.getDialect().getLabels().getNot());
        }
        if (concatenate) {
            if (indentation.isEnabled() && CombinedCondition.class.isAssignableFrom(getClass())) {
                builder.append(context.getDelimiter());
            } else {
                builder.append(" ");
            }
        }
        builder.append(doBuild(context, indentation));
        return builder.toString();
    }

    public Condition and(Condition other) {
        return append(other, AND, WHERE);
    }

    public Condition or(Condition other) {
        return append(other, OR, WHERE);
    }

    public Condition andNot(Condition other) {
        return append(other, AND, WHERE_NOT);
    }

    public Condition orNot(Condition other) {
        return append(other, OR, WHERE_NOT);
    }

    public Condition append(Condition other, ConcatenationType concatenationType, ConditionType conditionType) {
        other.setConcatenation(concatenationType);
        other.setType(conditionType);
        CombinedCondition combined;
        if (EmptyCondition.class.isAssignableFrom(getClass())) {
            return other;
        }
        if (!CombinedCondition.class.isAssignableFrom(getClass())) {
            combined = new CombinedCondition(this);
        } else {
            combined = (CombinedCondition) this;
        }
        combined.append(other);
        return combined;
    }

    public static Condition plain(String plainCondition) {
        return new PlainCondition(plainCondition);
    }


    public static Condition emptyCondition() {
        return new EmptyCondition();
    }

    public static Condition nested(Condition condition) {
        return new CombinedCondition(condition);
    }

    protected void addPlaceholderSqlTypes(List<Integer> sqlTypes) {
        if (sqlTypes != null) {
            if (placeholderSqlTypes == null) {
                placeholderSqlTypes = new ArrayList<>();
            }
            placeholderSqlTypes.addAll(sqlTypes);
        }
    }

    protected static List<Integer> resolvePlaceholderSqlTypes(Object... values) {
        List<Integer> placeholderSqlTypes = new ArrayList<>();
        if (values.length > 1 && SqlTypeSupplier.class.isAssignableFrom(values[0].getClass())) {
            for (int i = 1; i < values.length; i++) {
                if (values[i] != null && Placeholder.class.isAssignableFrom(values[i].getClass())) {
                    placeholderSqlTypes.add(Integer.valueOf(((SqlTypeSupplier) values[0]).getSqlType()));
                }
            }
        }
        return placeholderSqlTypes;
    }

    public List<Integer> getPlaceholderSqlTypes() {
        return placeholderSqlTypes;
    }
}
