/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.annotation;

import name.martingeisse.admin.entity.schema.ApplicationSchema;


/**
 * This strategy contributes entity annotations at startup.
 * 
 * Annotations cannot be stored with entity bean classes because
 * these classes are generated from the database schema. Instead,
 * one or more contributors using this interface provide the
 * annotations. This also allows to contribute annotations in a
 * more modular way than simply enumerating them all.
 */
public interface IEntityAnnotationContributor {

	/**
	 * Contributes annotations for all entities of the application schema and stores
	 * it in the corresponding fields in the entity and its properties.
	 * 
	 * @param applicationSchema the application schema (note that the singleton
	 * reference to this object is not yet set at the time this method is called).
	 */
	public void contributeEntityAnnotations(ApplicationSchema applicationSchema);
	
}
