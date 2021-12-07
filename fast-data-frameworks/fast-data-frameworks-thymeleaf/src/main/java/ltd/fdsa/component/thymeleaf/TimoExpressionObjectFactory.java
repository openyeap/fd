package ltd.fdsa.component.thymeleaf;

import ltd.fdsa.component.thymeleaf.utility.PageUtil;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 
 */
public class TimoExpressionObjectFactory implements IExpressionObjectFactory {

    public static final String PAGE_UTIL_NAME = "pageUtil";
    public static final PageUtil PAGE_UTIL_OBJECT = new PageUtil();
    public static final String DICT_UTIL_NAME = "dicts";
    public static final String LOG_UTIL_NAME = "logs";

    @Override
    public Set<String> getAllExpressionObjectNames() {
        Set<String> names = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList(
                PAGE_UTIL_NAME,
                DICT_UTIL_NAME,
                LOG_UTIL_NAME
        )));
        return names;
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        if (PAGE_UTIL_NAME.equals(expressionObjectName)) {
            return PAGE_UTIL_OBJECT;
        }

        return null;
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return expressionObjectName != null;
    }
}
