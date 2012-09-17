/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.annotation;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * This strategy is used by {@link AnnotatedClassEntityAnnotationContributor}
 * (and possibly other annotation resolvers) and finds the
 * annotated class for each entity.
 * 
 * It is an error if two resolvers return different classes for the
 * same entity. They must return the same class, if any.
 */
public interface IEntityAnnotatedClassResolver {

	/**
	 * Resolves the annotated class for the specified entity.
	 * @param entity the entity
	 * @return the annotated class, or null if this resolver doesn't know any such class
	 */
	public Class<?> resolveEntityAnnotatedClass(EntityDescriptor entity);
	
}
