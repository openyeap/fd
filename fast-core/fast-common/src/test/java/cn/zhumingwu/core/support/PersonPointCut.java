package cn.zhumingwu.core.support;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.core.util.NamingUtils;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Component
public class PersonPointCut implements Pointcut {

    public PersonPointCut() {
        //声明一个aspectj切点,一张切面
//        JdkRegexpMethodPointcut cut = new JdkRegexpMethodPointcut();
//        cut.setPattern("com.fsx.maintest.Person.run"); //它会拦截Person类下所有run的方法（无法精确到方法签名）
//        cut.setPattern(".*run.*");//.号匹配除"\r\n"之外的任何单个字符。*号代表零次或多次匹配前面的字符或子表达式  所以它拦截任意包下任意类的run方法
//        cut.setPatterns(new String[]{".*run.*", ".*say.*"}); //可以配置多个正则表达  式...  sayHi方法也会被拦截

    }


    @Override
    public ClassFilter getClassFilter() {
        return new ClassFilter() {
            @Override
            public boolean matches(Class<?> aClass) {
                NamingUtils.formatLog(log,"getClassFilter:{}", aClass.getCanonicalName());
                return true;
            }
        };
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new MethodMatcher() {
            @Override
            public boolean matches(Method method, Class<?> aClass) {
                NamingUtils.formatLog(log,"{} matches {}", aClass.getCanonicalName(), method.getName());
                return true;
            }

            @Override
            public boolean isRuntime() {
                NamingUtils.formatLog(log,"is run time");
                return false;
            }

            @Override
            public boolean matches(Method method, Class<?> aClass, Object... objects) {
                NamingUtils.formatLog(log,"{} matches {} ...{}", aClass.getCanonicalName(), method.getName(), objects);
                return true;
            }
        };
    }
}
