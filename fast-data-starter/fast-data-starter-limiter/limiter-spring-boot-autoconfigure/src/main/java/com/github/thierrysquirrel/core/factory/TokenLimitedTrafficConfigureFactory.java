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


import com.github.thierrysquirrel.annotation.LimitTraffic;
import com.github.thierrysquirrel.core.configure.TokenLimitedTrafficConfigure;
import org.apache.commons.codec.digest.DigestUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * ClassName: TokenLimitedTrafficConfigureFactory
 * Description:
 * date: 2019/7/17 18:35
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */

public class TokenLimitedTrafficConfigureFactory {
	private TokenLimitedTrafficConfigureFactory() {
	}

	public static TokenLimitedTrafficConfigure getRedisExecutionConfigure(Method method, LimitTraffic limitTraffic) {
		TokenLimitedTrafficConfigure redisExecutionConfigure = new TokenLimitedTrafficConfigure();
		String methodString = method.toString();
		byte[] tokenKey = DigestUtils.sha1(methodString);
		redisExecutionConfigure.setTokenKey(tokenKey);
		redisExecutionConfigure.setTokenValue(Arrays.hashCode(tokenKey));

		String lockKey = DigestUtils.sha1Hex(methodString);
		redisExecutionConfigure.setLockKey(lockKey);
		redisExecutionConfigure.setLockValue(lockKey.hashCode());

		redisExecutionConfigure.setInitialQuantity(limitTraffic.initialQuantity());
		redisExecutionConfigure.setMaximumCapacity(limitTraffic.maximumCapacity());
		redisExecutionConfigure.setAddedQuantity(limitTraffic.addedQuantity());
		redisExecutionConfigure.setIntervalTime(limitTraffic.intervalTime());
		redisExecutionConfigure.setTimeUnit(limitTraffic.timeUnit());

		return redisExecutionConfigure;
	}
}
