package ltd.fdsa.switcher.core.model;

import lombok.var;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringData implements Item {
    private String data;

    public StringData(String data) {
        this.data = data;
    }

    @Override
    public Item parse(byte[] bytes) {
        if (bytes.length < 3) {
            return null;
        }
        if (bytes[0] != this.getType().ordinal()) {
            return null;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length - 1);
        byteBuffer.put(bytes, 1, bytes.length - 1);
        byteBuffer.flip();
        var data = new String(byteBuffer.array(), StandardCharsets.UTF_8);
        return new StringData(data);
    }

    @Override
    public byte[] toBytes() {
        var content = this.data.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(content.length + 1);
        buffer.put((byte) getType().ordinal());
        buffer.put(content);
        return buffer.array();
    }

    @Override
    public Object getValue() {
        return this.data;
    }

    @Override
    public Type getType() {
        return Type.Chars;
    }
}
