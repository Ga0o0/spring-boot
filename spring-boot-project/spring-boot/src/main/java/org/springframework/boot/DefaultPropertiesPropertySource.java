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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.CollectionUtils;

/**
 * {@link MapPropertySource} containing default properties contributed directly to a
 * {@code SpringApplication}. By convention, the {@link DefaultPropertiesPropertySource}
 * is always the last property source in the {@link Environment}.
 *
 * @author Phillip Webb
 * @since 2.4.0
 */
// {@link MapPropertySource} 包含直接贡献给 {@code SpringApplication} 的默认属性。
// 按照惯例，{@link DefaultPropertiesPropertySource} 始终是 {@link Environment} 中的最后一个属性源。
public class DefaultPropertiesPropertySource extends MapPropertySource {

	/**
	 * The name of the 'default properties' property source.
	 */
	// “默认属性” 属性源的名称。
	public static final String NAME = "defaultProperties";

	/**
	 * Create a new {@link DefaultPropertiesPropertySource} with the given {@code Map}
	 * source.
	 * @param source the source map
	 */
	// 使用给定的 {@code Map} 源创建一个新的 {@link DefaultPropertiesPropertySource}。
	// @param source 源映射
	public DefaultPropertiesPropertySource(Map<String, Object> source) {
		super(NAME, source);
	}

	/**
	 * Return {@code true} if the given source is named 'defaultProperties'.
	 * @param propertySource the property source to check
	 * @return {@code true} if the name matches
	 */
	// 如果给定的源名为“defaultProperties”，则返回 {@code true}。
	// @param propertySource 要检查的属性源
	// @return 如果名称匹配，则返回 {@code true}。
	public static boolean hasMatchingName(PropertySource<?> propertySource) {
		return (propertySource != null) && propertySource.getName().equals(NAME);
	}

	/**
	 * Create a new {@link DefaultPropertiesPropertySource} instance if the provided
	 * source is not empty.
	 * @param source the {@code Map} source
	 * @param action the action used to consume the
	 * {@link DefaultPropertiesPropertySource}
	 */
	// 如果提供的源不为空，则创建一个新的 {@link DefaultPropertiesPropertySource} 实例。
	// @param source {@code Map} 源
	// @param action 用于使用 {@link DefaultPropertiesPropertySource} 的操作
	public static void ifNotEmpty(Map<String, Object> source, Consumer<DefaultPropertiesPropertySource> action) {
		if (!CollectionUtils.isEmpty(source) && action != null) {
			action.accept(new DefaultPropertiesPropertySource(source));
		}
	}

	/**
	 * Add a new {@link DefaultPropertiesPropertySource} or merge with an existing one.
	 * @param source the {@code Map} source
	 * @param sources the existing sources
	 * @since 2.4.4
	 */
	// 添加新的 {@link DefaultPropertiesPropertySource} 或与现有源合并。
	// @param source {@code Map} 源
	// @param sources 现有源
	public static void addOrMerge(Map<String, Object> source, MutablePropertySources sources) {
		if (!CollectionUtils.isEmpty(source)) {
			Map<String, Object> resultingSource = new HashMap<>();
			DefaultPropertiesPropertySource propertySource = new DefaultPropertiesPropertySource(resultingSource);
			// NAME = "defaultProperties"
			if (sources.contains(NAME)) {
				// 合并 source 和 sources 到 resultingSource
				mergeIfPossible(source, sources, resultingSource);
				// 用给定的属性源对象替换具有给定名称的属性源。
				sources.replace(NAME, propertySource);
			}
			else {
				resultingSource.putAll(source);
				// 添加具有最低优先级的给定属性源对象。
				sources.addLast(propertySource);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void mergeIfPossible(Map<String, Object> source, MutablePropertySources sources,
			Map<String, Object> resultingSource) {
		PropertySource<?> existingSource = sources.get(NAME);
		if (existingSource != null) {
			Object underlyingSource = existingSource.getSource();
			if (underlyingSource instanceof Map) {
				resultingSource.putAll((Map<String, Object>) underlyingSource);
			}
			resultingSource.putAll(source);
		}
	}

	/**
	 * Move the 'defaultProperties' property source so that it's the last source in the
	 * given {@link ConfigurableEnvironment}.
	 * @param environment the environment to update
	 */
	// 移动 “defaultProperties” 属性源，使其成为给定 {@link ConfigurableEnvironment} 中的最后一个源。
	// @param environment 要更新的环境
	public static void moveToEnd(ConfigurableEnvironment environment) {
		moveToEnd(environment.getPropertySources());
	}

	/**
	 * Move the 'defaultProperties' property source so that it's the last source in the
	 * given {@link MutablePropertySources}.
	 * @param propertySources the property sources to update
	 */
	// 移动 “defaultProperties” 属性源，使其成为给定 {@link MutablePropertySources} 中的最后一个源。
	// @param propertySources 要更新的属性源
	public static void moveToEnd(MutablePropertySources propertySources) {
		// NAME = "defaultProperties"
		PropertySource<?> propertySource = propertySources.remove(NAME);
		if (propertySource != null) {
			propertySources.addLast(propertySource);
		}
	}

}
