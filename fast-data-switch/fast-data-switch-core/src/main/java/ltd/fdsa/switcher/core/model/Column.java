package ltd.fdsa.switcher.core.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import ltd.fdsa.switcher.core.util.TypeConvertUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class Column {

    private Object rawData;

    private int byteSize;

    private String name;

    public Column(final String name, final Object object) {
        this(name, object, 8); // TODO get size of the object
    }

    public Column(final String name, final Object object, int byteSize) {
        this.name = name;
        this.rawData = object;
        this.byteSize = byteSize;
    }

    public Long asLong() {
        return TypeConvertUtils.convert(this.rawData, Long.class);
    }

    public Double asDouble() {
        return TypeConvertUtils.convert(this.rawData, Double.class);
    }

    public String asString() {
        return TypeConvertUtils.convert(this.rawData, String.class);
    }

    public Date asDate() {
        return TypeConvertUtils.convert(this.rawData, Date.class);
    }

    public Boolean asBoolean() {
        return TypeConvertUtils.convert(this.rawData, Boolean.class);
    }

    public BigDecimal asBigDecimal() {
        return TypeConvertUtils.convert(this.rawData, BigDecimal.class);
    }

    public BigInteger asBigInteger() {
        return TypeConvertUtils.convert(this.rawData, BigInteger.class);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public enum Type {
        BAD,
        NULL,
        INT,
        LONG,
        DOUBLE,
        STRING,
        BOOL,
        DATE,
        BYTES
    }
}
