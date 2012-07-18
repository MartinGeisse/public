/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import name.martingeisse.admin.entity.multi.IEntityListFieldOrder;
import name.martingeisse.admin.entity.multi.IGlobalEntityListPresenter;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * This class contains general configuration that applies to all entities.
 * It is determined by the application and stored in the application configuration.
 * 
 * TODO: In many places, defaults are used if no values are set in this
 * configuration. Refactor: Set default values in this configuration and
 * do not allow null values at runtime. This moves default behavior to
 * a central point (here) and not scatter it across the whole code.
 */
public final class GeneralEntityConfiguration {

	/**
	 * the defaultEntityListPresenter
	 */
	private IGlobalEntityListPresenter defaultEntityListPresenter;

	/**
	 * the entityListFieldOrder
	 */
	private IEntityListFieldOrder entityListFieldOrder;

	/**
	 * the entityNameMappingStrategy
	 */
	private IEntityNameMappingStrategy entityNameMappingStrategy;

	/**
	 * Constructor.
	 */
	public GeneralEntityConfiguration() {
	}

	/**
	 * Getter method for the defaultEntityListPresenter.
	 * @return the defaultEntityListPresenter
	 */
	public IGlobalEntityListPresenter getDefaultEntityListPresenter() {
		return defaultEntityListPresenter;
	}

	/**
	 * Setter method for the defaultEntityListPresenter.
	 * @param defaultEntityListPresenter the defaultEntityListPresenter to set
	 */
	public void setDefaultEntityListPresenter(final IGlobalEntityListPresenter defaultEntityListPresenter) {
		this.defaultEntityListPresenter = defaultEntityListPresenter;
	}

	/**
	 * Getter method for the entityListFieldOrder.
	 * @return the entityListFieldOrder
	 */
	public IEntityListFieldOrder getEntityListFieldOrder() {
		return entityListFieldOrder;
	}

	/**
	 * Setter method for the entityListFieldOrder.
	 * @param entityListFieldOrder the entityListFieldOrder to set
	 */
	public void setEntityListFieldOrder(final IEntityListFieldOrder entityListFieldOrder) {
		this.entityListFieldOrder = entityListFieldOrder;
	}

	/**
	 * Getter method for the entityNameMappingStrategy.
	 * @return the entityNameMappingStrategy
	 */
	public IEntityNameMappingStrategy getEntityNameMappingStrategy() {
		return entityNameMappingStrategy;
	}

	/**
	 * Setter method for the entityNameMappingStrategy.
	 * @param entityNameMappingStrategy the entityNameMappingStrategy to set
	 */
	public void setEntityNameMappingStrategy(final IEntityNameMappingStrategy entityNameMappingStrategy) {
		this.entityNameMappingStrategy = entityNameMappingStrategy;
	}

	/**
	 * Returns the entity name for the specified entity.
	 * @param entity the entity
	 * @return the name to display
	 */
	public String getEntityName(final EntityDescriptor entity) {
		final IEntityNameMappingStrategy entityNameMappingStrategy = getEntityNameMappingStrategy();
		return (entityNameMappingStrategy == null ? entity.getTableName() : entityNameMappingStrategy.getEntityName(entity));
	}

}
