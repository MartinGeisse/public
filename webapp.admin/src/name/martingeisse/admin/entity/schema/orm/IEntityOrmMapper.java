/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.orm;

import name.martingeisse.admin.application.ParameterKey;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * This class is used by the application to map entities to
 * specific implementations.
 */
public interface IEntityOrmMapper {

	/**
	 * The parameter key for this interface.
	 */
	public static final ParameterKey<IEntityOrmMapper> PARAMETER_KEY = new ParameterKey<IEntityOrmMapper>();

	/**
	 * Maps the specified entity to its specific implementation.
	 * @param entityDescriptor the entity to map
	 * @return the mapping (never null)
	 */
	public EntitySpecificCodeMapping map(EntityDescriptor entityDescriptor);

}
