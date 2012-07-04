/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import java.io.Serializable;

import name.martingeisse.admin.schema.EntityDescriptor;

/**
 * This strategy maps database table names to entity names.
 * 
 * TODO: This interface should be used while building the schema.
 * It is currently only used when the displayed name for the
 * entity is determined.
 */
public interface IEntityNameMappingStrategy extends Serializable {

	/**
	 * Returns the entity name for the specified entity.
	 * @param entity the entity
	 * @return the entity name
	 */
	public String getEntityName(EntityDescriptor entity);
	
}
