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

package org.springframework.boot;

import java.util.function.Supplier;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * A simple bootstrap context that is available during startup and {@link Environment}
 * post-processing up to the point that the {@link ApplicationContext} is prepared.
 * <p>
 * Provides lazy access to singletons that may be expensive to create, or need to be
 * shared before the {@link ApplicationContext} is available.
 *
 * @author Phillip Webb
 * @since 2.4.0
 */
// 一个简单的引导上下文，在启动和 {@link Environment} 后处理期间可用，直到 {@link ApplicationContext} 准备好为止。
//
// <p> 提供对单例的延迟访问，这些单例的创建成本可能很高，或者需要在 {@link ApplicationContext} 可用之前共享。
public interface BootstrapContext {

	/**
	 * Return an instance from the context if the type has been registered. The instance
	 * will be created if it hasn't been accessed previously.
	 * @param <T> the instance type
	 * @param type the instance type
	 * @return the instance managed by the context
	 * @throws IllegalStateException if the type has not been registered
	 */
	// 如果类型已注册，则从上下文返回一个实例。如果该实例之前未被访问过，则会创建该实例。
	// @param <T> 实例类型
	// @param type 实例类型
	// @return 上下文管理的实例
	// @throws IllegalStateException 如果类型尚未注册
	<T> T get(Class<T> type) throws IllegalStateException;

	/**
	 * Return an instance from the context if the type has been registered. The instance
	 * will be created if it hasn't been accessed previously.
	 * @param <T> the instance type
	 * @param type the instance type
	 * @param other the instance to use if the type has not been registered
	 * @return the instance
	 */
	// 如果类型已注册，则从上下文返回一个实例。如果该实例之前未被访问过，则会创建该实例。
	// @param <T> 实例类型
	// @param type 实例类型
	// @param other 类型未注册时使用的实例
	// @return 实例
	<T> T getOrElse(Class<T> type, T other);

	/**
	 * Return an instance from the context if the type has been registered. The instance
	 * will be created if it hasn't been accessed previously.
	 * @param <T> the instance type
	 * @param type the instance type
	 * @param other a supplier for the instance to use if the type has not been registered
	 * @return the instance
	 */
	// 如果类型已注册，则从上下文返回一个实例。如果该实例之前未被访问过，则会创建该实例。
	// @param <T> 实例类型
	// @param type 实例类型
	// @param other 实例使用的供应商（如果类型尚未注册）
	// @return 实例
	<T> T getOrElseSupply(Class<T> type, Supplier<T> other);

	/**
	 * Return an instance from the context if the type has been registered. The instance
	 * will be created if it hasn't been accessed previously.
	 * @param <T> the instance type
	 * @param <X> the exception to throw if the type is not registered
	 * @param type the instance type
	 * @param exceptionSupplier the supplier which will return the exception to be thrown
	 * @return the instance managed by the context
	 * @throws X if the type has not been registered
	 * @throws IllegalStateException if the type has not been registered
	 */
	// 如果类型已注册，则从上下文返回一个实例。如果该实例之前未被访问过，则会创建该实例。
	// @param <T> 实例类型
	// @param <X> 如果类型未注册，则抛出的异常
	// @param type 实例类型
	// @param exceptionSupplier 返回要抛出的异常的供应商
	// @return 上下文管理的实例
	// @throws X 如果类型未注册
	// @throws IllegalStateException 如果类型未注册
	<T, X extends Throwable> T getOrElseThrow(Class<T> type, Supplier<? extends X> exceptionSupplier) throws X;

	/**
	 * Return if a registration exists for the given type.
	 * @param <T> the instance type
	 * @param type the instance type
	 * @return {@code true} if the type has already been registered
	 */
	// 如果指定类型已注册，则返回该值。
	// @param <T> 实例类型
	// @param type 实例类型
	// @return {@code true} 如果该类型已注册，则返回该值。
	<T> boolean isRegistered(Class<T> type);

}
