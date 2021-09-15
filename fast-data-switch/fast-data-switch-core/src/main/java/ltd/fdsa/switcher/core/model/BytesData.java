package ltd.fdsa.switcher.core.model;

import java.nio.ByteBuffer;

public class BytesData implements Item {
    private byte[] data;

    public BytesData(byte[] data) {
        this.data = data;
    }

    @Override
    public Item parse(byte[] bytes) {
        if (bytes.length < 2) {
            return null;
        }
        if (bytes[0] != this.getType().ordinal()) {
            return null;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length - 1);
        byteBuffer.put(bytes, 1, bytes.length - 1);
        byteBuffer.flip();
        return new BytesData(byteBuffer.array());
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(this.data.length + 1);
        buffer.put((byte) getType().ordinal());
        buffer.put(this.data);
        return buffer.array();
    }

    @Override
    public Object getValue() {
        return this.data;
    }

    @Override
    public Type getType() {
        return Type.Bytes;
    }
}
