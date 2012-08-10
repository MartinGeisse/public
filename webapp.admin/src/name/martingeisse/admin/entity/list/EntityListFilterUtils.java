/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;

import com.mysema.query.support.Expressions;
import com.mysema.query.types.Path;

/**
 * Utility methods used by entity list filters and their clients.
 */
public class EntityListFilterUtils {

	/**
	 * Prevent instantiation.
	 */
	private EntityListFilterUtils() {
	}
	
	/**
	 * Creates and returns a {@link Path} for the entity being filtered.
	 * This is simply a {@link Path} for {@link IEntityListFilter#ALIAS}.
	 * @return the path for the entity
	 */
	public static Path<Object> entityPath() {
		return Expressions.path(Object.class, IEntityListFilter.ALIAS);
	}
	
	/**
	 * Creates and returns an untyped {@link Path} for the specified field of the
	 * entity being filtered. This is simply a {@link Path} for a field
	 * of {@link IEntityListFilter#ALIAS}.
	 * @param fieldName the name of the field
	 * @return the path for the field
	 */
	public static Path<Object> fieldPath(String fieldName) {
		return Expressions.path(Object.class, Expressions.path(Object.class, IEntityListFilter.ALIAS), fieldName);
	}
	
	/**
	 * Creates and returns a typed {@link Path} for the specified field of the
	 * entity being filtered. This is simply a {@link Path} for a field
	 * of {@link IEntityListFilter#ALIAS}.
	 * @param fieldType the class object for the field type
	 * @param fieldName the name of the field
	 * @param <T> the static field type
	 * @return the path for the field
	 */
	public static <T> Path<T> fieldPath(Class<T> fieldType, String fieldName) {
		return Expressions.path(fieldType, Expressions.path(Object.class, IEntityListFilter.ALIAS), fieldName);
	}

}
