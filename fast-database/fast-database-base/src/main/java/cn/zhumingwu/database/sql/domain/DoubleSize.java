package cn.zhumingwu.database.sql.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static java.util.Locale.GERMAN;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@ToString
public class DoubleSize implements Size {
    private static final DecimalFormat FORMAT = new DecimalFormat("0.0");

    static {
        FORMAT.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(GERMAN));
    }

    private final double value;

    public static DoubleSize valueOf(Double size) {
        return size != null ? new DoubleSize(size.doubleValue()) : null;
    }

    @Override
    public String getValue() {
        return FORMAT.format(value);
    }
}
