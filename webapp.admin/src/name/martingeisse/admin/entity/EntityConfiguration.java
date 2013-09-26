/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import name.martingeisse.admin.application.ParameterKey;
import name.martingeisse.wicket.autoform.componentfactory.DefaultAutoformPropertyComponentFactory;
import name.martingeisse.wicket.autoform.componentfactory.IAutoformPropertyComponentFactory;

/**
 * This class contains general configuration that applies to all entities.
 * It is determined by the application and stored in the application configuration.
 */
public final class EntityConfiguration {

	/**
	 * The parameter key for the general entity configuration.
	 */
	public static final ParameterKey<EntityConfiguration> parameterKey = new ParameterKey<EntityConfiguration>();

	/**
	 * the autoformPropertyComponentFactory
	 */
	private IAutoformPropertyComponentFactory autoformPropertyComponentFactory;

	/**
	 * Constructor.
	 */
	public EntityConfiguration() {
		autoformPropertyComponentFactory = DefaultAutoformPropertyComponentFactory.instance;
	}

	/**
	 * Getter method for the autoformPropertyComponentFactory.
	 * @return the autoformPropertyComponentFactory
	 */
	public IAutoformPropertyComponentFactory getAutoformPropertyComponentFactory() {
		return autoformPropertyComponentFactory;
	}

	/**
	 * Setter method for the autoformPropertyComponentFactory.
	 * @param autoformPropertyComponentFactory the autoformPropertyComponentFactory to set
	 */
	public void setAutoformPropertyComponentFactory(final IAutoformPropertyComponentFactory autoformPropertyComponentFactory) {
		this.autoformPropertyComponentFactory = autoformPropertyComponentFactory;
	}

}
