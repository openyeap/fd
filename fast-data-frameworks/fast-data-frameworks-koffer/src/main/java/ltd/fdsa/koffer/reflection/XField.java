package ltd.fdsa.koffer.reflection;

import java.lang.reflect.Field;

/**
 * 字段
 */
public class XField {
    private final Field field;

    XField(Field field) {
        this.field = field;
    }

    public XValue get(Object target) {
        try {
            return new XValue(field.get(target));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public void set(Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public Field field() {
        return field;
    }
}
