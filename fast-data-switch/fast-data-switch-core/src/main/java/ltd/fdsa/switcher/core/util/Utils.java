package ltd.fdsa.switcher.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public static String getConfigDir() {
        return System.getProperty("hdata.conf.dir") + System.getProperty("file.separator");
    }
}
