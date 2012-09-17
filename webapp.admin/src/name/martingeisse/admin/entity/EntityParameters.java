/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import name.martingeisse.admin.application.ParameterKey;

/**
 * Entity-related parameter keys.
 */
public final class EntityParameters {

	/**
	 * The parameter key for the general entity configuration.
	 */
	public static final ParameterKey<GeneralEntityConfiguration> generalEntityConfigurationParameter = new ParameterKey<GeneralEntityConfiguration>();

	/**
	 * Prevent instantiation.
	 */
	private EntityParameters() {
	}

}
