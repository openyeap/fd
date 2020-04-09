/**
 * Copyright 2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.thierrysquirrel.aspect;


import com.github.thierrysquirrel.core.execution.RedisOperationsExecution;
import com.github.thierrysquirrel.core.factory.RedisOperationsFactory;
import com.github.thierrysquirrel.core.utils.AspectUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

/**
 * ClassName: LimiterAspect
 * Description:
 * date: 2019/7/18 11:09
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Aspect
@Slf4j
@Data
public class LimiterAspect {
	private RedisTemplate<Object, Object> redisTemplate;

	public LimiterAspect(RedisTemplate<Object, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Pointcut("@annotation(com.github.thierrysquirrel.annotation.LimitTraffic)")
	public void limitTrafficPointcut() {
		log.debug("Start limiting traffic");
	}

	@Around("limitTrafficPointcut()")
	public Object limitTrafficAround(ProceedingJoinPoint point) throws Throwable {
		Method method = AspectUtils.getMethod(point);
		BoundListOperations<Object, Object> boundListOperations = RedisOperationsFactory.getBoundListOperations(redisTemplate, DigestUtils.sha1(method.toString()));
		Object token = RedisOperationsExecution.getToken(boundListOperations);
		if (ObjectUtils.isEmpty(token)) {
			return null;
		}
		return point.proceed();
	}
}
