/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import name.martingeisse.admin.schema.AbstractApplicationSchema;

/**
 * Implementations are contributed by plugins and are able to
 * recognize entity fields as references to other entities.
 */
public interface IEntityReferenceDetector {

	/**
	 * Checks if the specified property of the specified entity is a
	 * reference to another entity. If so, returns the name of that
	 * other entity. The application schema will look for an entity with
	 * that name, and if found, store the reference. It is not an error
	 * if the other entity does not exist since most implementations will
	 * be based on heuristics and patterns. 
	 * 
	 * @param schema the application schema. Note that this object must be used
	 * to look up entities since the global schema reference is not yet
	 * initialized when this method is invoked.
	 * @param entityName the name of the entity that contains the property
	 * @param propertyName the name of the property
	 * @return the name of the referenced entity, or null if the property
	 * was not recognized as a reference
	 */
	public String detectEntityReference(AbstractApplicationSchema schema, String entityName, String propertyName);
	
}
