package cn.zhumingwu.koffer.reflection;

/**
 * 值
 */
public class XValue {
    private final Object value;

    XValue(Object value) {
        this.value = value;
    }

    public XField field(String name) throws NoSuchFieldException {
        return KReflection.field(value.getClass(), name);
    }

    public XMethod method(String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        return KReflection.method(value.getClass(), name, parameterTypes);
    }

    public <T> T value() {
        return (T) value;
    }
}
