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

package com.github.thierrysquirrel.core.execution;


import com.github.thierrysquirrel.annotation.LimitTraffic;
import com.github.thierrysquirrel.core.configure.TokenLimitedTrafficConfigure;
import com.github.thierrysquirrel.core.error.LimitException;
import com.github.thierrysquirrel.core.factory.RedisOperationsFactory;
import com.github.thierrysquirrel.core.factory.RedisOperationsRecursionFactory;
import com.github.thierrysquirrel.core.factory.TokenLimitedTrafficConfigureFactory;
import com.github.thierrysquirrel.core.recursion.RedisOperationsRecursion;
import com.github.thierrysquirrel.thread.AbstractAddTokenThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ClassName: RedisLimitTrafficExecution
 * Description:
 * date: 2019/7/17 20:08
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Slf4j
public class RedisLimitTrafficExecution extends AbstractAddTokenThread {

	public RedisLimitTrafficExecution(RedisTemplate<Object, Object> redisTemplate, Method method, LimitTraffic limitTraffic) {
		super(redisTemplate, method, limitTraffic);
	}

	@Override
	protected void startAddingTokens(RedisTemplate<Object, Object> redisTemplate, Method method, LimitTraffic limitTraffic) {
		TokenLimitedTrafficConfigure redisExecutionConfigure = TokenLimitedTrafficConfigureFactory.getRedisExecutionConfigure(method, limitTraffic);
		RedisOperationsRecursion redisOperationsRecursion = RedisOperationsRecursionFactory.getRedisOperationsRecursion(redisExecutionConfigure);
		BoundListOperations<Object, Object> boundListOperations = RedisOperationsFactory.getBoundListOperations(redisTemplate, redisExecutionConfigure.getTokenKey());
		Long thisSize = boundListOperations.size();
		redisOperationsRecursion.addTokens(boundListOperations, redisExecutionConfigure.getTokenValue(), thisSize);
		BoundValueOperations<Object, Object> boundValueOperations = RedisOperationsFactory.getBoundValueOperations(redisTemplate, redisExecutionConfigure.getLockKey());

		boolean condition = Boolean.TRUE;
		while (condition) {
			Boolean lock = RedisOperationsExecution.lock(boundValueOperations, redisExecutionConfigure.getLockValue(), redisExecutionConfigure.getIntervalTime(), redisExecutionConfigure.getTimeUnit());
			if (lock) {
				long thisOffset = redisOperationsRecursion.getOffset().get() + redisExecutionConfigure.getAddedQuantity();
				redisOperationsRecursion.setOffset(new AtomicLong(thisOffset));
				thisSize = boundListOperations.size();
				redisOperationsRecursion.addTokens(boundListOperations, redisExecutionConfigure.getTokenValue(), thisSize);
				try {
					Thread.sleep(redisExecutionConfigure.getTimeUnit().toMillis(redisExecutionConfigure.getIntervalTime()));
				} catch (InterruptedException e) {
					condition = Boolean.FALSE;
					log.error("Thread.sleep error", new LimitException(e));
				}
			}
		}
	}
}
