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

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Enable auto-configuration of the Spring Application Context, attempting to guess and
 * configure beans that you are likely to need. Auto-configuration classes are usually
 * applied based on your classpath and what beans you have defined. For example, if you
 * have {@code tomcat-embedded.jar} on your classpath you are likely to want a
 * {@link TomcatServletWebServerFactory} (unless you have defined your own
 * {@link ServletWebServerFactory} bean).
 * <p>
 * When using {@link SpringBootApplication @SpringBootApplication}, the auto-configuration
 * of the context is automatically enabled and adding this annotation has therefore no
 * additional effect.
 * <p>
 * Auto-configuration tries to be as intelligent as possible and will back-away as you
 * define more of your own configuration. You can always manually {@link #exclude()} any
 * configuration that you never want to apply (use {@link #excludeName()} if you don't
 * have access to them). You can also exclude them through the
 * {@code spring.autoconfigure.exclude} property. Auto-configuration is always applied
 * after user-defined beans have been registered.
 * <p>
 * The package of the class that is annotated with {@code @EnableAutoConfiguration},
 * usually through {@code @SpringBootApplication}, has specific significance and is often
 * used as a 'default'. For example, it will be used when scanning for {@code @Entity}
 * classes. It is generally recommended that you place {@code @EnableAutoConfiguration}
 * (if you're not using {@code @SpringBootApplication}) in a root package so that all
 * sub-packages and classes can be searched.
 * <p>
 * Auto-configuration classes are regular Spring {@link Configuration @Configuration}
 * beans. They are located using {@link ImportCandidates}. Generally auto-configuration
 * beans are {@link Conditional @Conditional} beans (most often using
 * {@link ConditionalOnClass @ConditionalOnClass} and
 * {@link ConditionalOnMissingBean @ConditionalOnMissingBean} annotations).
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 * @since 1.0.0
 * @see ConditionalOnBean
 * @see ConditionalOnMissingBean
 * @see ConditionalOnClass
 * @see AutoConfigureAfter
 * @see SpringBootApplication
 */
// 启用 Spring 应用程序上下文的自动配置功能，尝试猜测并配置您可能需要的 bean。自动配置类通常根据您的类路径和您定义的 bean 来应用。
// 例如，如果您的类路径中有 {@code tomcat-embedded.jar}，您很可能需要一个 {@link TomcatServletWebServerFactory}
// （除非您定义了自己的 {@link ServletWebServerFactory} bean）。
//
// <p> 使用 {@link SpringBootApplication @SpringBootApplication} 时，上下文的自动配置会自动启用，因此添加此注解不会产生任何额外影响。
//
// <p> 自动配置会尽可能地智能，并且会随着您定义更多自己的配置而逐渐减弱。您可以随时手动 {@link #exclude()} 任何您不想应用的配置
// （如果您无权访问它们，请使用 {@link #excludeName()}）。您也可以通过 {@code spring.autoconfigure.exclude} 属性将它们排除在外。
// 自动配置始终在用户定义的 bean 注册后应用。
//
// <p> 使用 {@code @EnableAutoConfiguration} 注解的类的包（通常通过 {@code @SpringBootApplication} 进行）具有特定含义，
// 并且经常用作“默认”包。例如，在扫描 {@code @Entity} 类时将使用它。通常建议将 {@code @EnableAutoConfiguration}
// （如果您未使用 {@code @SpringBootApplication}）放置在根包中，以便可以搜索所有子包和类。
//
// <p> 自动配置类是常规的 Spring {@link Configuration @Configuration} bean。它们使用 {@link ImportCandidates} 进行定位。
// 通常，自动配置 bean 是 {@link Conditional @Conditional} bean（最常使用 {@link ConditionalOnClass @ConditionalOnClass} 和
// {@link ConditionalOnMissingBean @ConditionalOnMissingBean} 注解）。
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {

	/**
	 * Environment property that can be used to override when auto-configuration is
	 * enabled.
	 */
	// 启用自动配置后可用于覆盖的环境属性。
	String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

	/**
	 * Exclude specific auto-configuration classes such that they will never be applied.
	 * @return the classes to exclude
	 */
	// 排除特定的自动配置类，使其永远不会被应用。
	// @return 要排除的类
	Class<?>[] exclude() default {};

	/**
	 * Exclude specific auto-configuration class names such that they will never be
	 * applied.
	 * @return the class names to exclude
	 * @since 1.3.0
	 */
	// 排除特定的自动配置类名，使其永远不会被应用。
	// @return 要排除的类名
	String[] excludeName() default {};

}
