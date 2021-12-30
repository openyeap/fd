package ltd.fdsa.database.sql.utils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public interface ArrayUtils {
    static <T> List<T> toList(T value, T[] furtherValues) {
        List<T> list = new ArrayList<>(furtherValues.length + 1);
        list.add(value);
        list.addAll(asList(furtherValues));
        return list;
    }
}
