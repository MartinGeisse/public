/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import name.martingeisse.admin.application.ParameterKey;
import name.martingeisse.admin.entity.schema.IEntityListFieldOrder;
import name.martingeisse.admin.entity.schema.IEntityNameMappingStrategy;
import name.martingeisse.admin.entity.schema.PrefixEliminatingEntityNameMappingStrategy;

/**
 * This class contains general configuration that applies to all entities.
 * It is determined by the application and stored in the application configuration.
 */
public final class GeneralEntityConfiguration {

	/**
	 * The parameter key for the general entity configuration.
	 */
	public static final ParameterKey<GeneralEntityConfiguration> parameterKey = new ParameterKey<GeneralEntityConfiguration>();
	
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
		entityListFieldOrder = null;
		entityNameMappingStrategy = new PrefixEliminatingEntityNameMappingStrategy("");
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

}
