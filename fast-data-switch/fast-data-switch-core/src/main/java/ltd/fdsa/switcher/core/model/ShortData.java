package ltd.fdsa.switcher.core.model;

import java.nio.ByteBuffer;

public class ShortData implements Item {
    private short data;

    public ShortData(short value) {
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
        return new ShortData(buffer.getShort());
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put((byte) getType().ordinal());
        buffer.putShort(this.data);
        return buffer.array();
    }

    @Override
    public Object getValue() {
        return this.data;
    }

    @Override
    public Type getType() {
        return Type.Short0;
    }
}
