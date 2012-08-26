/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.security;

import name.martingeisse.admin.application.ApplicationConfiguration;

/**
 * Utilities to access security-related configuration in the {@link ApplicationConfiguration}.
 */
public class SecurityConfigurationUtil {

	/**
	 * The parameter key for the security configuration.
	 */
	public static final Class<SecurityConfiguration> SECURITY_CONFIGURATION_PARAMETER_KEY = SecurityConfiguration.class;

	/**
	 * Getter method for the security configuration.
	 * @return the security configuration
	 */
	public static SecurityConfiguration getSecurityConfiguration() {
		return ApplicationConfiguration.get().getParameters().get(SECURITY_CONFIGURATION_PARAMETER_KEY);
	}

	/**
	 * Setter method for the security configuration.
	 * @param securityConfiguration the security configuration to set
	 */
	public static void setSecurityConfiguration(final SecurityConfiguration securityConfiguration) {
		ApplicationConfiguration.get().getParameters().set(SECURITY_CONFIGURATION_PARAMETER_KEY, securityConfiguration);
	}
	
}
