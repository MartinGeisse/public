/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admon.entity;

import name.martingeisse.common.util.ParameterUtil;

/**
 * This exception type is thrown when an entity is specified by name,
 * but no such entity exists.
 */
public class UnknownEntityException extends RuntimeException {

	/**
	 * the entityName
	 */
	private final String entityName;

	/**
	 * Constructor.
	 * @param entityName the name that does not belong to any known entity
	 */
	public UnknownEntityException(String entityName) {
		super("Unknown entity: " + ParameterUtil.ensureNotNull(entityName, "entityName"));
		this.entityName = entityName;
	}

	/**
	 * Getter method for the entityName.
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

}
