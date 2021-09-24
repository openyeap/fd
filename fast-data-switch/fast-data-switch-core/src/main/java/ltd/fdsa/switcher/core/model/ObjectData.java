package ltd.fdsa.switcher.core.model;

import java.nio.ByteBuffer;

public class ObjectData implements Item {
    private byte[] data;

    public ObjectData(byte[]  value) {
        this.data = value;
    }

    @Override
    public Item parse(byte[] bytes) {



        if (bytes[0] != (byte) this.getType().ordinal()) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.allocate(bytes.length - 1);
        buffer.put(bytes, 1, bytes.length - 1);
        buffer.flip(); //need flip
        return new ObjectData(buffer.array());
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(9);
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
        return Type.LONG0;
    }
}
