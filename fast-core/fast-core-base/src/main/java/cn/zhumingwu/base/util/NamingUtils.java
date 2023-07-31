package cn.zhumingwu.base.util;


import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class NamingUtils {


    private static final String UNDERLINE = "_";

    private static final String HAT = "^";
    private static final Pattern linePattern = Pattern.compile("_(\\w)");
    private static final Pattern humpPattern = Pattern.compile("[A-Z]");


    /**
     * 下划线转驼峰命名
     *
     * @param name name
     * @return String
     */
    public static String underlineToCamel(String name) {
        // 快速检查
        if (Strings.isNullOrEmpty(name)) {
            // 没必要转换
            return name;
        }
        name = name.toLowerCase();
        Matcher matcher = linePattern.matcher(name);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 去掉指定的前缀
     *
     * @param name name
     * @param prefix prefix
     * @return String
     */
    public static String removePrefix(String name, String... prefix) {
        if (Strings.isNullOrEmpty(name)) {
            return name;
        }
        if (null != prefix) {
            // 判断是否有匹配的前缀，然后截取前缀
            // 删除前缀
            return Arrays.stream(prefix).filter(pf -> name.toLowerCase()
                    .matches(NamingUtils.HAT + pf.toLowerCase() + ".*"))
                    .findFirst().map(pf -> name.substring(pf.length())).orElse(name);
        }
        return name;
    }


    /**
     * 驼峰转下划线,效率比上面高
     *
     * @param name name
     * @return String
     */
    public static String camelToUnderline(String name) {
        if (Strings.isNullOrEmpty(name)) {
            return name;
        }
        Matcher matcher = humpPattern.matcher(name);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, UNDERLINE + matcher.group(0));
        }
        matcher.appendTail(sb);
        return sb.toString().toLowerCase();
    }

    public static void formatLog(Logger log, String content, Object... args) {
        content = MessageFormatter.arrayFormat(content, args).getMessage();

        var length = content.length();
        if (length > 60) {
            log.info(build(64, '='));
            log.info(content);
            log.info(build(64, '='));
            return;
        }
        length = 64 - length;

        if (length % 2 > 0) {
            length = length / 2;
            log.info("{}{}{}", build(length, '='), content, build(length + 1, '='));
        } else {
            length = length / 2;
            log.info("{}{}{}", build(length, '='), content, build(length, '='));
        }
    }

    public static String format(String content, Object... args) {
        return MessageFormatter.arrayFormat(content, args).getMessage();
    }

    private static String build(int count, char c) {
        var result = new char[count];
        for (int i = 0; i < count; i++) {
            result[i] = c;
        }
        return new String(result);
    }
}
