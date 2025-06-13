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

package org.springframework.boot.context.properties;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Indexed;

/**
 * Annotation for externalized configuration. Add this to a class definition or a
 * {@code @Bean} method in a {@code @Configuration} class if you want to bind and validate
 * some external Properties (e.g. from a .properties file).
 * <p>
 * Binding is either performed by calling setters on the annotated class or, if
 * {@link ConstructorBinding @ConstructorBinding} is in use, by binding to the constructor
 * parameters.
 * <p>
 * Note that contrary to {@code @Value}, SpEL expressions are not evaluated since property
 * values are externalized.
 *
 * @author Dave Syer
 * @since 1.0.0
 * @see ConfigurationPropertiesScan
 * @see ConstructorBinding
 * @see ConfigurationPropertiesBindingPostProcessor
 * @see EnableConfigurationProperties
 */
// 用于外部化配置的注解。
// 如果您需要绑定并验证某些外部属性（例如，来自 .properties 文件的属性），
// 请将其添加到 {@code @Configuration} 类的定义或 {@code @Bean} 方法中。
//
// <p> 绑定可以通过调用带注解类的 setter 方法执行，或者，
// 如果使用了 {@link ConstructorBinding @ConstructorBinding}，则通过绑定到构造函数参数来执行。
//
// <p> 请注意，与 {@code @Value} 相反，由于属性值是外部化的，因此不会对 SpEL 表达式进行求值。
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface ConfigurationProperties {

	/**
	 * The prefix of the properties that are valid to bind to this object. Synonym for
	 * {@link #prefix()}. A valid prefix is defined by one or more words separated with
	 * dots (e.g. {@code "acme.system.feature"}).
	 * @return the prefix of the properties to bind
	 */
	// 可绑定到此对象的有效属性的前缀。
	// 与 {@link #prefix()} 同义。有效前缀由一个或多个用点分隔的单词定义（例如 {@code "acme.system.feature"}）。
	// @return 要绑定的属性的前缀
	@AliasFor("prefix")
	String value() default "";

	/**
	 * The prefix of the properties that are valid to bind to this object. Synonym for
	 * {@link #value()}. A valid prefix is defined by one or more words separated with
	 * dots (e.g. {@code "acme.system.feature"}).
	 * @return the prefix of the properties to bind
	 */
	// 可绑定到此对象的有效属性的前缀。与 {@link #value()} 同义。
	// 有效前缀由一个或多个用点分隔的单词定义（例如 {@code "acme.system.feature"}）。
	// @return 要绑定的属性的前缀
	@AliasFor("value")
	String prefix() default "";

	/**
	 * Flag to indicate that when binding to this object invalid fields should be ignored.
	 * Invalid means invalid according to the binder that is used, and usually this means
	 * fields of the wrong type (or that cannot be coerced into the correct type).
	 * @return the flag value (default false)
	 */
	// 指示在绑定到此对象时应忽略无效字段的标志。
	// 无效表示根据所使用的绑定器无效，通常这意味着字段类型错误（或无法强制转换为正确类型）。
	// @return 标志值（默认 false）
	boolean ignoreInvalidFields() default false;

	/**
	 * Flag to indicate that when binding to this object unknown fields should be ignored.
	 * An unknown field could be a sign of a mistake in the Properties.
	 * @return the flag value (default true)
	 */
	// 标志指示在绑定到此对象时应忽略未知字段。未知字段可能表示属性设置存在错误。
	// @return 标志值（默认 true）
	boolean ignoreUnknownFields() default true;

}
