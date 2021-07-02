package ltd.fdsa.core.lock;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.context.ApplicationContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Aspect
@Component
@Slf4j
public class LockAspect {


    //@annotation指示器实现对标记了Metrics注解的方法进行匹配
    @Pointcut("@annotation(WithLock)")
    public void annotationLockMethod() {
    }


    @Around("annotationLockMethod()")
    public Object metrics(ProceedingJoinPoint joinPoint) throws Throwable {
        WithLock withLock = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(WithLock.class);

        var lock = ApplicationContextHolder
                .getLockManager(withLock.lockManager())
                .getLock(withLock.lockKey());

        if (lock.tryLock(withLock.timeOutSeconds(), TimeUnit.SECONDS)) {
            Object returnValue;
            try {
                //处理任务
                returnValue = joinPoint.proceed();
            } catch (Exception ex) {
                throw ex;
            } finally {
                lock.unlock();   //释放锁
            }
            return returnValue;
        } else {
            //如果不能获取锁，则直接做其他事情
            return null;
        }

    }

}
