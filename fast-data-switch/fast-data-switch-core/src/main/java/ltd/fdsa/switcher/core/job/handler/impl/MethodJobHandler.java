package ltd.fdsa.switcher.core.job.handler.impl;

import ltd.fdsa.switcher.core.job.handler.JobHandler;
import ltd.fdsa.web.view.Result;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class MethodJobHandler implements JobHandler {

    private final Object target;
    private final Method method;
    private Method initMethod;
    private Method destroyMethod;

    public MethodJobHandler(Object target, Method method, Method initMethod, Method destroyMethod) {
        this.target = target;
        this.method = method;
        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
    }


    @Override
    public Result<Object> execute(Map<String, String> context) {
        try {
            return Result.success(method.invoke(target, context.values().toArray()));
        } catch (IllegalAccessException e) {
            return Result.error(e);
        } catch (InvocationTargetException e) {
            return Result.error(e);
        }
    }

    @Override
    public void init() {
        if (initMethod != null) {
            try {
                initMethod.invoke(target);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {
        if (destroyMethod != null) {
            try {
                destroyMethod.invoke(target);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + "[" + target.getClass() + "#" + method.getName() + "]";
    }
}
