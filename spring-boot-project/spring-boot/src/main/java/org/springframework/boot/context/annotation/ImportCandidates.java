/*
 * Copyright 2012-2025 the original author or authors.
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

package org.springframework.boot.context.annotation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;

/**
 * Contains {@code @Configuration} import candidates, usually auto-configurations.
 *
 * The {@link #load(Class, ClassLoader)} method can be used to discover the import
 * candidates.
 *
 * @author Moritz Halbritter
 * @author Scott Frederick
 * @since 2.7.0
 */
// 包含 {@code @Configuration} 导入候选，通常是自动配置。
//
// {@link #load(Class, ClassLoader)} 方法可用于发现导入候选。
public final class ImportCandidates implements Iterable<String> {

	private static final String LOCATION = "META-INF/spring/%s.imports";

	private static final String COMMENT_START = "#";

	private final List<String> candidates;

	private ImportCandidates(List<String> candidates) {
		Assert.notNull(candidates, "'candidates' must not be null");
		this.candidates = Collections.unmodifiableList(candidates);
	}

	@Override
	public Iterator<String> iterator() {
		return this.candidates.iterator();
	}

	/**
	 * Returns the list of loaded import candidates.
	 * @return the list of import candidates
	 */
	// 返回已加载的导入候选项列表。
	// @return 导入候选项列表
	public List<String> getCandidates() {
		return this.candidates;
	}

	/**
	 * Loads the names of import candidates from the classpath.
	 *
	 * The names of the import candidates are stored in files named
	 * {@code META-INF/spring/full-qualified-annotation-name.imports} on the classpath.
	 * Every line contains the full qualified name of the candidate class. Comments are
	 * supported using the # character.
	 * @param annotation annotation to load
	 * @param classLoader class loader to use for loading
	 * @return list of names of annotated classes
	 */
	// 从类路径加载导入候选的名称。
	// 导入候选的名称存储在类路径下名为 {@code META-INF/spring/full-qualified-annotation-name.imports} 的文件中。
	// 每行都包含候选类的完整限定名。支持使用 # 字符进行注释。
	// @param 注解 用于加载的注解
	// @param classLoader 用于加载的类加载器
	// @return 带注解的类的名称列表
	public static ImportCandidates load(Class<?> annotation, ClassLoader classLoader) {
		Assert.notNull(annotation, "'annotation' must not be null");
		ClassLoader classLoaderToUse = decideClassloader(classLoader); // 决定类加载器
		String location = String.format(LOCATION, annotation.getName()); //  META-INF/spring/full-qualified-annotation-name.imports
		Enumeration<URL> urls = findUrlsInClasspath(classLoaderToUse, location);
		List<String> importCandidates = new ArrayList<>();
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			// readCandidateConfigurations(url) -> 读取候选配置
			importCandidates.addAll(readCandidateConfigurations(url));
		}
		return new ImportCandidates(importCandidates);
	}

	private static ClassLoader decideClassloader(ClassLoader classLoader) {
		if (classLoader == null) {
			return ImportCandidates.class.getClassLoader();
		}
		return classLoader;
	}

	private static Enumeration<URL> findUrlsInClasspath(ClassLoader classLoader, String location) {
		try {
			return classLoader.getResources(location);
		}
		catch (IOException ex) {
			throw new IllegalArgumentException("Failed to load configurations from location [" + location + "]", ex);
		}
	}

	private static List<String> readCandidateConfigurations(URL url) {
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new UrlResource(url).getInputStream(), StandardCharsets.UTF_8))) {
			List<String> candidates = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				line = stripComment(line); // 跳过注释
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}
				candidates.add(line);
			}
			return candidates;
		}
		catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load configurations from location [" + url + "]", ex);
		}
	}

	private static String stripComment(String line) {
		int commentStart = line.indexOf(COMMENT_START); // COMMENT_START = "#"
		if (commentStart == -1) {
			return line;
		}
		return line.substring(0, commentStart);
	}

}
