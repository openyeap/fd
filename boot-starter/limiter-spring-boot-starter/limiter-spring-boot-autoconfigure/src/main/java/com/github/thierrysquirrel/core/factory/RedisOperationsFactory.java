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

package com.github.thierrysquirrel.core.factory;


import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * ClassName: RedisOperationsFactory
 * Description:
 * date: 2019/7/17 18:58
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
public class RedisOperationsFactory {
	private RedisOperationsFactory() {
	}

	public static BoundValueOperations<Object, Object> getBoundValueOperations(RedisTemplate<Object, Object> redisTemplate, String lockKey) {
		return redisTemplate.boundValueOps(lockKey);
	}

	public static BoundListOperations<Object, Object> getBoundListOperations(RedisTemplate<Object, Object> redisTemplate, byte[] tokenKey) {
		return redisTemplate.boundListOps(tokenKey);
	}

}
