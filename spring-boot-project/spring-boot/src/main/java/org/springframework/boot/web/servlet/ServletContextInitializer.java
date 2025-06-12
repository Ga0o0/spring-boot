/*
 * Copyright 2012-2021 the original author or authors.
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

package org.springframework.boot.web.servlet;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;

/**
 * Interface used to configure a Servlet 3.0+ {@link ServletContext context}
 * programmatically. Unlike {@link WebApplicationInitializer}, classes that implement this
 * interface (and do not implement {@link WebApplicationInitializer}) will <b>not</b> be
 * detected by {@link SpringServletContainerInitializer} and hence will not be
 * automatically bootstrapped by the Servlet container.
 * <p>
 * This interface is designed to act in a similar way to
 * {@link ServletContainerInitializer}, but have a lifecycle that's managed by Spring and
 * not the Servlet container.
 * <p>
 * For configuration examples see {@link WebApplicationInitializer}.
 *
 * @author Phillip Webb
 * @since 1.4.0
 * @see WebApplicationInitializer
 */
// 此接口用于以编程方式配置 Servlet 3.0+ 版本的 {@link ServletContext 上下文}。
// 与 {@link WebApplicationInitializer} 不同，实现此接口（且未实现 {@link WebApplicationInitializer}）的类
// <b>不会被</b> {@link SpringServletContainerInitializer} 检测到，因此不会被 Servlet 容器自动引导。
//
// <p>此接口的设计方式与 {@link ServletContainerInitializer} 类似，但其生命周期由 Spring 而非 Servlet 容器管理。
//
// <p> 有关配置示例，请参阅 {@link WebApplicationInitializer}。
@FunctionalInterface
public interface ServletContextInitializer {

	/**
	 * Configure the given {@link ServletContext} with any servlets, filters, listeners
	 * context-params and attributes necessary for initialization.
	 * @param servletContext the {@code ServletContext} to initialize
	 * @throws ServletException if any call against the given {@code ServletContext}
	 * throws a {@code ServletException}
	 */
	// 使用初始化所需的任何 servlet、过滤器、侦听器上下文参数和属性来配置给定的 {@link ServletContext}。
	// @param servletContext 用于初始化的 {@code ServletContext}
	// @throws ServletException 如果对给定 {@code ServletContext} 的任何调用引发 {@code ServletException}
	void onStartup(ServletContext servletContext) throws ServletException;

}
