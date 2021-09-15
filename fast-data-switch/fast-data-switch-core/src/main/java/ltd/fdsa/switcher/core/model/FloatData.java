package ltd.fdsa.switcher.core.model;

import java.nio.ByteBuffer;

public class FloatData implements Item {
    private float data;

    public FloatData(float value) {
        this.data = value;
    }

    @Override
    public Item parse(byte[] bytes) {
        if (bytes.length != 5) {
            return null;
        }
        if (bytes[0] != (byte) this.getType().ordinal()) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length - 1);
        buffer.put(bytes, 1, bytes.length - 1);
        buffer.flip(); //need flip
        return new FloatData(buffer.getFloat());
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) getType().ordinal());
        buffer.putFloat(this.data);
        return buffer.array();
    }

    @Override
    public Object getValue() {
        return this.data;
    }

    @Override
    public Type getType() {
        return Type.Float0;
    }
}
