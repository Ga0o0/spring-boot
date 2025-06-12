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

package org.springframework.boot.origin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Interface that uniquely represents the origin of an item. For example, an item loaded
 * from a {@link File} may have an origin made up of the file name along with line/column
 * numbers.
 * <p>
 * Implementations must provide sensible {@code hashCode()}, {@code equals(...)} and
 * {@code #toString()} implementations.
 *
 * @author Madhura Bhave
 * @author Phillip Webb
 * @since 2.0.0
 * @see OriginProvider
 * @see TextResourceOrigin
 */
// 唯一表示项目来源的接口。例如，从 {@link File} 加载的项目可能具有由文件名以及行号/列号组成的来源。
//
// <p> 实现必须提供合理的 {@code hashCode()}、{@code equals(...)} 和 {@code #toString()} 实现。
public interface Origin {

	/**
	 * Return the parent origin for this instance if there is one. The parent origin
	 * provides the origin of the item that created this one.
	 * @return the parent origin or {@code null}
	 * @since 2.4.0
	 * @see Origin#parentsFrom(Object)
	 */
	// 如果存在父源，则返回此实例的父源。父源提供创建此实例的项目的源。
	// @return 父源或 {@code null}
	default Origin getParent() {
		return null;
	}

	/**
	 * Find the {@link Origin} that an object originated from. Checks if the source object
	 * is an {@link Origin} or {@link OriginProvider} and also searches exception stacks.
	 * @param source the source object or {@code null}
	 * @return an optional {@link Origin}
	 */
	// 查找对象的来源 {@link Origin}。检查源对象是否为 {@link Origin} 或 {@link OriginProvider}，并搜索异常堆栈。
	// @param source 源对象或 {@code null}
	// @return 可选的 {@link Origin}
	static Origin from(Object source) {
		if (source instanceof Origin origin) {
			return origin;
		}
		Origin origin = null;
		if (source instanceof OriginProvider originProvider) {
			origin = originProvider.getOrigin();
		}
		if (origin == null && source instanceof Throwable throwable) {
			return from(throwable.getCause());
		}
		return origin;
	}

	/**
	 * Find the parents of the {@link Origin} that an object originated from. Checks if
	 * the source object is an {@link Origin} or {@link OriginProvider} and also searches
	 * exception stacks. Provides a list of all parents up to root {@link Origin},
	 * starting with the most immediate parent.
	 * @param source the source object or {@code null}
	 * @return a list of parents or an empty list if the source is {@code null}, has no
	 * origin, or no parent
	 * @since 2.4.0
	 */
	// 查找对象来源 {@link Origin} 的父对象。
	// 检查源对象是否为 {@link Origin} 或 {@link OriginProvider}，并搜索异常堆栈。
	// 提供所有父对象的列表，直至根 {@link Origin}，从最直接的父对象开始。
	// @param source 源对象或 {@code null}
	// @return 返回父对象列表；如果源对象为 {@code null}、无来源或无父对象，则返回空列表
	static List<Origin> parentsFrom(Object source) {
		Origin origin = from(source);
		if (origin == null) {
			return Collections.emptyList();
		}
		Set<Origin> parents = new LinkedHashSet<>();
		origin = origin.getParent();
		while (origin != null && !parents.contains(origin)) {
			parents.add(origin);
			origin = origin.getParent();
		}
		return Collections.unmodifiableList(new ArrayList<>(parents));
	}

}
