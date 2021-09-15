package ltd.fdsa.switcher.core.model;

import java.nio.ByteBuffer;
import java.util.Date;

public class DateData implements Item {
    private Date data;

    public DateData(long value) {
        this.data = new Date(value);
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
        return new DateData(buffer.getLong());
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.put((byte) getType().ordinal());
        buffer.putLong(this.data.getTime());
        return buffer.array();
    }

    @Override
    public Object getValue() {
        return this.data;
    }

    @Override
    public Type getType() {
        return Type.DATE0;
    }
}
