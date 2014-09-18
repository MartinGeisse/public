/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.application.security;

import name.martingeisse.common.security.authentication.IAuthenticationStrategy;
import name.martingeisse.common.security.authentication.IUserIdentity;
import name.martingeisse.common.security.authentication.IUserProperties;
import name.martingeisse.common.security.credentials.ICredentials;

/**
 * SlaveServices-specific authentication strategy.
 */
public class AuthenticationStrategy implements IAuthenticationStrategy {

	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.security.authentication.IAuthenticationStrategy#determineProperties(name.martingeisse.wicket.security.credentials.ICredentials)
	 */
	@Override
	public IUserProperties determineProperties(ICredentials credentials) {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.security.authentication.IAuthenticationStrategy#determineIdentity(name.martingeisse.wicket.security.authentication.IUserProperties)
	 */
	@Override
	public IUserIdentity determineIdentity(IUserProperties properties) {
		return null;
	}

}
