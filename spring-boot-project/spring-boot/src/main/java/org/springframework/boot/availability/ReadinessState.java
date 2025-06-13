/*
 * Copyright 2012-2020 the original author or authors.
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

package org.springframework.boot.availability;

/**
 * "Readiness" state of the application.
 * <p>
 * An application is considered ready when it's {@link LivenessState live} and willing to
 * accept traffic. "Readiness" failure means that the application is not able to accept
 * traffic and that the infrastructure should stop routing requests to it.
 *
 * @author Brian Clozel
 * @since 2.3.0
 */
// 应用程序的“就绪”状态。
// <p>
// 当应用程序处于 {@link LivenessState live} 状态并愿意接受流量时，即被视为就绪状态。“就绪”失败意味着应用程序无法接受流量，并且基础架构应停止向其路由请求。
public enum ReadinessState implements AvailabilityState {

	/**
	 * The application is ready to receive traffic.
	 */
	// 应用已准备好接收流量。
	ACCEPTING_TRAFFIC,

	/**
	 * The application is not willing to receive traffic.
	 */
	// 应用不愿意接收流量。
	REFUSING_TRAFFIC

}
