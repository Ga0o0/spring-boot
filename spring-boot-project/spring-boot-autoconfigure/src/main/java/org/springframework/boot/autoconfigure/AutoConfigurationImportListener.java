/*
 * Copyright 2012-2019 the original author or authors.
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

import java.util.EventListener;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;

/**
 * Listener that can be registered with {@code spring.factories} to receive details of
 * imported auto-configurations.
 * <p>
 * An {@link AutoConfigurationImportListener} may implement any of the following
 * {@link org.springframework.beans.factory.Aware Aware} interfaces, and their respective
 * methods will be called prior to
 * {@link #onAutoConfigurationImportEvent(AutoConfigurationImportEvent)}:
 * <ul>
 * <li>{@link EnvironmentAware}</li>
 * <li>{@link BeanFactoryAware}</li>
 * <li>{@link BeanClassLoaderAware}</li>
 * <li>{@link ResourceLoaderAware}</li>
 * </ul>
 *
 * @author Phillip Webb
 * @since 1.5.0
 */
// 可以向 {@code spring.factories} 注册的监听器，用于接收导入的自动配置的详细信息。
//
// <p> {@link AutoConfigurationImportListener} 可以实现以下任何 {@link org.springframework.beans.factory.Aware Aware} 接口，
// 并且它们各自的方法将在 {@link #onAutoConfigurationImportEvent(AutoConfigurationImportEvent)} 之前调用：
//
// <ul>
// <li>{@link EnvironmentAware} </li>
// <li>{@link BeanFactoryAware } </li>
// <li>{@link BeanClassLoaderAware } </li>
// <li>{@link ResourceLoaderAware } </li>
// </ul>
@FunctionalInterface
public interface AutoConfigurationImportListener extends EventListener {

	/**
	 * Handle an auto-configuration import event.
	 * @param event the event to respond to
	 */
	// 处理自动配置导入事件。
	// @param event 需要响应的事件
	void onAutoConfigurationImportEvent(AutoConfigurationImportEvent event);

}
