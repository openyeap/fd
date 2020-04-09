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

package com.github.thierrysquirrel.annotation;


import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: LimitTraffic
 * Description:
 * date: 2019/7/17 16:52
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface LimitTraffic {

	/**
	 * initialQuantity
	 * Initial number of tokens
	 *
	 * @return long
	 */
	long initialQuantity();

	/**
	 * Maximum capacity
	 * Number defaults to initialQuantity
	 * maximumCapacity &lt; initialQuantity   maximumCapacity = initialQuantity
	 *
	 * @return long
	 */
	long maximumCapacity() default 0L;

	/**
	 * addedQuantity
	 * Number of tokens added at intervals
	 *
	 * @return long
	 */
	long addedQuantity();

	/**
	 * Additional capacity interval time
	 * When using, please cooperate with timeUnit
	 *
	 * @return long
	 */
	long intervalTime() default 1000L;

	/**
	 * Unit of time
	 *
	 * @return TimeUnit
	 */
	TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
