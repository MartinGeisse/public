/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.security.authentication;

import name.martingeisse.admin.security.credentials.ICredentials;

/**
 * This strategy ignores the credentials altogether and just returns
 * the same user properties and user identity all the time.
 */
public class DefaultAuthenticationStrategy implements IAdminAuthenticationStrategy {

	/**
	 * the user
	 */
	private static final UserIdentity user = new UserIdentity();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.security.authentication.IAdminAuthenticationStrategy#determineProperties(name.martingeisse.admin.application.security.ICredentials)
	 */
	@Override
	public IUserProperties determineProperties(ICredentials credentials) {
		return user;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.security.authentication.IAdminAuthenticationStrategy#determineIdentity(name.martingeisse.admin.application.security.authentication.IUserProperties)
	 */
	@Override
	public IUserIdentity determineIdentity(IUserProperties properties) {
		return user;
	}

	/**
	 * User identity implementation.
	 */
	private static class UserIdentity extends UserProperties implements IUserIdentity {
	}

}
