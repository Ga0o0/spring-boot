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

package org.springframework.boot;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

/**
 * Filter that can be used to exclude beans definitions from having their
 * {@link AbstractBeanDefinition#setLazyInit(boolean) lazy-init} set by the
 * {@link LazyInitializationBeanFactoryPostProcessor}.
 * <p>
 * Primarily intended to allow downstream projects to deal with edge-cases in which it is
 * not easy to support lazy-loading (such as in DSLs that dynamically create additional
 * beans). Adding an instance of this filter to the application context can be used for
 * these edge cases.
 * <p>
 * A typical example would be something like this: <pre>
 * &#64;Bean
 * public static LazyInitializationExcludeFilter integrationLazyInitializationExcludeFilter() {
 *   return LazyInitializationExcludeFilter.forBeanTypes(IntegrationFlow.class);
 * }
 * </pre>
 * <p>
 * NOTE: Beans of this type will be instantiated very early in the spring application
 * lifecycle so they should generally be declared static and not have any dependencies.
 *
 * @author Tyler Van Gorder
 * @author Philip Webb
 * @since 2.2.0
 */
// 可用于排除 Bean 定义使其 {@link LazyInitializationBeanFactoryPostProcessor} 无法设置其
// {@link AbstractBeanDefinition#setLazyInit(boolean) lazy-init} 的过滤器。
//
// <p>主要用于允许下游项目处理不易支持延迟加载的边缘情况（例如在动态创建其他 Bean 的 DSL 中）。
// 将此过滤器的实例添加到应用程序上下文可用于处理这些边缘情况。
//
// <p>典型示例如下：
// <pre>
// @Bean
// public static LazyInitializationExcludeFilter integrationLazyInitializationExcludeFilter() {
// 		return LazyInitializationExcludeFilter.forBeanTypes(IntegrationFlow.class);
// }
// </pre>
//
// <p>注意：这种类型的 Bean 将在 Spring 应用程序生命周期的早期实例化，因此它们通常应声明为静态的并且没有任何依赖项。
@FunctionalInterface
public interface LazyInitializationExcludeFilter {

	/**
	 * Returns {@code true} if the specified bean definition should be excluded from
	 * having {@code lazy-init} automatically set.
	 * @param beanName the bean name
	 * @param beanDefinition the bean definition
	 * @param beanType the bean type
	 * @return {@code true} if {@code lazy-init} should not be automatically set
	 */
	boolean isExcluded(String beanName, BeanDefinition beanDefinition, Class<?> beanType);

	/**
	 * Factory method that creates a filter for the given bean types.
	 * @param types the filtered types
	 * @return a new filter instance
	 */
	static LazyInitializationExcludeFilter forBeanTypes(Class<?>... types) {
		return (beanName, beanDefinition, beanType) -> {
			for (Class<?> type : types) {
				if (type.isAssignableFrom(beanType)) {
					return true;
				}
			}
			return false;
		};
	}

}
