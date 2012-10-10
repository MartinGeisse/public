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
public class DefaultEntityOrmMapper implements IEntityOrmMapper {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.orm.IEntityOrmMapper#map(name.martingeisse.admin.entity.schema.EntityDescriptor)
	 */
	@Override
	public EntityOrmMapping map(final EntityDescriptor entityDescriptor) {
		return new EntityOrmMapping(entityDescriptor, null, null);
	}

}
