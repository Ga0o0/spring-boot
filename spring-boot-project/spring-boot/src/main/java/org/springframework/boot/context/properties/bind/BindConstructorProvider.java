/*
 * Copyright 2012-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.context.properties.bind;

import java.lang.reflect.Constructor;

/**
 * Strategy interface used to determine a specific constructor to use when binding.
 *
 * @author Madhura Bhave
 * @since 2.2.1
 */
// 策略接口用于确定绑定时要使用的特定构造函数。
@FunctionalInterface
public interface BindConstructorProvider {

	/**
	 * Default {@link BindConstructorProvider} implementation that only returns a value
	 * when there's a single constructor and when the bindable has no existing value.
	 */
	// 默认的 {@link BindConstructorProvider} 实现，仅在只有一个构造函数且可绑定对象没有现有值时返回值。
	BindConstructorProvider DEFAULT = new DefaultBindConstructorProvider();

	/**
	 * Return the bind constructor to use for the given type, or {@code null} if
	 * constructor binding is not supported.
	 * @param type the type to check
	 * @param isNestedConstructorBinding if this binding is nested within a constructor
	 * binding
	 * @return the bind constructor or {@code null}
	 * @since 3.0.0
	 */
	// 返回用于指定类型的绑定构造函数，如果不支持构造函数绑定，则返回 {@code null}。
	// @param type 要检查的类型
	// @param isNestedConstructorBinding 如果此绑定嵌套在构造函数绑定中
	// @return 绑定构造函数或 {@code null}
	default Constructor<?> getBindConstructor(Class<?> type, boolean isNestedConstructorBinding) {
		return getBindConstructor(Bindable.of(type), isNestedConstructorBinding);
	}

	/**
	 * Return the bind constructor to use for the given bindable, or {@code null} if
	 * constructor binding is not supported.
	 * @param bindable the bindable to check
	 * @param isNestedConstructorBinding if this binding is nested within a constructor
	 * binding
	 * @return the bind constructor or {@code null}
	 */
	// 返回用于指定可绑定对象的绑定构造函数，如果不支持构造函数绑定，则返回 {@code null}。
	// @param bindable 要检查的可绑定对象
	// @param isNestedConstructorBinding 表示此绑定嵌套在构造函数绑定中
	// @return 绑定构造函数，否则返回 {@code null}
	Constructor<?> getBindConstructor(Bindable<?> bindable, boolean isNestedConstructorBinding);

}
