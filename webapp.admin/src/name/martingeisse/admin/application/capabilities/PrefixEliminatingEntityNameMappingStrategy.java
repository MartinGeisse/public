/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import name.martingeisse.admin.entity.IEntityNameMappingStrategy;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * This name mapping strategy checks if the name begins with
 * the specified prefix and if so, removes that prefix.
 */
public class PrefixEliminatingEntityNameMappingStrategy implements IEntityNameMappingStrategy {

	/**
	 * the prefix
	 */
	private String prefix;

	/**
	 * Constructor.
	 */
	public PrefixEliminatingEntityNameMappingStrategy() {
	}

	/**
	 * Constructor.
	 * @param prefix the prefix to eliminate
	 */
	public PrefixEliminatingEntityNameMappingStrategy(final String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Getter method for the prefix.
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Setter method for the prefix.
	 * @param prefix the prefix to set
	 */
	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityNameMappingStrategy#getEntityName(name.martingeisse.admin.schema.EntityDescriptor)
	 */
	@Override
	public String getEntityName(EntityDescriptor entity) {
		String name = entity.getTableName();
		if (name.startsWith(prefix)) {
			return name.substring(prefix.length());
		} else {
			return name;
		}
	}

}
