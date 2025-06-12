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

package org.springframework.boot.autoconfigure;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.repository.Repository;

/**
 * Indicates a {@link Configuration configuration} class that declares one or more
 * {@link Bean @Bean} methods and also triggers {@link EnableAutoConfiguration
 * auto-configuration} and {@link ComponentScan component scanning}. This is a convenience
 * annotation that is equivalent to declaring {@code @SpringBootConfiguration},
 * {@code @EnableAutoConfiguration} and {@code @ComponentScan}.
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 * @since 1.2.0
 */
// 指示一个 {@link Configuration} 配置类，该类声明一个或多个 {@link Bean @Bean} 方法，
// 并触发 {@link EnableAutoConfiguration 自动配置} 和 {@link ComponentScan 组件扫描}。
// 这是一个便捷注解，相当于声明 {@code @SpringBootConfiguration}、{@code @EnableAutoConfiguration} 和 {@code @ComponentScan}。
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {

	/**
	 * Exclude specific auto-configuration classes such that they will never be applied.
	 * @return the classes to exclude
	 */
	// 排除特定的自动配置类，使它们永远不会被应用。
	// @return 要排除的类
	@AliasFor(annotation = EnableAutoConfiguration.class)
	Class<?>[] exclude() default {};

	/**
	 * Exclude specific auto-configuration class names such that they will never be
	 * applied.
	 * @return the class names to exclude
	 * @since 1.3.0
	 */
	// 排除特定的自动配置类名，使其永远不会被应用。
	// @return 要排除的类名
	@AliasFor(annotation = EnableAutoConfiguration.class)
	String[] excludeName() default {};

	/**
	 * Base packages to scan for annotated components. Use {@link #scanBasePackageClasses}
	 * for a type-safe alternative to String-based package names.
	 * <p>
	 * <strong>Note:</strong> this setting is an alias for
	 * {@link ComponentScan @ComponentScan} only. It has no effect on {@code @Entity}
	 * scanning or Spring Data {@link Repository} scanning. For those you should add
	 * {@link org.springframework.boot.autoconfigure.domain.EntityScan @EntityScan} and
	 * {@code @Enable...Repositories} annotations.
	 * @return base packages to scan
	 * @since 1.3.0
	 */
	// 用于扫描带注解组件的基础包。
	// 使用 {@link #scanBasePackageClasses} 可作为基于字符串的包名的类型安全替代方案。
	// <p> <strong>注意：</strong>此设置仅为 {@link ComponentScan @ComponentScan} 的别名。
	// 它对 {@code @Entity} 扫描或 Spring Data {@link Repository} 扫描无效。
	// 对于这些扫描，您应该添加 {@link org.springframework.boot.autoconfigure.domain.EntityScan @EntityScan} 和
	// {@code @Enable...Repositories} 注解。
	// @return 用于扫描的基础包
	@AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
	String[] scanBasePackages() default {};

	/**
	 * Type-safe alternative to {@link #scanBasePackages} for specifying the packages to
	 * scan for annotated components. The package of each class specified will be scanned.
	 * <p>
	 * Consider creating a special no-op marker class or interface in each package that
	 * serves no purpose other than being referenced by this attribute.
	 * <p>
	 * <strong>Note:</strong> this setting is an alias for
	 * {@link ComponentScan @ComponentScan} only. It has no effect on {@code @Entity}
	 * scanning or Spring Data {@link Repository} scanning. For those you should add
	 * {@link org.springframework.boot.autoconfigure.domain.EntityScan @EntityScan} and
	 * {@code @Enable...Repositories} annotations.
	 * @return base packages to scan
	 * @since 1.3.0
	 */
	// 这是 {@link #scanBasePackages} 的类型安全替代方案，用于指定要扫描带注释组件的软件包。将扫描指定的每个类的软件包。
	// <p> 考虑在每个软件包中创建一个特殊的无操作标记类或接口，除了被此属性引用外，没有其他用途。
	// <p> <strong>注意：</strong>此设置仅为 {@link ComponentScan @ComponentScan} 的别名。
	// 它对 {@code @Entity} 扫描或 Spring Data {@link Repository} 扫描无效。
	// 对于这些，您应该添加 {@link org.springframework.boot.autoconfigure.domain.EntityScan @EntityScan} 和
	// {@code @Enable...Repositories} 注释。
	// @return 要扫描的基础软件包
	@AliasFor(annotation = ComponentScan.class, attribute = "basePackageClasses")
	Class<?>[] scanBasePackageClasses() default {};

	/**
	 * The {@link BeanNameGenerator} class to be used for naming detected components
	 * within the Spring container.
	 * <p>
	 * The default value of the {@link BeanNameGenerator} interface itself indicates that
	 * the scanner used to process this {@code @SpringBootApplication} annotation should
	 * use its inherited bean name generator, e.g. the default
	 * {@link AnnotationBeanNameGenerator} or any custom instance supplied to the
	 * application context at bootstrap time.
	 * @return {@link BeanNameGenerator} to use
	 * @see SpringApplication#setBeanNameGenerator(BeanNameGenerator)
	 * @since 2.3.0
	 */
	// {@link BeanNameGenerator} 类用于在 Spring 容器中命名检测到的组件。
	// <p> {@link BeanNameGenerator} 接口本身的默认值表示用于处理此 {@code @SpringBootApplication}
	// 注释的扫描器应使用其继承的 bean 名称生成器，例如默认的 {@link AnnotationBeanNameGenerator} 或在引导时提供给应用程序上下文的任何自定义实例。
	// @return {@link BeanNameGenerator} 使用
	@AliasFor(annotation = ComponentScan.class, attribute = "nameGenerator")
	Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

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
	 * @since 2.2
	 * @return whether to proxy {@code @Bean} methods
	 */
	// 指定是否应代理 {@link Bean @Bean} 方法以强制执行 Bean 生命周期行为，
	// 例如，即使在用户代码中直接调用 {@code @Bean} 方法，也返回共享的单例 Bean 实例。
	// 此功能需要方法拦截，通过运行时生成的 CGLIB 子类实现，但这存在一些限制，例如配置类及其方法不允许声明 {@code final}。
	// <p> 默认值为 {@code true}，允许在配置类中进行“Bean 间引用”以及对此配置的 {@code @Bean} 方法的外部调用，例如从另一个配置类调用。
	// 如果由于此特定配置的每个 {@code @Bean} 方法都是自包含的并且设计为供容器使用的普通工厂方法而不需要这样做，
	// 请将此标志切换为 {@code false} 以避免 CGLIB 子类处理。
	// <p> 关闭 Bean 方法拦截可以有效地单独处理 {@code @Bean} 方法，就像在非 {@code @Configuration} 类上声明一样，
	// 也称为“@Bean 精简模式”（参见 {@link Bean @Bean 的 javadoc}）。
	// 因此，它在行为上等同于删除 {@code @Configuration} 构造型。
	// @since 2.2
	// @return 是否代理 {@code @Bean} 方法
	@AliasFor(annotation = Configuration.class)
	boolean proxyBeanMethods() default true;

}
