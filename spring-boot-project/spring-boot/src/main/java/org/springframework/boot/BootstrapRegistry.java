/*
 * Copyright 2012-2023 the original author or authors.
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
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

/**
 * A simple object registry that is available during startup and {@link Environment}
 * post-processing up to the point that the {@link ApplicationContext} is prepared.
 * <p>
 * Can be used to register instances that may be expensive to create, or need to be shared
 * before the {@link ApplicationContext} is available.
 * <p>
 * The registry uses {@link Class} as a key, meaning that only a single instance of a
 * given type can be stored.
 * <p>
 * The {@link #addCloseListener(ApplicationListener)} method can be used to add a listener
 * that can perform actions when {@link BootstrapContext} has been closed and the
 * {@link ApplicationContext} is fully prepared. For example, an instance may choose to
 * register itself as a regular Spring bean so that it is available for the application to
 * use.
 *
 * @author Phillip Webb
 * @since 2.4.0
 * @see BootstrapContext
 * @see ConfigurableBootstrapContext
 */
// 一个简单的对象注册表，在启动和 {@link Environment} 后处理期间可用，直到 {@link ApplicationContext} 准备好为止。
//
// <p> 可用于注册创建成本高昂或在 {@link ApplicationContext} 可用之前需要共享的实例。
//
// <p> 注册表使用 {@link Class} 作为键，这意味着只能存储给定类型的单个实例。
//
// <p> {@link #addCloseListener(ApplicationListener)} 方法可用于添加一个监听器，
// 该监听器可以在 {@link BootstrapContext} 关闭且 {@link ApplicationContext} 完全准备好时执行操作。
// 例如，实例可以选择将自身注册为常规 Spring bean，以便应用程序可以使用它。
public interface BootstrapRegistry {

	/**
	 * Register a specific type with the registry. If the specified type has already been
	 * registered and has not been obtained as a {@link Scope#SINGLETON singleton}, it
	 * will be replaced.
	 * @param <T> the instance type
	 * @param type the instance type
	 * @param instanceSupplier the instance supplier
	 */
	// 向注册表注册特定类型。如果指定的类型已被注册，且尚未以 {@link Scope#SINGLETON 单例模式获取，则将被替换。
	// @param <T> 实例类型
	// @param type 实例类型
	// @param instanceSupplier 实例供应商
	<T> void register(Class<T> type, InstanceSupplier<T> instanceSupplier);

	/**
	 * Register a specific type with the registry if one is not already present.
	 * @param <T> the instance type
	 * @param type the instance type
	 * @param instanceSupplier the instance supplier
	 */
	// 如果尚不存在特定类型，则向注册表注册该类型。
	// @param <T> 实例类型
	// @param type 实例类型
	// @param instanceSupplier 实例供应商
	<T> void registerIfAbsent(Class<T> type, InstanceSupplier<T> instanceSupplier);

	/**
	 * Return if a registration exists for the given type.
	 * @param <T> the instance type
	 * @param type the instance type
	 * @return {@code true} if the type has already been registered
	 */
	// 如果指定类型已注册，则返回此值。
	// @param <T> 实例类型
	// @param type 实例类型
	// 如果该类型已注册，则返回 {@code true}
	<T> boolean isRegistered(Class<T> type);

	/**
	 * Return any existing {@link InstanceSupplier} for the given type.
	 * @param <T> the instance type
	 * @param type the instance type
	 * @return the registered {@link InstanceSupplier} or {@code null}
	 */
	// 返回给定类型的任何现有 {@link InstanceSupplier}。
	// @param <T> 实例类型
	// @param type 实例类型
	// @return 已注册的 {@link InstanceSupplier} 或 {@code null}
	<T> InstanceSupplier<T> getRegisteredInstanceSupplier(Class<T> type);

