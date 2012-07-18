/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

/**
 * This interface provides a getter method for an entity ID. It can be implemented,
 * for example, by pages to provide an ID for navigation links init.
 */
public interface IGetEntityId {

	/**
	 * @return the entity ID
	 */
	public Object getEntityId();
	
}
