package ltd.fdsa.switcher.core.model;

import java.nio.ByteBuffer;

public class CharData implements Item {
    private char data;

    public CharData(char data) {
        this.data = data;
    }

    @Override
    public Item parse(byte[] bytes) {
        if (bytes.length != 3) {
            return null;
        }
        if (bytes[0] != this.getType().ordinal()) {
            return null;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length - 1);
        byteBuffer.put(bytes, 1, bytes.length-1);
        byteBuffer.flip();
        return new CharData(byteBuffer.getChar());
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put((byte) getType().ordinal());
        buffer.putChar(this.data);
        return buffer.array();
    }

    @Override
    public Object getValue() {
        return this.data;
    }

    @Override
    public Type getType() {
        return Type.Char0;
    }
}
