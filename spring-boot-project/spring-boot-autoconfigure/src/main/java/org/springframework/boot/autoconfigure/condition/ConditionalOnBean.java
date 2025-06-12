/*
 * Copyright 2012-2024 the original author or authors.
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

package org.springframework.boot.autoconfigure.condition;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * {@link Conditional @Conditional} that only matches when beans meeting all the specified
 * requirements are already contained in the {@link BeanFactory}. All the requirements
 * must be met for the condition to match, but they do not have to be met by the same
 * bean.
 * <p>
 * When placed on a {@link Bean @Bean} method and none of {@link #value}, {@link #type},
 * or {@link #name} has been specified, the bean type to match defaults to the return type
 * of the {@code @Bean} method:
 *
 * <pre class="code">
 * &#064;Configuration
 * public class MyAutoConfiguration {
 *
 *     &#064;ConditionalOnBean
 *     &#064;Bean
 *     public MyService myService() {
 *         ...
 *     }
 *
 * }</pre>
 * <p>
 * In the sample above the condition will match if a bean of type {@code MyService} is
 * already contained in the {@link BeanFactory}.
 * <p>
 * The condition can only match the bean definitions that have been processed by the
 * application context so far and, as such, it is strongly recommended to use this
 * condition on auto-configuration classes only. If a candidate bean may be created by
 * another auto-configuration, make sure that the one using this condition runs after.
 *
 * @author Phillip Webb
 * @since 1.0.0
 */
// {@link Conditional @Conditional} 仅当 {@link BeanFactory} 中已包含满足所有指定要求的 Bean 时才会匹配。
// 条件必须满足所有要求才能匹配，但这些要求不必由同一个 Bean 满足。
//
// <p> 当放置在 {@link Bean @Bean} 方法上且未指定 {@link #value}、{@link #type} 或 {@link #name} 时，
// 要匹配的 Bean 类型默认为 {@code @Bean} 方法的返回类型：
//
// <pre class="code">
// @Configuration
// public class MyAutoConfiguration {
// 		@ConditionalOnBean
// 		@Bean
// 		public MyService myService() {
// 			...
// 		}
// }</pre>
//
// <p> 在上面的示例中，如果 {@link BeanFactory} 中已包含类型为 {@code MyService} 的 Bean，则条件将匹配。
//
// <p> 该条件只能匹配迄今为止已被应用上下文处理过的 Bean 定义，因此强烈建议仅在自动配置类中使用此条件。
// 如果候选 Bean 可能由其他自动配置创建，请确保使用此条件的 Bean 在之后运行。
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnBeanCondition.class)
public @interface ConditionalOnBean {

	/**
	 * The class types of beans that should be checked. The condition matches when beans
	 * of all classes specified are contained in the {@link BeanFactory}.
	 * @return the class types of beans to check
	 */
	// 需要检查的 bean 的类类型。当 {@link BeanFactory} 中包含所有指定类的 bean 时，条件成立。
	// @return 需要检查的 bean 的类类型
	Class<?>[] value() default {};

	/**
	 * The class type names of beans that should be checked. The condition matches when
	 * beans of all classes specified are contained in the {@link BeanFactory}.
	 * @return the class type names of beans to check
	 */
	// 需要检查的 bean 的类类型名称。当 {@link BeanFactory} 中包含所有指定类的 bean 时，条件成立。
	// @return 需要检查的 bean 的类类型名称
	String[] type() default {};

	/**
	 * The annotation type decorating a bean that should be checked. The condition matches
	 * when all the annotations specified are defined on beans in the {@link BeanFactory}.
	 * @return the class-level annotation types to check
	 */
	// 装饰需要检查的 bean 的注解类型。当 {@link BeanFactory} 中的 bean 上定义了所有指定的注解时，条件成立。
	// @return 需要检查的类级别注解类型
	Class<? extends Annotation>[] annotation() default {};

	/**
	 * The names of beans to check. The condition matches when all the bean names
	 * specified are contained in the {@link BeanFactory}.
	 * @return the names of beans to check
	 */
	// 需要检查的 bean 的名称。当所有指定的 Bean 名称都包含在 {@link BeanFactory} 中时，条件匹配。
	// @return 待检查 Bean 的名称
	String[] name() default {};

	/**
	 * Strategy to decide if the application context hierarchy (parent contexts) should be
	 * considered.
	 * @return the search strategy
	 */
	// 用于决定是否应考虑应用上下文层次结构（父上下文）的策略。
	// @return 搜索策略
	SearchStrategy search() default SearchStrategy.ALL;

	/**
	 * Additional classes that may contain the specified bean types within their generic
	 * parameters. For example, an annotation declaring {@code value=Name.class} and
	 * {@code parameterizedContainer=NameRegistration.class} would detect both
	 * {@code Name} and {@code NameRegistration<Name>}.
	 * @return the container types
	 * @since 2.1.0
	 */
	// 可能在其泛型参数中包含指定 Bean 类型的附加类。例如，声明 {@code value=Name.class} 和
	// {@code parameterizedContainer=NameRegistration.class} 的注解将同时检测 {@code Name} 和 {@code NameRegistration<Name>}。
	// @return 容器类型
	Class<?>[] parameterizedContainer() default {};

}
