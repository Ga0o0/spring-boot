/*
 * Copyright 2012-2024 the original author or authors.
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

import org.springframework.boot.context.properties.bind.Binder.Context;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;

/**
 * Internal strategy used by {@link Binder} to bind data objects. A data object is an
 * object composed itself of recursively bound properties.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 * @see JavaBeanBinder
 * @see ValueObjectBinder
 */
// {@link Binder} 用于绑定数据对象的内部策略。数据对象是由递归绑定的属性组成的对象。
interface DataObjectBinder {

	/**
	 * Return a bound instance or {@code null} if the {@link DataObjectBinder} does not
	 * support the specified {@link Bindable}.
	 * @param <T> the source type
	 * @param name the name being bound
	 * @param target the bindable to bind
	 * @param context the bind context
	 * @param propertyBinder property binder
	 * @return a bound instance or {@code null}
	 */
	// 如果 {@link DataObjectBinder} 不支持指定的 {@link Bindable}，则返回绑定实例或 {@code null}。
	// @param <T> 源类型
	// @param name 被绑定的名称
	// @param target 要绑定的可绑定对象
	// @param context 绑定上下文
	// @param propertyBinder 属性绑定器
	// @return 绑定实例或 {@code null}
	<T> T bind(ConfigurationPropertyName name, Bindable<T> target, Context context,
			DataObjectPropertyBinder propertyBinder);

	/**
	 * Return a newly created instance or {@code null} if the {@link DataObjectBinder}
	 * does not support the specified {@link Bindable}.
	 * @param <T> the source type
	 * @param target the bindable to create
	 * @param context the bind context
	 * @return the created instance
	 */
	// 如果 {@link DataObjectBinder} 不支持指定的 {@link Bindable}，则返回新创建的实例或 {@code null}。
	// @param <T> 源类型
	// @param target 要创建的可绑定对象
	// @param context 绑定上下文
	// @return 已创建的实例
	<T> T create(Bindable<T> target, Context context);

	/**
	 * Callback that can be used to add additional suppressed exceptions when an instance
	 * cannot be created.
	 * @param <T> the source type
	 * @param target the bindable that was being created
	 * @param context the bind context
	 * @param exception the exception about to be thrown
	 */
	// 当无法创建实例时，可用于添加额外抑制异常的回调函数。
	// @param <T> 源类型
	// @param target 正在创建的可绑定对象
	// @param context 绑定上下文
	// @param exception 即将抛出的异常
	default <T> void onUnableToCreateInstance(Bindable<T> target, Context context, RuntimeException exception) {
	}

}
