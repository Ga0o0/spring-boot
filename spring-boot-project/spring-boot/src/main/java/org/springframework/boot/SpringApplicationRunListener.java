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

import java.time.Duration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

/**
 * Listener for the {@link SpringApplication} {@code run} method.
 * {@link SpringApplicationRunListener}s are loaded through the
 * {@link SpringFactoriesLoader} and should declare a public constructor that accepts a
 * {@link SpringApplication} instance and a {@code String[]} of arguments. A new
 * {@link SpringApplicationRunListener} instance will be created for each run.
 *
 * @author Phillip Webb
 * @author Dave Syer
 * @author Andy Wilkinson
 * @author Chris Bono
 * @since 1.0.0
 */
// {@link SpringApplication} {@code run} 方法的监听器。
// {@link SpringApplicationRunListener} 通过 {@link SpringFactoriesLoader} 加载，并应声明一个公共构造函数，
// 该构造函数接受一个 {@link SpringApplication} 实例和一个 {@code String[]} 参数。
// 每次运行都会创建一个新的 {@link SpringApplicationRunListener} 实例。
public interface SpringApplicationRunListener {

	/**
	 * Called immediately when the run method has first started. Can be used for very
	 * early initialization.
	 * @param bootstrapContext the bootstrap context
	 */
	// 在 run 方法首次启动时立即调用。可用于非常早期的初始化。
	// @param bootstrapContext 引导上下文
	default void starting(ConfigurableBootstrapContext bootstrapContext) {
	}

	/**
	 * Called once the environment has been prepared, but before the
	 * {@link ApplicationContext} has been created.
	 * @param bootstrapContext the bootstrap context
	 * @param environment the environment
	 */
	// 在环境准备好后，但在 {@link ApplicationContext} 创建之前调用。
	// @param bootstrapContext 引导上下文
	// @param environment 环境
	default void environmentPrepared(ConfigurableBootstrapContext bootstrapContext,
			ConfigurableEnvironment environment) {
	}

	/**
	 * Called once the {@link ApplicationContext} has been created and prepared, but
	 * before sources have been loaded.
	 * @param context the application context
	 */
	// 在 {@link ApplicationContext} 创建并准备好后，但在源加载之前调用。
	// @param context 应用程序上下文
	default void contextPrepared(ConfigurableApplicationContext context) {
	}

	/**
	 * Called once the application context has been loaded but before it has been
	 * refreshed.
	 * @param context the application context
	 */
	// 在应用程序上下文加载后，但在刷新之前调用。
	// @param context 应用程序上下文
	default void contextLoaded(ConfigurableApplicationContext context) {
	}

	/**
	 * The context has been refreshed and the application has started but
	 * {@link CommandLineRunner CommandLineRunners} and {@link ApplicationRunner
	 * ApplicationRunners} have not been called.
	 * @param context the application context.
	 * @param timeTaken the time taken to start the application or {@code null} if unknown
	 * @since 2.6.0
	 */
	// 上下文已刷新，应用已启动，但 {@link CommandLineRunner CommandLineRunners} 和 {@link ApplicationRunner ApplicationRunners} 尚未调用。
	// @param context 应用上下文。
	// @param timeTaken 启动应用所用时间，如果未知，则返回 {@code null}
	default void started(ConfigurableApplicationContext context, Duration timeTaken) {
	}

	/**
	 * Called immediately before the run method finishes, when the application context has
	 * been refreshed and all {@link CommandLineRunner CommandLineRunners} and
	 * {@link ApplicationRunner ApplicationRunners} have been called.
	 * @param context the application context.
	 * @param timeTaken the time taken for the application to be ready or {@code null} if
	 * unknown
	 * @since 2.6.0
	 */
	// 在 run 方法完成之前立即调用，此时应用上下文已刷新，并且所有 {@link CommandLineRunner CommandLineRunners} 和 {@link ApplicationRunner ApplicationRunners} 均已调用。
	// @param context 应用上下文。
	// @param timeTaken 应用程序准备就绪所需的时间，如果未知则返回 {@code null}
	default void ready(ConfigurableApplicationContext context, Duration timeTaken) {
	}

	/**
	 * Called when a failure occurs when running the application.
	 * @param context the application context or {@code null} if a failure occurred before
	 * the context was created
	 * @param exception the failure
	 * @since 2.0.0
	 */
	// 在运行应用程序时发生故障时调用。
	// @param context 应用程序上下文，如果在创建上下文之前发生故障则返回 {@code null}
	// @param exception 失败
	default void failed(ConfigurableApplicationContext context, Throwable exception) {
	}

}
