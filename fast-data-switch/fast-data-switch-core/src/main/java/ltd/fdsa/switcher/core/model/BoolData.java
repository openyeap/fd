package ltd.fdsa.switcher.core.model;

import java.nio.ByteBuffer;

public class BoolData implements Item {
    private byte data;

    public BoolData(byte data) {
        this.data = data;
    }

    @Override
    public Item parse(byte[] bytes) {
        if (bytes.length != 2) {
            return null;
        }
        if (bytes[0] != this.getType().ordinal()) {
            return null;
        }
        return new BoolData(bytes[1]);
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) getType().ordinal());
        buffer.put(this.data);
        return buffer.array();
    }

    @Override
    public Object getValue() {
        return this.data > 0;
    }

    @Override
    public Type getType() {
        return Type.BOOL0;
    }
}
