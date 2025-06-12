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

package org.springframework.boot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.logging.Log;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Class used by {@link SpringApplication} to print the application banner.
 *
 * @author Phillip Webb
 */
// {@link SpringApplication} 用于打印应用程序横幅的类。
class SpringApplicationBannerPrinter {

	static final String BANNER_LOCATION_PROPERTY = "spring.banner.location";

	static final String DEFAULT_BANNER_LOCATION = "banner.txt";

	private static final Banner DEFAULT_BANNER = new SpringBootBanner();

	private final ResourceLoader resourceLoader;

	private final Banner fallbackBanner;

	SpringApplicationBannerPrinter(ResourceLoader resourceLoader, Banner fallbackBanner) {
		this.resourceLoader = resourceLoader;
		this.fallbackBanner = fallbackBanner;
	}

	Banner print(Environment environment, Class<?> sourceClass, Log logger) {
		// 获取 Banner
		Banner banner = getBanner(environment);
		try {
			// 使用指定的字符集解码字节，将缓冲区内容转换为字符串，并使用 logger 打印
			logger.info(createStringFromBanner(banner, environment, sourceClass));
		}
		catch (UnsupportedEncodingException ex) {
			logger.warn("Failed to create String for banner", ex); // 无法创建横幅字符串
		}
		return new PrintedBanner(banner, sourceClass);
	}

	Banner print(Environment environment, Class<?> sourceClass, PrintStream out) {
		Banner banner = getBanner(environment);
		banner.printBanner(environment, sourceClass, out);
		return new PrintedBanner(banner, sourceClass);
	}

	private Banner getBanner(Environment environment) {
		// 从 spring.banner.location 获取 Banner；
		// spring.banner.location 默认值为 banner.txt
		Banner textBanner = getTextBanner(environment);
		if (textBanner != null) {
			return textBanner;
		}
		if (this.fallbackBanner != null) {
			return this.fallbackBanner;
		}
		// DEFAULT_BANNER = new SpringBootBanner()
		return DEFAULT_BANNER;
	}

	private Banner getTextBanner(Environment environment) {
		// BANNER_LOCATION_PROPERTY = "spring.banner.location"
		// DEFAULT_BANNER_LOCATION = "banner.txt"
		// spring.banner.location 默认值为 banner.txt
		String location = environment.getProperty(BANNER_LOCATION_PROPERTY, DEFAULT_BANNER_LOCATION);
		Resource resource = this.resourceLoader.getResource(location);
		try {
			if (resource.exists() && !resource.getURL().toExternalForm().contains("liquibase-core")) {
				return new ResourceBanner(resource);
			}
		}
		catch (IOException ex) {
			// Ignore
		}
		return null;
	}

	private String createStringFromBanner(Banner banner, Environment environment, Class<?> mainApplicationClass)
			throws UnsupportedEncodingException {
		// spring.banner.charset 默认值 UTF-8
		String charset = environment.getProperty("spring.banner.charset", StandardCharsets.UTF_8.name());
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try (PrintStream out = new PrintStream(byteArrayOutputStream, false, charset)) {
			// 将横幅打印到指定的打印流。
			banner.printBanner(environment, mainApplicationClass, out);
		}
		// 使用指定的字符集解码字节，将缓冲区内容转换为字符串。
		return byteArrayOutputStream.toString(charset);
	}

	/**
	 * Decorator that allows a {@link Banner} to be printed again without needing to
	 * specify the source class.
	 */
	// 装饰器允许再次打印 {@link Banner}，而无需指定源类。
	private static class PrintedBanner implements Banner {

		private final Banner banner;

		private final Class<?> sourceClass;

		PrintedBanner(Banner banner, Class<?> sourceClass) {
			this.banner = banner;
			this.sourceClass = sourceClass;
		}

		@Override
		public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
			sourceClass = (sourceClass != null) ? sourceClass : this.sourceClass;
			// 将横幅打印到指定的打印流。
			this.banner.printBanner(environment, sourceClass, out);
		}

	}

	static class SpringApplicationBannerPrinterRuntimeHints implements RuntimeHintsRegistrar {

		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			hints.resources().registerPattern(DEFAULT_BANNER_LOCATION);
		}

	}

}
