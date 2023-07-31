package cn.zhumingwu.dataswitch.core.model;

import com.google.iot.cbor.CborConversionException;
import com.google.iot.cbor.CborMap;
import com.google.iot.cbor.CborParseException;
import lombok.extern.slf4j.Slf4j;


import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Record {

    private final Map<String, Object> columns;

    public Record(byte... bytes) {
        this.columns = new HashMap<>();
        if (bytes.length > 0) {
            try {
                var map = CborMap.createFromCborByteArray(bytes);
                for (var entry : map.toNormalMap().entrySet()) {
                    this.columns.put(entry.getKey(), entry.getValue());
                }
            } catch (CborParseException e) {

            } catch (CborConversionException e) {
            }
        }
    }

    public boolean add(Column... columns) {
        for (var column : columns) {
            this.columns.put(column.getKey(), column.getValue());
        }
        return true;
    }

    public boolean add(String key, Object value) {
        if (value instanceof String) {
            this.columns.put(key, new Column(key, value.toString()));
            return true;
        }
        return false;
    }

    public int size() {
        return this.columns.size();
    }

    public boolean isEmpty() {
        return this.columns == null || this.columns.size() <= 0;
    }

    public Map<String, Object> toNormalMap() {
        return this.columns;
    }

    @Override
    public String toString() {
        try {
            var map = CborMap.createFromJavaObject(this.columns);
            return map.toJsonString();
        } catch (CborConversionException e) {
            log.error("Cbor Conversion Exception", e);
            return "";
        }
    }

    public byte[] toByteArray() {
        try {
            var map = CborMap.createFromJavaObject(this.columns);
            return map.toCborByteArray();
        } catch (CborConversionException e) {
            log.error("Cbor Conversion Exception", e);
            return new byte[0];
        }
    }
}
