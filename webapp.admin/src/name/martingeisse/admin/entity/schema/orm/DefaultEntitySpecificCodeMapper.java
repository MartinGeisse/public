/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.orm;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * The default implementation for {@link IEntityOrmMapper}.
 * Returns an empty mapping for all entities.
 */
public class DefaultEntitySpecificCodeMapper implements IEntityOrmMapper {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.codemapping.IEntitySpecificCodeMapper#map(name.martingeisse.admin.entity.schema.EntityDescriptor)
	 */
	@Override
	public EntitySpecificCodeMapping map(final EntityDescriptor entityDescriptor) {
		return new EntitySpecificCodeMapping(entityDescriptor, null);
	}

}
