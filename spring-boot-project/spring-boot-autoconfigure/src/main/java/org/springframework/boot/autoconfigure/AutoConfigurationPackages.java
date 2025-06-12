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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.annotation.DeterminableImports;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Class for storing auto-configuration packages for reference later (e.g. by JPA entity
 * scanner).
 *
 * @author Phillip Webb
 * @author Dave Syer
 * @author Oliver Gierke
 * @since 1.0.0
 */
// 用于存储自动配置包以供以后参考的类（例如通过 JPA 实体扫描器）。
public abstract class AutoConfigurationPackages {

	private static final Log logger = LogFactory.getLog(AutoConfigurationPackages.class);

	private static final String BEAN = AutoConfigurationPackages.class.getName();

	/**
	 * Determine if the auto-configuration base packages for the given bean factory are
	 * available.
	 * @param beanFactory the source bean factory
	 * @return true if there are auto-config packages available
	 */
	// 判断给定 bean 工厂的自动配置基础包是否可用。
	// @param beanFactory 源 bean 工厂
	// @return 如果有可用的自动配置包，则返回 true
	public static boolean has(BeanFactory beanFactory) {
		return beanFactory.containsBean(BEAN) && !get(beanFactory).isEmpty();
	}

	/**
	 * Return the auto-configuration base packages for the given bean factory.
	 * @param beanFactory the source bean factory
	 * @return a list of auto-configuration packages
	 * @throws IllegalStateException if auto-configuration is not enabled
	 */
	// 返回给定 bean 工厂的自动配置基础包。
	// @param beanFactory 源 bean 工厂
	// @return 自动配置包列表
	// 如果未启用自动配置，则抛出 IllegalStateException
	public static List<String> get(BeanFactory beanFactory) {
		try {
			return beanFactory.getBean(BEAN, BasePackages.class).get();
		}
		catch (NoSuchBeanDefinitionException ex) {
			throw new IllegalStateException("Unable to retrieve @EnableAutoConfiguration base packages");
		}
	}

	/**
	 * Programmatically registers the auto-configuration package names. Subsequent
	 * invocations will add the given package names to those that have already been
	 * registered. You can use this method to manually define the base packages that will
	 * be used for a given {@link BeanDefinitionRegistry}. Generally it's recommended that
	 * you don't call this method directly, but instead rely on the default convention
	 * where the package name is set from your {@code @EnableAutoConfiguration}
	 * configuration class or classes.
	 * @param registry the bean definition registry
	 * @param packageNames the package names to set
	 */
	// 以编程方式注册自动配置包名称。后续调用会将指定的包名称添加到已注册的包名称中。
	// 您可以使用此方法手动定义将用于给定 {@link BeanDefinitionRegistry} 的基础包。
	// 通常建议您不要直接调用此方法，而是依赖默认约定，即从您的 {@code @EnableAutoConfiguration} 配置类中设置包名称。
	// @param registry bean 定义注册表 
	// @param packageNames 需要设置的包名称
	public static void register(BeanDefinitionRegistry registry, String... packageNames) {
		// BEAN = AutoConfigurationPackages.class.getName()
		if (registry.containsBeanDefinition(BEAN)) {
			addBasePackages(registry.getBeanDefinition(BEAN), packageNames);
		}
		else {
			RootBeanDefinition beanDefinition = new RootBeanDefinition(BasePackages.class);
			beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			// 合并已存在的 Packages 和 packageNames，并设置给 BasePackages 的构造函数参数
			addBasePackages(beanDefinition, packageNames);
			// register AutoConfigurationPackages.class.getName(): BasePackages BeanDefinition
			registry.registerBeanDefinition(BEAN, beanDefinition);
		}
	}

