/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import java.io.Serializable;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * This strategy maps database table names to entity names and display names.
 */
public interface IEntityNameMappingStrategy extends Serializable {

	/**
	 * Returns the entity name for the specified entity. Should not assume
	 * either the name or display name to be set.
	 * @param entity the entity
	 * @return the entity name
	 */
	public String determineEntityName(EntityDescriptor entity);

	/**
	 * Returns the entity display name for the specified entity. This method
	 * may assume that the entity name is set as returned by determineEntityName().
	 * @param entity the entity
	 * @return the entity display name
	 */
	public String determineEntityDisplayName(EntityDescriptor entity);

}
