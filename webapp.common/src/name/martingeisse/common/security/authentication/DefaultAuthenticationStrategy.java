/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security.authentication;

import name.martingeisse.common.security.credentials.ICredentials;

/**
 * This strategy ignores the credentials altogether and just returns
 * the same user properties and user identity all the time. It can
 * be used in applications that do not distinguish users based on
 * identity (though such an application can still determine a user's
 * permissions directly from the supplied credentials).
 */
public class DefaultAuthenticationStrategy implements IAuthenticationStrategy {

	/**
	 * The singleton user identity of the application.
	 */
	public static final IUserIdentity SINGLETON_USER_IDENTITY = new IUserIdentity() {
	};
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.authentication.IAuthenticationStrategy#determineProperties(name.martingeisse.common.security.credentials.ICredentials)
	 */
	@Override
	public IUserProperties determineProperties(ICredentials credentials) {
		return new EmptyUserProperties();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.authentication.IAuthenticationStrategy#determineIdentity(name.martingeisse.common.security.authentication.IUserProperties)
	 */
	@Override
	public IUserIdentity determineIdentity(IUserProperties properties) {
		return SINGLETON_USER_IDENTITY;
	}

}
