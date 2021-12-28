package ltd.fdsa.database.utils;

import lombok.var;
import ltd.fdsa.database.model.FieldInfo;
import ltd.fdsa.database.model.EntityInfo;
import org.springframework.util.ReflectionUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

public class PlaceHolder {
    static final Map<Class<?>, EntityInfo> cache = new HashMap<>();
    static final Table DEFAULT_TABLE;
    static final Column DEFAULT_COLUMN;

    static {
        // 默认注解
        @Table
        final class c {
            @Column
            String name;
        }
        DEFAULT_TABLE = c.class.getAnnotation(Table.class);
        DEFAULT_COLUMN = c.class.getDeclaredFields()[0].getAnnotation(Column.class);
    }

    public static EntityInfo genEntityInfo(Class<?> clazz) {
        if (!cache.containsKey(clazz)) {
            var builder = EntityInfo.builder();
            var table = clazz.getAnnotation(Table.class);
            if (table == null) {
                builder.name(clazz.getName());
            } else {
                builder.name(table.name());
            }
            ReflectionUtils.doWithFields(clazz, field -> {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                var bu = FieldInfo.builder();
                var column = field.getAnnotation(Column.class);
                if (column == null) {
                    bu.name(field.getName());
                    column = DEFAULT_COLUMN;
                } else {
                    bu.name(column.name());
                }
                bu.length(column.length());
                bu.nullable(column.nullable());
                bu.precision(column.precision());
                bu.scale(column.scale());
                bu.type(field.getType());
                bu.length(column.length());
                bu.unique(column.unique());
                var c = bu.build();
                if (field.getAnnotation(Id.class) != null) {
                    builder.id(c);
                }
                builder.field(column.name(), c);
            });
            cache.put(clazz, builder.build());
        }
        return cache.get(clazz);
    }

}
