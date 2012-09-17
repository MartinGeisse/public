/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.security;

import name.martingeisse.admin.application.ParameterKey;

/**
 * Security-related parameter keys.
 */
public class SecurityParameters {

	/**
	 * The parameter key for the security configuration.
	 */
	public static final ParameterKey<SecurityConfiguration> securityConfigurationParameter = new ParameterKey<SecurityConfiguration>();

	/**
	 * Getter method for the security configuration. Throws an exception if the configuration is missing (null).
	 * @return the security configuration
	 */
	public static SecurityConfiguration getSecurityConfigurationSafe() {
		SecurityConfiguration configuration = securityConfigurationParameter.get();
		if (configuration == null) {
			throw new IllegalStateException("no security configuration");
		}
		return configuration;
	}
	
}
