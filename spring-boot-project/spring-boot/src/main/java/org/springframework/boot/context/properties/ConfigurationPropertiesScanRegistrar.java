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

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * {@link ImportBeanDefinitionRegistrar} for registering
 * {@link ConfigurationProperties @ConfigurationProperties} bean definitions through
 * scanning.
 *
 * @author Madhura Bhave
 * @author Phillip Webb
 */
// {@link ImportBeanDefinitionRegistrar} 用于通过扫描注册 {@link ConfigurationProperties @ConfigurationProperties} bean 定义。
class ConfigurationPropertiesScanRegistrar implements ImportBeanDefinitionRegistrar {

	private final Environment environment;

	private final ResourceLoader resourceLoader;

	ConfigurationPropertiesScanRegistrar(Environment environment, ResourceLoader resourceLoader) {
		this.environment = environment;
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		// 从注解 @ConfigurationPropertiesScan 上需要扫描包名的集合
		Set<String> packagesToScan = getPackagesToScan(importingClassMetadata);
		scan(registry, packagesToScan);
	}

	private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
		// 获取注解 @ConfigurationPropertiesScan 的属性
		AnnotationAttributes attributes = AnnotationAttributes
			.fromMap(metadata.getAnnotationAttributes(ConfigurationPropertiesScan.class.getName()));
		// ConfigurationPropertiesScan#basePackages
		String[] basePackages = attributes.getStringArray("basePackages");
		// ConfigurationPropertiesScan#basePackageClasses
		Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
		Set<String> packagesToScan = new LinkedHashSet<>(Arrays.asList(basePackages));
		for (Class<?> basePackageClass : basePackageClasses) {
			// ClassUtils.getPackageName(basePackageClass) -> 确定给定类的包的名称
			packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
		}
		if (packagesToScan.isEmpty()) {
			// 添加注解 @ConfigurationPropertiesScan 标注类的包的名称
			packagesToScan.add(ClassUtils.getPackageName(metadata.getClassName()));
		}
		// 移除没有实际文本的名包
		packagesToScan.removeIf((candidate) -> !StringUtils.hasText(candidate));
		return packagesToScan;
	}

	private void scan(BeanDefinitionRegistry registry, Set<String> packages) {
		ConfigurationPropertiesBeanRegistrar registrar = new ConfigurationPropertiesBeanRegistrar(registry);
		// 创建 ClassPathScanningCandidateComponentProvider
		ClassPathScanningCandidateComponentProvider scanner = getScanner(registry);
		// 扫描包，查找候选组件并注册
		for (String basePackage : packages) {
			for (BeanDefinition candidate : scanner.findCandidateComponents(basePackage)) {
				register(registrar, candidate.getBeanClassName());
			}
		}
	}

	private ClassPathScanningCandidateComponentProvider getScanner(BeanDefinitionRegistry registry) {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.setEnvironment(this.environment);
		scanner.setResourceLoader(this.resourceLoader);
		// 包含 @ConfigurationProperties
		scanner.addIncludeFilter(new AnnotationTypeFilter(ConfigurationProperties.class));
		TypeExcludeFilter typeExcludeFilter = new TypeExcludeFilter();
		typeExcludeFilter.setBeanFactory((BeanFactory) registry);
		scanner.addExcludeFilter(typeExcludeFilter);
		return scanner;
	}

	private void register(ConfigurationPropertiesBeanRegistrar registrar, String className) throws LinkageError {
		try {
			register(registrar, ClassUtils.forName(className, null));
		}
		catch (ClassNotFoundException ex) {
			// Ignore
		}
	}

	private void register(ConfigurationPropertiesBeanRegistrar registrar, Class<?> type) {
		// 是否被 @Component 标记
		if (!isComponent(type)) {
			registrar.register(type);
		}
	}

	private boolean isComponent(Class<?> type) {
		return MergedAnnotations.from(type, SearchStrategy.TYPE_HIERARCHY).isPresent(Component.class);
	}

}
