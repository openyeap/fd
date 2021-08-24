package ltd.fdsa.switcher.core.model;

public class ByteData implements Data {
    private byte data;

    @Override
    public Object getValue() {
        return this.data;
    }

    @Override
    public Type getType() {
        return Type.Byte;
    }
}
