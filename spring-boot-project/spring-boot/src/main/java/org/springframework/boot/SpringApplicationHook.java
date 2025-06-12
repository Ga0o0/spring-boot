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

package org.springframework.boot;

/**
 * Low-level hook that can be used to attach a {@link SpringApplicationRunListener} to a
 * {@link SpringApplication} in order to observe or modify its behavior. Hooks are managed
 * on a per-thread basis providing isolation when multiple applications are executed in
 * parallel.
 *
 * @author Andy Wilkinson
 * @author Phillip Webb
 * @since 3.0.0
 * @see SpringApplication#withHook
 */
// 低级钩子，可用于将 {@link SpringApplicationRunListener} 附加到 {@link SpringApplication}，以便观察或修改其行为。
// 钩子以线程为单位进行管理，以便在多个应用程序并行执行时提供隔离。
@FunctionalInterface
public interface SpringApplicationHook {

	/**
	 * Return the {@link SpringApplicationRunListener} that should be hooked into the
	 * given {@link SpringApplication}.
	 * @param springApplication the source {@link SpringApplication} instance
	 * @return the {@link SpringApplicationRunListener} to attach
	 */
	// 返回应挂接到给定 {@link SpringApplication} 的 {@link SpringApplicationRunListener}。
	// @param springApplication 源 {@link SpringApplication} 实例
	// @return 要附加的 {@link SpringApplicationRunListener}
	SpringApplicationRunListener getRunListener(SpringApplication springApplication);

}
