package cn.zhumingwu.data.hub.core.util;

import lombok.var;

import java.util.Set;

public class Utils {

    /**
     * 线程休眠
     *
     * @param millis
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }


    /**
     * 获取配置目录
     *
     * @return
     */
    public static String getName(Set<String> list) {
        var i = 0;
        while (list.contains("field" + i)) {
            i++;
        }
        return "field" + i;
    }
}
