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

package com.github.thierrysquirrel.core.configure;


import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: TokenLimitedTrafficConfigure
 * Description:
 * date: 2019/7/17 18:34
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Data
public class TokenLimitedTrafficConfigure implements Serializable {
	private byte[] tokenKey;
	private Integer tokenValue;
	private String lockKey;
	private Integer lockValue;
	private long initialQuantity;
	private long maximumCapacity;
	private long addedQuantity;
	private long intervalTime;
	private TimeUnit timeUnit;
}
