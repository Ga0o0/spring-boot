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
 * "Liveness" state of the application.
 * <p>
 * An application is considered live when it's running with a correct internal state.
 * "Liveness" failure means that the internal state of the application is broken and we
 * cannot recover from it. As a result, the platform should restart the application.
 *
 * @author Brian Clozel
 * @since 2.3.0
 */
// 应用的 “活跃” 状态。
// <p>
// 当应用以正确的内部状态运行时，即视为应用处于活跃状态。
// “活跃” 状态失败意味着应用的内部状态已损坏，且我们无法恢复。因此，平台应重启该应用。
public enum LivenessState implements AvailabilityState {

	/**
	 * The application is running and its internal state is correct.
	 */
	// 应用程序正在运行，其内部状态正确。
	CORRECT,

	/**
	 * The application is running but its internal state is broken.
	 */
	// 应用程序正在运行，但其内部状态已损坏。
	BROKEN

}