	private static void addBasePackages(BeanDefinition beanDefinition, String[] additionalBasePackages) {
		ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
		if (constructorArgumentValues.hasIndexedArgumentValue(0)) {
			String[] existingPackages = (String[]) constructorArgumentValues.getIndexedArgumentValue(0, String[].class)
				.getValue();
			// concat existingPackages and additionalBasePackages
			constructorArgumentValues.addIndexedArgumentValue(0,
					Stream.concat(Stream.of(existingPackages), Stream.of(additionalBasePackages))
						.distinct()
						.toArray(String[]::new));
		}
		else {
			constructorArgumentValues.addIndexedArgumentValue(0, additionalBasePackages);
		}
	}

	/**
	 * {@link ImportBeanDefinitionRegistrar} to store the base package from the importing
	 * configuration.
	 */
	// {@link ImportBeanDefinitionRegistrar} 用于存储来自导入配置的基础包。
	static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports {

		@Override
		public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
			register(registry, new PackageImports(metadata).getPackageNames().toArray(new String[0]));
		}

		// 确定 Imports
		@Override
		public Set<Object> determineImports(AnnotationMetadata metadata) {
			return Collections.singleton(new PackageImports(metadata));
		}

	}

	/**
	 * Wrapper for a package import.
	 */
	// 包导入的包装器。
	private static final class PackageImports {

		private final List<String> packageNames;

		PackageImports(AnnotationMetadata metadata) {
			AnnotationAttributes attributes = AnnotationAttributes
				.fromMap(metadata.getAnnotationAttributes(AutoConfigurationPackage.class.getName(), false));
			// 获取 AutoConfigurationPackage#basePackages 为数组并转换为 List
			List<String> packageNames = new ArrayList<>(Arrays.asList(attributes.getStringArray("basePackages")));
			// 获取 AutoConfigurationPackage#basePackageClasses 为数组
			for (Class<?> basePackageClass : attributes.getClassArray("basePackageClasses")) {
				// 获取 Class 的包名并添加到 packageNames 中
				packageNames.add(basePackageClass.getPackage().getName());
			}
			if (packageNames.isEmpty()) {
				// 确定给定完全限定类名的包的名称，例如 java.lang.String 类名的包名称为 “java.lang”。
				// 即返回被 @AutoConfigurationPackage 标记的类的包名
				packageNames.add(ClassUtils.getPackageName(metadata.getClassName()));
			}
			this.packageNames = Collections.unmodifiableList(packageNames); // 返回指定列表的不可修改视图。
		}

		List<String> getPackageNames() {
			return this.packageNames;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			return this.packageNames.equals(((PackageImports) obj).packageNames);
		}

		@Override
		public int hashCode() {
			return this.packageNames.hashCode();
		}

		@Override
		public String toString() {
			return "Package Imports " + this.packageNames;
		}

	}

	/**
	 * Holder for the base package (name may be null to indicate no scanning).
	 */
	// 基础包的持有者（名称可以为空以表示无扫描）。
	static final class BasePackages {

		private final List<String> packages;

		private boolean loggedBasePackageInfo;

		BasePackages(String... names) {
			List<String> packages = new ArrayList<>();
			for (String name : names) {
				if (StringUtils.hasText(name)) {
					packages.add(name);
				}
			}
			this.packages = packages;
		}

		List<String> get() {
			if (!this.loggedBasePackageInfo) {
				if (this.packages.isEmpty()) {
					if (logger.isWarnEnabled()) {
						// @EnableAutoConfiguration 已在默认包中的类上声明。未启用自动 @Repository 和 @Entity 扫描。
						logger.warn("@EnableAutoConfiguration was declared on a class "
								+ "in the default package. Automatic @Repository and "
								+ "@Entity scanning is not enabled.");
					}
				}
				else {
					if (logger.isDebugEnabled()) {
						String packageNames = StringUtils.collectionToCommaDelimitedString(this.packages);
						// @EnableAutoConfiguration 已在包 “packageNames” 中的类上声明。已启用自动 @Repository 和 @Entity 扫描。
						logger.debug("@EnableAutoConfiguration was declared on a class in the package '" + packageNames
								+ "'. Automatic @Repository and @Entity scanning is enabled.");
					}
				}
				this.loggedBasePackageInfo = true;
			}
			return this.packages;
		}

	}

}
