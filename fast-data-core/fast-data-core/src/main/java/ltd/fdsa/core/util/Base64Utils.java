package ltd.fdsa.core.util;


import lombok.var;

import java.util.Base64;

public class Base64Utils {


    public static String urlEncode(String input) {
        return Base64.getUrlEncoder().encodeToString(input.getBytes());
    }

    public static String urlDecode(String input) {
        var data = Base64.getDecoder().decode(input.getBytes());
        return new String(data);
    }

    public static String encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static String decode(String input) {
        var data = Base64.getDecoder().decode(input);
        return new String(data);
    }

    public static int sum(String input) {
        int a = 0;
        for (char item : input.toCharArray()) {
            // h = 31 * h + item;
            a = (a << 5) - a + item;
        }
        return a < 0 ? -a : a;
    }
}
