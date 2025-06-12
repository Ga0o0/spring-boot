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

package org.springframework.boot.web.server;

import java.net.InetAddress;
import java.util.Set;

import org.springframework.boot.ssl.SslBundles;

/**
 * A configurable {@link WebServerFactory}.
 *
 * @author Phillip Webb
 * @author Brian Clozel
 * @author Scott Frederick
 * @since 2.0.0
 * @see ErrorPageRegistry
 */
// 可配置的{@link WebServerFactory}。
public interface ConfigurableWebServerFactory extends WebServerFactory, ErrorPageRegistry {

	/**
	 * Sets the port that the web server should listen on. If not specified port '8080'
	 * will be used. Use port -1 to disable auto-start (i.e. start the web application
	 * context but not have it listen to any port).
	 * @param port the port to set
	 */
	// 设置 Web 服务器应监听的端口。如果未指定，则使用端口“8080”。使用 port -1 可禁用自动启动（即启动 Web 应用上下文但不监听任何端口）。
	// @param port 要设置的端口
	void setPort(int port);

	/**
	 * Sets the specific network address that the server should bind to.
	 * @param address the address to set (defaults to {@code null})
	 */
	// 设置服务器应绑定到的特定网络地址。
	// @param address 要设置的地址（默认为 {@code null}）
	void setAddress(InetAddress address);

	/**
	 * Sets the error pages that will be used when handling exceptions.
	 * @param errorPages the error pages
	 */
	// 设置处理异常时将使用的错误页面。
	// @param errorPages 错误页面
	void setErrorPages(Set<? extends ErrorPage> errorPages);

	/**
	 * Sets the SSL configuration that will be applied to the server's default connector.
	 * @param ssl the SSL configuration
	 */
	// 设置将应用于服务器默认连接器的 SSL 配置。
	// @param ssl SSL 配置
	void setSsl(Ssl ssl);

	/**
	 * Sets the SSL bundles that can be used to configure SSL connections.
	 * @param sslBundles the SSL bundles
	 * @since 3.1.0
	 */
	// 设置可用于配置 SSL 连接的 SSL 包。
	// @param sslBundles SSL 包
	void setSslBundles(SslBundles sslBundles);

	/**
	 * Sets the HTTP/2 configuration that will be applied to the server.
	 * @param http2 the HTTP/2 configuration
	 */
	// 设置将应用于服务器的 HTTP/2 配置。
	// @param http2 HTTP/2 配置
	void setHttp2(Http2 http2);

	/**
	 * Sets the compression configuration that will be applied to the server's default
	 * connector.
	 * @param compression the compression configuration
	 */
	// 设置将应用于服务器默认连接器的压缩配置。
	// @param compression 压缩配置
	void setCompression(Compression compression);

	/**
	 * Sets the server header value.
	 * @param serverHeader the server header value
	 */
	// 设置服务器标头值。
	// @param serverHeader 服务器标头值
	void setServerHeader(String serverHeader);

	/**
	 * Sets the shutdown configuration that will be applied to the server.
	 * @param shutdown the shutdown configuration
	 * @since 2.3.0
	 */
	// 设置将应用于服务器的关闭配置。
	// @param shutdown 关闭配置
	default void setShutdown(Shutdown shutdown) {

	}

}
