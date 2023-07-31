package cn.zhumingwu.dataswitch.core.model;

import com.google.iot.cbor.CborConversionException;
import com.google.iot.cbor.CborObject;
import com.google.iot.cbor.CborTextString;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


import java.nio.ByteBuffer;

@Data
@Slf4j
public class Column {
    private final String key;
    private final Object value;

    private final Type type;

    Column(String key, Object value, Type type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public Column(String key, Integer value) {
        this(key, value, Type.Number);
    }

    public Column(String key, String value) {
        this(key, value, Type.String);
    }

    public byte[] toByteArray() {
        try {
            var dataItem = CborObject.createFromJavaObject(this.value).toCborByteArray();
            var keyItem = CborTextString.create(this.key).toCborByteArray();
            ByteBuffer buffer = ByteBuffer.allocate(keyItem.length + dataItem.length);
            buffer.put(keyItem);
            buffer.put(dataItem);
            return buffer.array();
        } catch (CborConversionException e) {
            log.error("cbor conversion exception", e);
            return new byte[0];
        }
    }

    public enum Type {
        String, Object, Array, Number, Bool,
    }
}
