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

package org.springframework.boot.context.event;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Event published as late as conceivably possible to indicate that the application is
 * ready to service requests. The source of the event is the {@link SpringApplication}
 * itself, but beware of modifying its internal state since all initialization steps will
 * have been completed by then.
 *
 * @author Stephane Nicoll
 * @author Chris Bono
 * @since 1.3.0
 * @see ApplicationFailedEvent
 */
// 事件尽可能晚地发布，以表明应用程序已准备好处理请求。
// 事件的来源是 {@link SpringApplication} 本身，但请注意不要修改其内部状态，因为届时所有初始化步骤都已完成。
@SuppressWarnings("serial")
public class ApplicationReadyEvent extends SpringApplicationEvent {

	private final ConfigurableApplicationContext context;

	private final Duration timeTaken;

	/**
	 * Create a new {@link ApplicationReadyEvent} instance.
	 * @param application the current application
	 * @param args the arguments the application is running with
	 * @param context the context that was being created
	 * @param timeTaken the time taken to get the application ready to service requests
	 * @since 2.6.0
	 */
	// 创建一个新的 {@link ApplicationReadyEvent} 实例。
	// @param application 当前应用程序
	// @param args 应用程序运行时使用的参数
	// @param context 正在创建的上下文
	// @param timeTaken 使应用程序准备好处理请求所需的时间
	public ApplicationReadyEvent(SpringApplication application, String[] args, ConfigurableApplicationContext context,
			Duration timeTaken) {
		super(application, args);
		this.context = context;
		this.timeTaken = timeTaken;
	}

	/**
	 * Return the application context.
	 * @return the context
	 */
	// 返回应用上下文。
	// @return 上下文
	public ConfigurableApplicationContext getApplicationContext() {
		return this.context;
	}

	/**
	 * Return the time taken for the application to be ready to service requests, or
	 * {@code null} if unknown.
	 * @return the time taken to be ready to service requests
	 * @since 2.6.0
	 */
	// 返回应用准备好处理请求所需的时间，如果未知则返回 {@code null}。
	// @return 准备好处理请求所需的时间
	public Duration getTimeTaken() {
		return this.timeTaken;
	}

}