	/**
	 * Add an {@link ApplicationListener} that will be called with a
	 * {@link BootstrapContextClosedEvent} when the {@link BootstrapContext} is closed and
	 * the {@link ApplicationContext} has been prepared.
	 * @param listener the listener to add
	 */
	// 添加一个 {@link ApplicationListener}，
	// 当 {@link BootstrapContext} 关闭且 {@link ApplicationContext} 已准备好时，
	// 它将通过 {@link BootstrapContextClosedEvent} 调用。
	// @param listener 要添加的监听器
	void addCloseListener(ApplicationListener<BootstrapContextClosedEvent> listener);

	/**
	 * Supplier used to provide the actual instance when needed.
	 *
	 * @param <T> the instance type
	 * @see Scope
	 */
	// 供应商用于在需要时提供实际实例。
	//
	// @param <T> 实例类型
	@FunctionalInterface
	interface InstanceSupplier<T> {

		/**
		 * Factory method used to create the instance when needed.
		 * @param context the {@link BootstrapContext} which may be used to obtain other
		 * bootstrap instances.
		 * @return the instance
		 */
		// 用于在需要时创建实例的工厂方法。
		// @param context {@link BootstrapContext}，可用于获取其他引导实例。
		// @return 实例
		T get(BootstrapContext context);

		/**
		 * Return the scope of the supplied instance.
		 * @return the scope
		 * @since 2.4.2
		 */
		// 返回所提供实例的范围。
		// @return the scope
		default Scope getScope() {
			return Scope.SINGLETON;
		}

		/**
		 * Return a new {@link InstanceSupplier} with an updated {@link Scope}.
		 * @param scope the new scope
		 * @return a new {@link InstanceSupplier} instance with the new scope
		 * @since 2.4.2
		 */
		// 返回一个更新了 {@link Scope} 的新 {@link InstanceSupplier}。
		// @param scope 新的 scope
		// @return 一个更新了 scope 的新 {@link InstanceSupplier} 实例
		default InstanceSupplier<T> withScope(Scope scope) {
			Assert.notNull(scope, "Scope must not be null");
			InstanceSupplier<T> parent = this;
			return new InstanceSupplier<>() {

				@Override
				public T get(BootstrapContext context) {
					return parent.get(context);
				}

				@Override
				public Scope getScope() {
					return scope;
				}

			};
		}

		/**
		 * Factory method that can be used to create an {@link InstanceSupplier} for a
		 * given instance.
		 * @param <T> the instance type
		 * @param instance the instance
		 * @return a new {@link InstanceSupplier}
		 */
		// 工厂方法，可用于为给定实例创建 {@link InstanceSupplier}。
		// @param <T> 实例类型
		// @param 实例
		// @return 一个新的 {@link InstanceSupplier}
		static <T> InstanceSupplier<T> of(T instance) {
			return (registry) -> instance;
		}

		/**
		 * Factory method that can be used to create an {@link InstanceSupplier} from a
		 * {@link Supplier}.
		 * @param <T> the instance type
		 * @param supplier the supplier that will provide the instance
		 * @return a new {@link InstanceSupplier}
		 */
		// 工厂方法，可用于从 {@link Supplier} 创建 {@link InstanceSupplier}。
		// @param <T> 实例类型
		// @param Supplier 提供实例的供应商
		// @return 一个新的 {@link InstanceSupplier}
		static <T> InstanceSupplier<T> from(Supplier<T> supplier) {
			return (registry) -> (supplier != null) ? supplier.get() : null;
		}

	}

	/**
	 * The scope of an instance.
	 *
	 * @since 2.4.2
	 */
	// 实例的范围。
	enum Scope {

		/**
		 * A singleton instance. The {@link InstanceSupplier} will be called only once and
		 * the same instance will be returned each time.
		 */
		// 单例实例。{@link InstanceSupplier} 只会被调用一次，并且每次都会返回同一个实例。
		SINGLETON,

		/**
		 * A prototype instance. The {@link InstanceSupplier} will be called whenever an
		 * instance is needed.
		 */
		// 原型实例。每当需要实例时，都会调用 {@link InstanceSupplier}。
		PROTOTYPE

	}

}
