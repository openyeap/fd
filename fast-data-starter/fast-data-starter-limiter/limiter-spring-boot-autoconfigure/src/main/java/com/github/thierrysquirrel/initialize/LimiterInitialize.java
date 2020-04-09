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

package com.github.thierrysquirrel.initialize;


import com.github.thierrysquirrel.annotation.LimitTraffic;
import com.github.thierrysquirrel.annotation.TokenLimitedTraffic;
import com.github.thierrysquirrel.core.execution.RedisLimitTrafficExecution;
import com.github.thierrysquirrel.core.execution.ThreadPoolExecutorExecution;
import com.github.thierrysquirrel.core.factory.ThreadPoolFactory;
import com.github.thierrysquirrel.core.utils.AnnotatedMethodsUtils;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ClassName: LimiterInitialize
 * Description:
 * date: 2019/7/17 17:26
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Data
public class LimiterInitialize implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	private RedisTemplate<Object, Object> redisTemplate;

	public LimiterInitialize(RedisTemplate<Object, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	public void initialize() {
		Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(TokenLimitedTraffic.class);
		beansWithAnnotation.forEach((beanName, bean) -> {
			Map<Method, LimitTraffic> methodAndAnnotation = AnnotatedMethodsUtils.getMethodAndAnnotation(bean, LimitTraffic.class);
			ThreadPoolExecutor tokenLimitedTrafficThreadPool = ThreadPoolFactory.createTokenLimitedTrafficThreadPool(beanName, methodAndAnnotation.size());
			methodAndAnnotation.forEach((method, limitTraffic) -> ThreadPoolExecutorExecution.statsThread(tokenLimitedTrafficThreadPool, new RedisLimitTrafficExecution(redisTemplate, method, limitTraffic)));
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
