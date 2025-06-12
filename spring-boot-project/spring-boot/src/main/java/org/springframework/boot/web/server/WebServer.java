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

package org.springframework.boot.web.server;

/**
 * Simple interface that represents a fully configured web server (for example Tomcat,
 * Jetty, Netty). Allows the server to be {@link #start() started} and {@link #stop()
 * stopped}.
 *
 * @author Phillip Webb
 * @author Dave Syer
 * @since 2.0.0
 */
// 代表已完全配置的 Web 服务器（例如 Tomcat、Jetty、Netty）的简单接口。允许服务器 {@link #start() 启动} 和 {@link #stop() 停止}。
public interface WebServer {

	/**
	 * Starts the web server. Calling this method on an already started server has no
	 * effect.
	 * @throws WebServerException if the server cannot be started
	 */
	// 启动 Web 服务器。在已启动的服务器上调用此方法无效。
	// @throws WebServerException 如果服务器无法启动
	void start() throws WebServerException;

	/**
	 * Stops the web server. Calling this method on an already stopped server has no
	 * effect.
	 * @throws WebServerException if the server cannot be stopped
	 */
	// 停止 Web 服务器。在已停止的服务器上调用此方法无效。
	// @throws WebServerException 如果服务器无法停止
	void stop() throws WebServerException;

	/**
	 * Return the port this server is listening on.
	 * @return the port (or -1 if none)
	 */
	// 返回此服务器正在监听的端口。
	// @return 端口（如果没有，则返回 -1）
	int getPort();

	/**
	 * Initiates a graceful shutdown of the web server. Handling of new requests is
	 * prevented and the given {@code callback} is invoked at the end of the attempt. The
	 * attempt can be explicitly ended by invoking {@link #stop}. The default
	 * implementation invokes the callback immediately with
	 * {@link GracefulShutdownResult#IMMEDIATE}, i.e. no attempt is made at a graceful
	 * shutdown.
	 * @param callback the callback to invoke when the graceful shutdown completes
	 * @since 2.3.0
	 */
	// 启动 Web 服务器的优雅关闭。系统将阻止处理新请求，并在尝试结束时调用指定的 {@code callback}。
	// 可以通过调用 {@link #stop} 显式结束尝试。默认实现会立即调用回调函数 {@link GracefulShutdownResult#IMMEDIATE}，即不会尝试优雅关闭。
	// @param 回调函数用于指定优雅关闭完成后要调用的回调函数。
	default void shutDownGracefully(GracefulShutdownCallback callback) {
		callback.shutdownComplete(GracefulShutdownResult.IMMEDIATE);
	}

	/**
	 * Destroys the web server such that it cannot be started again.
	 * @since 3.2.0
	 */
	// 破坏 Web 服务器，使其无法再次启动。
	default void destroy() {
		stop();
	}

}
