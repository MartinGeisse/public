/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import name.martingeisse.admin.application.ApplicationConfiguration;

/**
 * Utilities to access entity configuration in the {@link ApplicationConfiguration}.
 */
public final class EntityConfigurationUtil {

	/**
	 * The parameter key for the general entity configuration.
	 */
	public static final Class<GeneralEntityConfiguration> GENERAL_ENTITY_CONFIGURATION_PARAMETER_KEY = GeneralEntityConfiguration.class;

	/**
	 * Prevent instantiation.
	 */
	private EntityConfigurationUtil() {
	}

	/**
	 * Getter method for the general entity configuration.
	 * @return the general entity configuration
	 */
	public static GeneralEntityConfiguration getGeneralEntityConfiguration() {
		return ApplicationConfiguration.get().getParameters().get(GENERAL_ENTITY_CONFIGURATION_PARAMETER_KEY);
	}

	/**
	 * Setter method for the general entity configuration.
	 * @param generalEntityConfiguration the general entity configuration to set
	 */
	public static void setGeneralEntityConfiguration(final GeneralEntityConfiguration generalEntityConfiguration) {
		ApplicationConfiguration.get().getParameters().set(GENERAL_ENTITY_CONFIGURATION_PARAMETER_KEY, generalEntityConfiguration);
	}

}
