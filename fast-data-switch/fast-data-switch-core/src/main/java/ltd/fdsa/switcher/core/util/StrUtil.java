package ltd.fdsa.switcher.core.util;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {

    private static final long KB_IN_BYTES = 1024;

    private static final long MB_IN_BYTES = 1024 * KB_IN_BYTES;

    private static final long GB_IN_BYTES = 1024 * MB_IN_BYTES;

    private static final long TB_IN_BYTES = 1024 * GB_IN_BYTES;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("(\\$)\\{?(\\w+)\\}?");

    private static String SYSTEM_ENCODING = System.getProperty("file.encoding");

    static {
        if (SYSTEM_ENCODING == null) {
            SYSTEM_ENCODING = "UTF-8";
        }
    }

    private StrUtil() {
    }

    public static String stringify(long byteNumber) {
        if (byteNumber / TB_IN_BYTES > 0) {
            return df.format((double) byteNumber / (double) TB_IN_BYTES) + "TB";
        } else if (byteNumber / GB_IN_BYTES > 0) {
            return df.format((double) byteNumber / (double) GB_IN_BYTES) + "GB";
        } else if (byteNumber / MB_IN_BYTES > 0) {
            return df.format((double) byteNumber / (double) MB_IN_BYTES) + "MB";
        } else if (byteNumber / KB_IN_BYTES > 0) {
            return df.format((double) byteNumber / (double) KB_IN_BYTES) + "KB";
        } else {
            return String.valueOf(byteNumber) + "B";
        }
    }

    public static String replaceVariable(final String param) {
        Map<String, String> mapping = new HashMap<String, String>();

        Matcher matcher = VARIABLE_PATTERN.matcher(param);
        while (matcher.find()) {
            String variable = matcher.group(2);
            String value = System.getProperty(variable);
            if (StringUtils.isEmpty(value)) {
                value = matcher.group();
            }
            mapping.put(matcher.group(), value);
        }

        String retString = param;
        for (final String key : mapping.keySet()) {
            retString = retString.replace(key, mapping.get(key));
        }

        return retString;
    }

    public static String compressMiddle(String s, int headLength, int tailLength) {
        Assert.notNull(s, "Input string must not be null");
        Assert.isTrue(headLength > 0, "Head length must be larger than 0");
        Assert.isTrue(tailLength > 0, "Tail length must be larger than 0");

        if (headLength + tailLength >= s.length()) {
            return s;
        }
        return s.substring(0, headLength) + "..." + s.substring(s.length() - tailLength);
    }
}
