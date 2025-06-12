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

import org.springframework.beans.BeanUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * Strategy interface for creating the {@link ConfigurableApplicationContext} used by a
 * {@link SpringApplication}. Created contexts should be returned in their default form,
 * with the {@code SpringApplication} responsible for configuring and refreshing the
 * context.
 *
 * @author Andy Wilkinson
 * @author Phillip Webb
 * @since 2.4.0
 */
// 用于创建 {@link SpringApplication} 使用的 {@link ConfigurableApplicationContext} 的策略接口。
// 创建的上下文应以其默认形式返回，{@code SpringApplication} 负责配置和刷新上下文。
@FunctionalInterface
public interface ApplicationContextFactory {

	/**
	 * A default {@link ApplicationContextFactory} implementation that will create an
	 * appropriate context for the {@link WebApplicationType}.
	 */
	// 默认的 {@link ApplicationContextFactory} 实现将为 {@link WebApplicationType} 创建适当的上下文。
	ApplicationContextFactory DEFAULT = new DefaultApplicationContextFactory();

	/**
	 * Return the {@link Environment} type expected to be set on the
	 * {@link #create(WebApplicationType) created} application context. The result of this
	 * method can be used to convert an existing environment instance to the correct type.
	 * @param webApplicationType the web application type
	 * @return the expected application context type or {@code null} to use the default
	 * @since 2.6.14
	 */
	// 返回预期在 {@link #create(WebApplicationType) 已创建} 应用上下文中设置的 {@link Environment} 类型。
	// 此方法的结果可用于将现有环境实例转换为正确的类型。
	// @param webApplicationType Web 应用类型
	// @return 预期的应用上下文类型，或 {@code null} 使用默认值
	default Class<? extends ConfigurableEnvironment> getEnvironmentType(WebApplicationType webApplicationType) {
		return null;
	}

	/**
	 * Create a new {@link Environment} to be set on the
	 * {@link #create(WebApplicationType) created} application context. The result of this
	 * method must match the type returned by
	 * {@link #getEnvironmentType(WebApplicationType)}.
	 * @param webApplicationType the web application type
	 * @return an environment instance or {@code null} to use the default
	 * @since 2.6.14
	 */
	// 创建一个新的 {@link Environment}，并将其设置在 {@link #create(WebApplicationType) 已创建} 应用上下文中。
	// 此方法的结果必须与 {@link #getEnvironmentType(WebApplicationType)} 返回的类型匹配。
	// @param webApplicationType Web 应用类型
	// @return 一个环境实例，或使用 {@code null} 以使用默认值
	default ConfigurableEnvironment createEnvironment(WebApplicationType webApplicationType) {
		return null;
	}

	/**
	 * Creates the {@link ConfigurableApplicationContext application context} for a
	 * {@link SpringApplication}, respecting the given {@code webApplicationType}.
	 * @param webApplicationType the web application type
	 * @return the newly created application context
	 */
	// 根据给定的 {@code webApplicationType}，为 {@link SpringApplication} 创建 {@link ConfigurableApplicationContext 应用上下文}。
	// @param webApplicationType Web 应用类型
	// @return 新创建的应用上下文
	ConfigurableApplicationContext create(WebApplicationType webApplicationType);

	/**
	 * Creates an {@code ApplicationContextFactory} that will create contexts by
	 * instantiating the given {@code contextClass} through its primary constructor.
	 * @param contextClass the context class
	 * @return the factory that will instantiate the context class
	 * @see BeanUtils#instantiateClass(Class)
	 */
	// 创建一个 {@code ApplicationContextFactory}，它将通过其主构造函数实例化给定的 {@code contextClass} 来创建上下文。
	// @param contextClass 上下文类
	// @return 将实例化上下文类的工厂
	static ApplicationContextFactory ofContextClass(Class<? extends ConfigurableApplicationContext> contextClass) {
		return of(() -> BeanUtils.instantiateClass(contextClass));
	}

	/**
	 * Creates an {@code ApplicationContextFactory} that will create contexts by calling
	 * the given {@link Supplier}.
	 * @param supplier the context supplier, for example
	 * {@code AnnotationConfigApplicationContext::new}
	 * @return the factory that will instantiate the context class
	 */
	// 创建一个 {@code ApplicationContextFactory}，它将通过调用给定的 {@link Supplier} 来创建上下文。
	// @param Supplier 上下文供应商，例如 {@code AnnotationConfigApplicationContext::new}
	// @return 将实例化上下文类的工厂
	static ApplicationContextFactory of(Supplier<ConfigurableApplicationContext> supplier) {
		return (webApplicationType) -> supplier.get();
	}

}
