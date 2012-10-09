/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.orm;

import name.martingeisse.admin.entity.instance.RawEntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.common.util.ParameterUtil;

import com.mysema.query.sql.RelationalPath;

/**
 * This class represents the mapping between an entity and specific
 * code for that entity. It contains:
 * 
 * - an optional {@link RelationalPath} that specifies how entity
 *   instances are fetched from the database
 * - an optional class object used by entity instances
 */
public final class EntitySpecificCodeMapping {

	/**
	 * the entity
	 */
	private final EntityDescriptor entity;

	/**
	 * the relationalPath
	 */
	private final RelationalPath<?> relationalPath;

	/**
	 * the entityInstanceClass
	 */
	private final Class<?> entityInstanceClass;

	/**
	 * Constructor.
	 * @param entity the parent entity (must not be null)
	 * @param relationalPath the {@link RelationalPath} used to fetch the entity, or null to use
	 * the default "data row" fetching
	 * @param entityInstanceClass the class used for entity instances, or null to use the
	 * default {@link RawEntityInstance}.
	 */
	public EntitySpecificCodeMapping(final EntityDescriptor entity, final RelationalPath<?> relationalPath,
		final Class<?> entityInstanceClass) {
		ParameterUtil.ensureNotNull(entity, "entity");
		this.entity = entity;
		this.relationalPath = relationalPath;
		this.entityInstanceClass = entityInstanceClass;
	}

	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return entity;
	}

	/**
	 * Getter method for the relationalPath.
	 * @return the relationalPath
	 */
	public RelationalPath<?> getRelationalPath() {
		return relationalPath;
	}

	/**
	 * Getter method for the entityInstanceClass.
	 * @return the entityInstanceClass
	 */
	public Class<?> getEntityInstanceClass() {
		return entityInstanceClass;
	}

}
