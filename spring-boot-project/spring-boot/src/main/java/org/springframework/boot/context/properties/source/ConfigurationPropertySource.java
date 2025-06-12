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

package org.springframework.boot.context.properties.source;

import java.util.function.Predicate;

import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

/**
 * A source of {@link ConfigurationProperty ConfigurationProperties}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 * @since 2.0.0
 * @see ConfigurationPropertyName
 * @see OriginTrackedValue
 * @see #getConfigurationProperty(ConfigurationPropertyName)
 */
// {@link ConfigurationProperty ConfigurationProperties} 的来源。
@FunctionalInterface
public interface ConfigurationPropertySource {

	/**
	 * Return a single {@link ConfigurationProperty} from the source or {@code null} if no
	 * property can be found.
	 * @param name the name of the property (must not be {@code null})
	 * @return the associated object or {@code null}.
	 */
	// 从源返回单个 {@link ConfigurationProperty}，如果未找到任何属性，则返回 {@code null}。
	// @param name 属性名称（不得为 {@code null}）
	// @return 关联对象或 {@code null}。
	ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name);

	/**
	 * Returns if the source contains any descendants of the specified name. May return
	 * {@link ConfigurationPropertyState#PRESENT} or
	 * {@link ConfigurationPropertyState#ABSENT} if an answer can be determined or
	 * {@link ConfigurationPropertyState#UNKNOWN} if it's not possible to determine a
	 * definitive answer.
	 * @param name the name to check
	 * @return if the source contains any descendants
	 */
	// 如果源包含指定名称的任何后代，则返回结果。如果可以确定答案，则可能返回 {@link ConfigurationPropertyState#PRESENT} 或
	// {@link ConfigurationPropertyState#ABSENT}；如果无法确定最终答案，则可能返回 {@link ConfigurationPropertyState#UNKNOWN}。
	// @param name 要检查的名称
	// @return 如果源包含任何后代，则返回结果
	default ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
		return ConfigurationPropertyState.UNKNOWN;
	}

	/**
	 * Return a filtered variant of this source, containing only names that match the
	 * given {@link Predicate}.
	 * @param filter the filter to match
	 * @return a filtered {@link ConfigurationPropertySource} instance
	 */
	// 返回此源的过滤后变体，仅包含与给定 {@link Predicate} 匹配的名称。
	// @param filter 要匹配的过滤器
	// @return 一个过滤后的 {@link ConfigurationPropertySource} 实例
	default ConfigurationPropertySource filter(Predicate<ConfigurationPropertyName> filter) {
		return new FilteredConfigurationPropertiesSource(this, filter);
	}

	/**
	 * Return a variant of this source that supports name aliases.
	 * @param aliases a function that returns a stream of aliases for any given name
	 * @return a {@link ConfigurationPropertySource} instance supporting name aliases
	 */
	// 返回此源的支持名称别名的变体。
	// @param aliases 一个函数，返回给定名称的别名流
	// @return 一个支持名称别名的 {@link ConfigurationPropertySource} 实例
	default ConfigurationPropertySource withAliases(ConfigurationPropertyNameAliases aliases) {
		return new AliasedConfigurationPropertySource(this, aliases);
	}

	/**
	 * Return a variant of this source that supports a prefix.
	 * @param prefix the prefix for properties in the source
	 * @return a {@link ConfigurationPropertySource} instance supporting a prefix
	 * @since 2.5.0
	 */
	// 返回此源的支持前缀的变体。
	// @param prefix 源中属性的前缀
	// @return 一个支持前缀的 {@link ConfigurationPropertySource} 实例
	default ConfigurationPropertySource withPrefix(String prefix) {
		return (StringUtils.hasText(prefix)) ? new PrefixedConfigurationPropertySource(this, prefix) : this;
	}

	/**
	 * Return the underlying source that is actually providing the properties.
	 * @return the underlying property source or {@code null}.
	 */
	// 返回实际提供属性的底层源。
	// @return 底层属性源或 {@code null}。
	default Object getUnderlyingSource() {
		return null;
	}

	/**
	 * Return a single new {@link ConfigurationPropertySource} adapted from the given
	 * Spring {@link PropertySource} or {@code null} if the source cannot be adapted.
	 * @param source the Spring property source to adapt
	 * @return an adapted source or {@code null} {@link SpringConfigurationPropertySource}
	 * @since 2.4.0
	 */
	// 返回一个从给定的 Spring {@link PropertySource} 适配而来的新 {@link ConfigurationPropertySource}；如果源无法适配，则返回 {@code null}。
	// @param source 需要适配的 Spring 属性源
	// @return 已适配的源或 {@code null} {@link SpringConfigurationPropertySource}
	static ConfigurationPropertySource from(PropertySource<?> source) {
		if (source instanceof ConfigurationPropertySourcesPropertySource) {
			return null;
		}
		return SpringConfigurationPropertySource.from(source);
	}

}
