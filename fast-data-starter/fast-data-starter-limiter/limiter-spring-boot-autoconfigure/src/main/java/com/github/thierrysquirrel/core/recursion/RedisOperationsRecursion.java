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

package com.github.thierrysquirrel.core.recursion;


import com.github.thierrysquirrel.core.execution.RedisOperationsExecution;
import lombok.Data;
import org.springframework.data.redis.core.BoundListOperations;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ClassName: RedisOperationsRecursion
 * Description:
 * date: 2019/7/17 19:06
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Data
public class RedisOperationsRecursion {
	private AtomicLong offset;
	private Long maximumCapacity;

	public RedisOperationsRecursion(AtomicLong offset, Long maximumCapacity) {
		this.offset = offset;
		this.maximumCapacity = maximumCapacity;
	}
	
	public void addTokens(BoundListOperations<Object, Object> boundListOperations, Integer tokenValue, Long thisSize) {
		long thisOffset = offset.get();
		boolean loading=maximumCapacity > thisSize && thisOffset > 0;
		while (loading){
			long update = thisOffset - 1;
			offset.compareAndSet(thisOffset, update);
			thisSize = RedisOperationsExecution.addTokens(boundListOperations, tokenValue);
			thisOffset = offset.get();
			loading=maximumCapacity > thisSize && thisOffset > 0;
		}
		offset.compareAndSet(thisOffset, 0);
	}

}
