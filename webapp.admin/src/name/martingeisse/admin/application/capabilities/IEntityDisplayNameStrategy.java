/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import java.io.Serializable;

import name.martingeisse.admin.schema.EntityDescriptor;

/**
 * This strategy returns the displayed name for each entity.
 */
public interface IEntityDisplayNameStrategy extends Serializable {

	/**
	 * Returns the displayed name for the specified entity.
	 * @param entity the entity
	 * @return the name to display
	 */
	public String getDisplayName(EntityDescriptor entity);
	
}
