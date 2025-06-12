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

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Interface used to indicate that a bean should <em>run</em> when it is contained within
 * a {@link SpringApplication}. Multiple {@link CommandLineRunner} beans can be defined
 * within the same application context and can be ordered using the {@link Ordered}
 * interface or {@link Order @Order} annotation.
 * <p>
 * If you need access to {@link ApplicationArguments} instead of the raw String array
 * consider using {@link ApplicationRunner}.
 *
 * @author Dave Syer
 * @since 1.0.0
 * @see ApplicationRunner
 */
// 此接口用于指示当 bean 包含在 {@link SpringApplication} 中时，它应该<em>运行</em>。
// 可以在同一个应用程序上下文中定义多个 {@link CommandLineRunner} bean，
// 并且可以使用 {@link Ordered} 接口或 {@link Order @Order} 注解进行排序。
//
// <p>如果您需要访问 {@link ApplicationArguments} 而不是原始字符串数组，
// 请考虑使用 {@link ApplicationRunner}。
@FunctionalInterface
public interface CommandLineRunner extends Runner {

	/**
	 * Callback used to run the bean.
	 * @param args incoming main method arguments
	 * @throws Exception on error
	 */
	// 用于运行 bean 的回调。
	// @param args 传入的主方法参数
	// @throws 错误抛出异常
	void run(String... args) throws Exception;

}
