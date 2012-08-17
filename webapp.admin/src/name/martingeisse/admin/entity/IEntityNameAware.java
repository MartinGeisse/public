/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

/**
 * A typed getter/setter for entity names.
 */
public interface IEntityNameAware {

	/**
	 * Getter method for the entityName.
	 * @return the entityName
	 */
	public String getEntityName();

	/**
	 * Setter method for the entityName.
	 * @param entityName the entityName to set
	 */
	public void setEntityName(final String entityName);

}
