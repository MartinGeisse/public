/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.annotation;

import name.martingeisse.admin.entity.schema.ApplicationSchema;


/**
 * This strategy finds entity annotations at startup.
 */
public interface IEntityAnnotationResolver {

	/**
	 * Resolves annotations for all entities of the application schema and stores
	 * it in the corresponding fields in the entity and its properties.
	 * @param applicationSchema the application schema (note that the singleton
	 * reference to this object is not yet set at the time this method is called).
	 */
	public void resolveEntityAnnotations(ApplicationSchema applicationSchema);
	
}
