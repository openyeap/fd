package cn.zhumingwu.database.sql.utils;

import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.domain.BuildingContext;
import cn.zhumingwu.database.sql.domain.LikeType;
import cn.zhumingwu.database.sql.domain.Placeholder;
import cn.zhumingwu.database.sql.domain.Plain;
import cn.zhumingwu.database.sql.functions.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.var;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BuilderUtils {
    public static String getValued(Object value, BuildingContext context, Indentation indentation) {
        return getValued(value, "", "", context, indentation);
    }

    private static String getValued(Object value, String prefix, String postfix, BuildingContext context, Indentation indentation) {
        if (value == null) {
            return context.getDialect().getLabels().getNull();
        }
        if (Column.class.isAssignableFrom(value.getClass())) {
            return ((Column) value).getFullNameOrAlias(context);
        }
        if (Function.class.isAssignableFrom(value.getClass())) {
            return ((Function) value).getValue(context, indentation);
        }
        if (Plain.class.isAssignableFrom(value.getClass())) {
            return ((Plain) value).getValue();
        }
        if (Placeholder.class.isAssignableFrom(value.getClass())) {
            return ((Placeholder) value).getValue(context, indentation);
        }
        if (Number.class.isAssignableFrom(value.getClass())) {
            return String.valueOf(value);
        }
        if (LocalDate.class.isAssignableFrom(value.getClass())) {
            return valueApostrophe(prefix + context.getDialect().getDateFormatter().format((LocalDate) value) + postfix, context);
        }
        if (LocalDateTime.class.isAssignableFrom(value.getClass())) {
            return valueApostrophe(prefix + context.getDialect().getDateTimeFormatter().format((LocalDateTime) value) + postfix, context);
        }
        if (LocalTime.class.isAssignableFrom(value.getClass())) {
            return valueApostrophe(prefix + context.getDialect().getTimeFormatter().format((LocalTime) value) + postfix, context);
        }
        return valueApostrophe(prefix + value + postfix, context);
    }

    public static String columnApostrophe(String value, BuildingContext context) {
        return apostrophe(value, context.getDialect().getLabels().getColumnApostrophe(), context);
    }

    public static String valueApostrophe(String value, BuildingContext context) {
        return apostrophe(value, context.getDialect().getLabels().getValueApostrophe(), context);
    }

    private static String apostrophe(String value, char apostrophe, BuildingContext context) {
        return apostrophe + context.getDialect().escape(value, apostrophe) + apostrophe;
    }

    public static String buildLikePart(Object value, LikeType likeType, BuildingContext context, Indentation indentation) {
        var prefix = likeType == LikeType.BEFORE || likeType == LikeType.BOTH ? "%" : "";
        var postfix = likeType == LikeType.AFTER || likeType == LikeType.BOTH ? "%" : "";
        return getValued(value, prefix, postfix, context, indentation);
    }
}
