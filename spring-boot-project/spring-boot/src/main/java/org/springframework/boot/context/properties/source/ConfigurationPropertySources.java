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

package org.springframework.boot.context.properties.source;

import java.util.Collections;
import java.util.stream.Stream;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySource.StubPropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.Assert;

/**
 * Provides access to {@link ConfigurationPropertySource ConfigurationPropertySources}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
// 提供对 {@link ConfigurationPropertySource ConfigurationPropertySources} 的访问。
public final class ConfigurationPropertySources {

	/**
	 * The name of the {@link PropertySource} {@link #attach(Environment) adapter}.
	 */
	// {@link PropertySource} {@link #attach(Environment) 适配器} 的名称。
	private static final String ATTACHED_PROPERTY_SOURCE_NAME = "configurationProperties";

	private ConfigurationPropertySources() {
	}

	/**
	 * Create a new {@link PropertyResolver} that resolves property values against an
	 * underlying set of {@link PropertySources}. Provides an
	 * {@link ConfigurationPropertySource} aware and optimized alternative to
	 * {@link PropertySourcesPropertyResolver}.
	 * @param propertySources the set of {@link PropertySource} objects to use
	 * @return a {@link ConfigurablePropertyResolver} implementation
	 * @since 2.5.0
	 */
	// 创建一个新的 {@link PropertyResolver}，用于根据底层的 {@link PropertySources} 集合解析属性值。
	// 提供一个可感知 {@link ConfigurationPropertySource} 并经过优化的 {@link PropertySourcesPropertyResolver} 替代方案。
	// @param propertySources 要使用的 {@link PropertySource} 对象集合
	// @return 一个 {@link ConfigurablePropertyResolver} 实现
	public static ConfigurablePropertyResolver createPropertyResolver(MutablePropertySources propertySources) {
		return new ConfigurationPropertySourcesPropertyResolver(propertySources);
	}

	/**
	 * Determines if the specific {@link PropertySource} is the
	 * {@link ConfigurationPropertySource} that was {@link #attach(Environment) attached}
	 * to the {@link Environment}.
	 * @param propertySource the property source to test
	 * @return {@code true} if this is the attached {@link ConfigurationPropertySource}
	 */
	public static boolean isAttachedConfigurationPropertySource(PropertySource<?> propertySource) {
		return ATTACHED_PROPERTY_SOURCE_NAME.equals(propertySource.getName());
	}

	/**
	 * Attach a {@link ConfigurationPropertySource} support to the specified
	 * {@link Environment}. Adapts each {@link PropertySource} managed by the environment
	 * to a {@link ConfigurationPropertySource} and allows classic
	 * {@link PropertySourcesPropertyResolver} calls to resolve using
	 * {@link ConfigurationPropertyName configuration property names}.
	 * <p>
	 * The attached resolver will dynamically track any additions or removals from the
	 * underlying {@link Environment} property sources.
	 * @param environment the source environment (must be an instance of
	 * {@link ConfigurableEnvironment})
	 * @see #get(Environment)
	 */
	// 将 {@link ConfigurationPropertySource} 支持附加到指定的 {@link Environment}。
	// 将环境管理的每个 {@link PropertySource} 适配到 {@link ConfigurationPropertySource}，
	// 并允许使用 {@link ConfigurationPropertyName 配置属性名称} 进行经典的 {@link PropertySourcesPropertyResolver} 调用解析。
	// <p> 附加的解析器将动态跟踪底层 {@link Environment} 属性源的任何添加或删除。
	// @param environment 源环境（必须是 {@link ConfigurableEnvironment} 的一个实例）
	public static void attach(Environment environment) {
		// 断言提供的对象是所提供类的一个实例。
		Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
		MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();
		PropertySource<?> attached = getAttached(sources); // 获取附加信息
		if (!isUsingSources(attached, sources)) { // attached 是否是正在使用 Sources
			attached = new ConfigurationPropertySourcesPropertySource(ATTACHED_PROPERTY_SOURCE_NAME,
					new SpringConfigurationPropertySources(sources));
		}
		sources.remove(ATTACHED_PROPERTY_SOURCE_NAME);
		sources.addFirst(attached);
	}

	private static boolean isUsingSources(PropertySource<?> attached, MutablePropertySources sources) {
		return attached instanceof ConfigurationPropertySourcesPropertySource
				&& ((SpringConfigurationPropertySources) attached.getSource()).isUsingSources(sources);
	}

	static PropertySource<?> getAttached(MutablePropertySources sources) {
		// ATTACHED_PROPERTY_SOURCE_NAME = "configurationProperties"
		return (sources != null) ? sources.get(ATTACHED_PROPERTY_SOURCE_NAME) : null;
	}

	/**
	 * Return a set of {@link ConfigurationPropertySource} instances that have previously
	 * been {@link #attach(Environment) attached} to the {@link Environment}.
	 * @param environment the source environment (must be an instance of
	 * {@link ConfigurableEnvironment})
	 * @return an iterable set of configuration property sources
	 * @throws IllegalStateException if not configuration property sources have been
	 * attached
	 */
	// 返回一组先前已 {@link #attach(Environment) 附加到 {@link Environment} 的 {@link ConfigurationPropertySource} 实例。
	// @param environment 源环境（必须是 {@link ConfigurableEnvironment} 的实例）
	// @return 一组可迭代的配置属性源
	// 如果尚未附加配置属性源，则抛出 IllegalStateException
	public static Iterable<ConfigurationPropertySource> get(Environment environment) {
		Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
		MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();
		// ATTACHED_PROPERTY_SOURCE_NAME = "configurationProperties"
		ConfigurationPropertySourcesPropertySource attached = (ConfigurationPropertySourcesPropertySource) sources
			.get(ATTACHED_PROPERTY_SOURCE_NAME);
		if (attached == null) {
			return from(sources);
		}
		return attached.getSource();
	}

	/**
	 * Return {@link Iterable} containing a single new {@link ConfigurationPropertySource}
	 * adapted from the given Spring {@link PropertySource}.
	 * @param source the Spring property source to adapt
	 * @return an {@link Iterable} containing a single newly adapted
	 * {@link SpringConfigurationPropertySource}
	 */
	public static Iterable<ConfigurationPropertySource> from(PropertySource<?> source) {
		return Collections.singleton(ConfigurationPropertySource.from(source));
	}

	/**
	 * Return {@link Iterable} containing new {@link ConfigurationPropertySource}
	 * instances adapted from the given Spring {@link PropertySource PropertySources}.
	 * <p>
	 * This method will flatten any nested property sources and will filter all
	 * {@link StubPropertySource stub property sources}. Updates to the underlying source,
	 * identified by changes in the sources returned by its iterator, will be
	 * automatically tracked. The underlying source should be thread safe, for example a
	 * {@link MutablePropertySources}
	 * @param sources the Spring property sources to adapt
	 * @return an {@link Iterable} containing newly adapted
	 * {@link SpringConfigurationPropertySource} instances
	 */
	// 返回 {@link Iterable}，其中包含从给定 Spring {@link PropertySource PropertySources} 适配的新 {@link ConfigurationPropertySource} 实例。
	// <p>
	// 此方法将展平所有嵌套的属性源，并过滤所有 {@link StubPropertySource 存根属性源}。底层源的更新（由其迭代器返回的源的更改标识）将被自动跟踪。
	// 底层源应该是线程安全的，例如 {@link MutablePropertySources}
	// @param sources 需要适配的 Spring 属性源
	// @return 一个 {@link Iterable}，其中包含新适配的 {@link SpringConfigurationPropertySource} 实例
	public static Iterable<ConfigurationPropertySource> from(Iterable<PropertySource<?>> sources) {
		return new SpringConfigurationPropertySources(sources);
	}

	private static Stream<PropertySource<?>> streamPropertySources(PropertySources sources) {
		return sources.stream()
			.flatMap(ConfigurationPropertySources::flatten)
			.filter(ConfigurationPropertySources::isIncluded);
	}

	private static Stream<PropertySource<?>> flatten(PropertySource<?> source) {
		if (source.getSource() instanceof ConfigurableEnvironment configurableEnvironment) {
			return streamPropertySources(configurableEnvironment.getPropertySources());
		}
		return Stream.of(source);
	}

	private static boolean isIncluded(PropertySource<?> source) {
		return !(source instanceof StubPropertySource)
				&& !(source instanceof ConfigurationPropertySourcesPropertySource);
	}

}
