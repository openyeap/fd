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

package com.github.thierrysquirrel.autoconfigure;


import com.github.thierrysquirrel.annotation.EnableLimiter;
import com.github.thierrysquirrel.aspect.LimiterAspect;
import com.github.thierrysquirrel.initialize.LimiterInitialize;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * ClassName: LimiterAutoConfigure
 * Description:
 * date: 2019/7/17 17:17
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Configuration
@ConditionalOnBean(annotation = EnableLimiter.class)
public class LimiterAutoConfigure {
	@Resource
	private RedisTemplate<Object, Object> redisTemplate;

	@Bean
	@ConditionalOnMissingBean(LimiterInitialize.class)
	public LimiterInitialize limiterInitialize() {
		return new LimiterInitialize(redisTemplate);
	}

	@Bean
	@ConditionalOnMissingBean(LimiterAspect.class)
	public LimiterAspect limiterAspect() {
		return new LimiterAspect(redisTemplate);
	}
}
