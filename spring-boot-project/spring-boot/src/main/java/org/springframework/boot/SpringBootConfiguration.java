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

package org.springframework.boot;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Indexed;

/**
 * Indicates that a class provides Spring Boot application
 * {@link Configuration @Configuration}. Can be used as an alternative to the Spring's
 * standard {@code @Configuration} annotation so that configuration can be found
 * automatically (for example in tests).
 * <p>
 * Application should only ever include <em>one</em> {@code @SpringBootConfiguration} and
 * most idiomatic Spring Boot applications will inherit it from
 * {@code @SpringBootApplication}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @since 1.4.0
 */
// 表示某个类提供了 Spring Boot 应用的 {@link Configuration @Configuration}。
// 可以替代 Spring 的标准 {@code @Configuration} 注解，以便自动找到配置（例如在测试中）。
//
// <p> 应用程序应该只包含一个 {@code @SpringBootConfiguration}，并且大多数惯用的
// Spring Boot 应用程序都会从 {@code @SpringBootApplication} 继承它。
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Indexed
public @interface SpringBootConfiguration {

	/**
	 * Specify whether {@link Bean @Bean} methods should get proxied in order to enforce
	 * bean lifecycle behavior, e.g. to return shared singleton bean instances even in
	 * case of direct {@code @Bean} method calls in user code. This feature requires
	 * method interception, implemented through a runtime-generated CGLIB subclass which
	 * comes with limitations such as the configuration class and its methods not being
	 * allowed to declare {@code final}.
	 * <p>
	 * The default is {@code true}, allowing for 'inter-bean references' within the
	 * configuration class as well as for external calls to this configuration's
	 * {@code @Bean} methods, e.g. from another configuration class. If this is not needed
	 * since each of this particular configuration's {@code @Bean} methods is
	 * self-contained and designed as a plain factory method for container use, switch
	 * this flag to {@code false} in order to avoid CGLIB subclass processing.
	 * <p>
	 * Turning off bean method interception effectively processes {@code @Bean} methods
	 * individually like when declared on non-{@code @Configuration} classes, a.k.a.
	 * "@Bean Lite Mode" (see {@link Bean @Bean's javadoc}). It is therefore behaviorally
	 * equivalent to removing the {@code @Configuration} stereotype.
	 * @return whether to proxy {@code @Bean} methods
	 * @since 2.2
	 */
	// 指定是否应代理 {@link Bean @Bean} 方法以强制执行 Bean 生命周期行为，例如，即使在用户代码中直接调用 {@code @Bean} 方法，也返回共享的单例 Bean 实例。
	// 此功能需要方法拦截，通过运行时生成的 CGLIB 子类实现，但这存在一些限制，例如配置类及其方法不允许声明 {@code final}。
	// <p>
	// 默认值为 {@code true}，允许在配置类中进行“Bean 间引用”以及对此配置的 {@code @Bean} 方法的外部调用，例如从另一个配置类调用。
	// 如果由于此特定配置的每个 {@code @Bean} 方法都是自包含的并且设计为供容器使用的普通工厂方法而不需要这样做，
	// 请将此标志切换为 {@code false} 以避免 CGLIB 子类处理。
	// <p>
	// 关闭 Bean 方法拦截可以有效地单独处理 {@code @Bean} 方法，就像在非 {@code @Configuration} 类上声明一样，
	// 也称为“@Bean Lite 模式”（参见 {@link Bean @Bean 的 javadoc}）。因此，它在行为上等同于删除 {@code @Configuration} 构造型。
	// @return 是否代理 {@code @Bean} 方法
	@AliasFor(annotation = Configuration.class)
	boolean proxyBeanMethods() default true;

}
