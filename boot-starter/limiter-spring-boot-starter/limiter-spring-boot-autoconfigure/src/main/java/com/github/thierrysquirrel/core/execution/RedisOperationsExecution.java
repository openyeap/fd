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


import lombok.Data;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: RedisOperationsExecution
 * Description:
 * date: 2019/7/17 17:39
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Data
public class RedisOperationsExecution {
	private RedisOperationsExecution() {
	}

	public static Boolean lock(BoundValueOperations<Object, Object> boundValueOperations, Integer lockValue, Long timeout, TimeUnit timeUnit) {
		return boundValueOperations.setIfAbsent(lockValue, timeout, timeUnit);
	}

	public static Long addTokens(BoundListOperations<Object, Object> boundListOperations, Integer tokenValue) {
		return boundListOperations.leftPush(tokenValue);
	}

	public static Object getToken(BoundListOperations<Object, Object> boundListOperations) {
		return boundListOperations.leftPop();
	}

}
