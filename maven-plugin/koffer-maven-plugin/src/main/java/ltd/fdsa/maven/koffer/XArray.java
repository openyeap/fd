package ltd.fdsa.maven.koffer;

/**
 * 数组工具
 */
public class XArray {

    /**
     * 数组是否为{@code null}或者是空数组
     *
     * @param array 数组
     * @return 如果是则返回{@code true} 否则返回{@code false}
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
}
