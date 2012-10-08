/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.naming;


import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.commons.lang.WordUtils;

/**
 * This name mapping strategy checks if the name begins with
 * the specified prefix and if so, removes that prefix.
 * The display name is set to the name.
 */
public final class DefaultEntityNameMappingStrategy implements IEntityNameMappingStrategy {

	/**
	 * Constructor.
	 */
	public DefaultEntityNameMappingStrategy() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.IEntityNameMappingStrategy#determineEntityName(name.martingeisse.admin.entity.schema.EntityDescriptor)
	 */
	@Override
	public String determineEntityName(final EntityDescriptor entity) {
		return entity.getTableName();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.IEntityNameMappingStrategy#determineEntityDisplayName(name.martingeisse.admin.entity.schema.EntityDescriptor)
	 */
	@Override
	public String determineEntityDisplayName(final EntityDescriptor entity) {
		String baseName = entity.getTableName();
		baseName = baseName.replace('-', ' ');
		baseName = baseName.replace('_', ' ');
		return WordUtils.capitalizeFully(baseName);
	}

}
