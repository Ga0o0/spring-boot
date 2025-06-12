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

package org.springframework.boot.web.servlet.server;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;

/**
 * Factory interface that can be used to create a {@link WebServer}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 * @see WebServer
 */
// 工厂接口可用于创建{@link WebServer}。
@FunctionalInterface
public interface ServletWebServerFactory extends WebServerFactory {

	/**
	 * Gets a new fully configured but paused {@link WebServer} instance. Clients should
	 * not be able to connect to the returned server until {@link WebServer#start()} is
	 * called (which happens when the {@code ApplicationContext} has been fully
	 * refreshed).
	 * @param initializers {@link ServletContextInitializer}s that should be applied as
	 * the server starts
	 * @return a fully configured and started {@link WebServer}
	 * @see WebServer#stop()
	 */
	// 获取一个已完全配置但已暂停的新 {@link WebServer} 实例。
	// 客户端在调用 {@link WebServer#start()} 之前（即 {@code ApplicationContext} 完全刷新后）无法连接到返回的服务器。
	// @param 初始化程序 {@link ServletContextInitializer} 应在服务器启动时应用
	// @return 一个已完全配置并启动的 {@link WebServer}
	WebServer getWebServer(ServletContextInitializer... initializers);

}
