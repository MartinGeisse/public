/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.instance;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * Utility methods to fetch entity instances from the database.
 */
public class EntityInstanceUtil {

	/**
	 * Prevent instantiation.
	 */
	private EntityInstanceUtil() {
	}
	
	/**
	 * Fetches an entity instance by id, throwing an exception if no
	 * such instance exists.
	 * @param entity the entity descriptor
	 * @param id the id
	 * @return the instance
	 */
	public static EntityInstance fetchRequiredById(EntityDescriptor entity, Object id) {
		return null;
	}
	
	/**
	 * Fetches an entity instance by id that may or may not exist.
	 * @param entity the entity descriptor
	 * @param id the id
	 * @return the instance or null
	 */
	public static EntityInstance fetchOptionalById(EntityDescriptor entity, Object id) {
		return null;
		// TODO remove FetchEntityInstanceAction
	}

	// fetchBy* (einzelnes Feld)
	// fetchBy* (mehrere Felder -> wie?)
	
	
}
