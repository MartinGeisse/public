/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.naming;


import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.commons.lang3.text.WordUtils;

/**
 * This name mapping strategy checks if the name begins with
 * the specified prefix and if so, removes that prefix.
 * The display name is set to the name.
 */
public final class PrefixEliminatingEntityNameMappingStrategy implements IEntityNameMappingStrategy {

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
	 * @see name.martingeisse.admin.entity.IEntityNameMappingStrategy#determineEntityName(name.martingeisse.admin.entity.schema.EntityDescriptor)
	 */
	@Override
	public String determineEntityName(final EntityDescriptor entity) {
		final String name = entity.getTableName();
		if (name.startsWith(prefix)) {
			return name.substring(prefix.length());
		} else {
			return name;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.IEntityNameMappingStrategy#determineEntityDisplayName(name.martingeisse.admin.entity.schema.EntityDescriptor)
	 */
	@Override
	public String determineEntityDisplayName(final EntityDescriptor entity) {
		String baseName = determineEntityName(entity);
		baseName = baseName.replace('-', ' ');
		baseName = baseName.replace('_', ' ');
		return WordUtils.capitalizeFully(baseName);
	}

}
