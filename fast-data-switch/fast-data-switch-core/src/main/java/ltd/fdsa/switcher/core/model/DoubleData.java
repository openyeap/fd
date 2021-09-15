package ltd.fdsa.switcher.core.model;

import java.nio.ByteBuffer;

public class DoubleData implements Item {
    private double data;

    public DoubleData(double value) {
        this.data = value;
    }

    @Override
    public Item parse(byte[] bytes) {
        if (bytes.length != 9) {
            return null;
        }
        if (bytes[0] != (byte) this.getType().ordinal()) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length - 1);
        buffer.put(bytes, 1, bytes.length - 1);
        buffer.flip(); //need flip
        return new DoubleData(buffer.getDouble());
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.put((byte) getType().ordinal());
        buffer.putDouble(this.data);
        return buffer.array();
    }

    @Override
    public Object getValue() {
        return this.data;
    }

    @Override
    public Type getType() {
        return Type.Double0;
    }
}
