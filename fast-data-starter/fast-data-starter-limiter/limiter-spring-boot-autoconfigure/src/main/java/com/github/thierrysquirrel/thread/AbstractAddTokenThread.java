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

package com.github.thierrysquirrel.thread;


import com.github.thierrysquirrel.annotation.LimitTraffic;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;

/**
 * ClassName: AbstractAddTokenThread
 * Description:
 * date: 2019/7/17 20:04
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Data
public abstract class AbstractAddTokenThread implements Runnable {
	private RedisTemplate<Object, Object> redisTemplate;
	private Method method;
	private LimitTraffic limitTraffic;

	public AbstractAddTokenThread(RedisTemplate<Object, Object> redisTemplate, Method method, LimitTraffic limitTraffic) {
		this.redisTemplate = redisTemplate;
		this.method = method;
		this.limitTraffic = limitTraffic;
	}

	/**
	 * Start adding tokens
	 *
	 * @param redisTemplate redisTemplate
	 * @param method        method
	 * @param limitTraffic  limitTraffic
	 */
	protected abstract void startAddingTokens(RedisTemplate<Object, Object> redisTemplate, Method method, LimitTraffic limitTraffic);

	@Override
	public void run() {
		startAddingTokens(redisTemplate, method, limitTraffic);
	}
}
