package cn.zhumingwu.database.sql.utils;

import lombok.Getter;
import lombok.ToString;
import lombok.var;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString
public class Indentation {
    @Getter
    private final boolean enabled;

    private final int currentIndentation;

    Indentation(boolean enabled) {
        this(enabled, 0);
    }

    Indentation(boolean enabled, int i) {
        this.enabled = enabled;
        this.currentIndentation = i;
    }

    public static Indentation enabled() {
        return new Indentation(true);
    }

    public static Indentation disabled() {
        return new Indentation(false);
    }

    public static Indentation indent(boolean indent) {
        return new Indentation(indent);
    }

    private static String repeat(String input, int count) {
        var builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(input);
        }
        return builder.toString();
    }

    public String getIndent() {
        return enabled ? repeat("  ", currentIndentation) : "";
    }

    public String getDelimiter() {
        return enabled ? System.lineSeparator() : " ";
    }

    public Indentation indent() {
        return new Indentation(enabled, currentIndentation + 1);
    }

    public Indentation deIndent() {
        return new Indentation(enabled, currentIndentation - 1);
    }
}
