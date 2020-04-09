package com.daoshu.logging.aop;

import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.daoshu.logging.annotation.LogAnnotation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Order(-1) // 保证该在@Transactional之前执行
public class LogAnnotationAspect {

	@Autowired
//	private LogService logService ; 
	@Around("execution(* com.daoshu.*(..)) && @annotation(logAnnotation)")
	public Object logSave(ProceedingJoinPoint joinPoint, LogAnnotation logAnnotation) throws Throwable {
		// 记录开始时间
		long start = System.currentTimeMillis();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

		// 获取方法参数
		Object[] args = new Object[0];
		if (logAnnotation.withParam()) {
			args = joinPoint.getArgs();// 参数值
		}
		String info = String.join(",", logAnnotation.module(), methodSignature.getDeclaringTypeName(),
				methodSignature.getName());

		Object result = null;
		try {
			// 调用原来的方法
			result = joinPoint.proceed();
			log.debug(info);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		} finally {
		}
		return result;
	}
}