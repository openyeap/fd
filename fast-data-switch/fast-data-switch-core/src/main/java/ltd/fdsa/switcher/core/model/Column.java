package ltd.fdsa.switcher.core.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import ltd.fdsa.switcher.core.util.TypeConvertUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class Column {

    private Item value;

    private int byteSize;

    private Item key;

    public Column(final String name, final Item object) {
        this(name, object, object.toBytes().length);
    }

    public Column(final String name, final Item object, int byteSize) {
        this.key = new StringData(name);
        this.value = object;
        this.byteSize = byteSize;
    }

    public Long asLong() {
        return TypeConvertUtils.convert(this.value.getValue(), Long.class);
    }

    public Double asDouble() {
        return TypeConvertUtils.convert(this.value.getValue(), Double.class);
    }

    public String asString() {
        return TypeConvertUtils.convert(this.value.getValue(), String.class);
    }

    public Date asDate() {
        return TypeConvertUtils.convert(this.value.getValue(), Date.class);
    }

    public Boolean asBoolean() {
        return TypeConvertUtils.convert(this.value.getValue(), Boolean.class);
    }

    public BigDecimal asBigDecimal() {
        return TypeConvertUtils.convert(this.value.getValue(), BigDecimal.class);
    }

    public BigInteger asBigInteger() {
        return TypeConvertUtils.convert(this.value.getValue(), BigInteger.class);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
