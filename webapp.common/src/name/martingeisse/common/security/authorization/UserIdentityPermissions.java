/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security.authorization;

import name.martingeisse.common.security.authentication.IUserIdentity;

/**
 * Represents the permissions of a user based on his identity. For example,
 * most applications will allow a user to edit his own account details
 * just because it is his own account.
 * 
 * Such applications can return an instace of this class from
 * {@link IAuthorizationStrategy#determinePermissions(name.martingeisse.common.security.credentials.ICredentials, name.martingeisse.common.security.authentication.IUserProperties, IUserIdentity)},
 * either directly (if identity permissions are all the application uses),
 * or as part of a {@link PermissionBundle}.
 */
public final class UserIdentityPermissions implements IPermissions {

	/**
	 * the userIdentity
	 */
	private final IUserIdentity userIdentity;
	
	/**
	 * Constructor.
	 * @param userIdentity the user's identity
	 */
	public UserIdentityPermissions(final IUserIdentity userIdentity) {
		this.userIdentity = userIdentity;
	}
	
	/**
	 * Getter method for the userIdentity.
	 * @return the userIdentity
	 */
	public IUserIdentity getUserIdentity() {
		return userIdentity;
	}
	
}
