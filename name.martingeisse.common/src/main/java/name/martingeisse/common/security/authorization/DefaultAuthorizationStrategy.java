/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security.authorization;

import name.martingeisse.common.security.authentication.IUserIdentity;
import name.martingeisse.common.security.authentication.IUserProperties;
import name.martingeisse.common.security.credentials.ICredentials;


/**
 * This authorization strategy returns either {@link UnauthorizedPermissions}
 * or {@link SuperuserPermissions}, depending on whether the user's identity
 * was successfully authenticated.
 */
public final class DefaultAuthorizationStrategy implements IAuthorizationStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.authorization.IAuthorizationStrategy#determinePermissions(name.martingeisse.common.security.credentials.ICredentials, name.martingeisse.common.security.authentication.IUserProperties, name.martingeisse.common.security.authentication.IUserIdentity)
	 */
	@Override
	public IPermissions determinePermissions(ICredentials credentials, IUserProperties userProperties, IUserIdentity userIdentity) {
		return (userIdentity == null ? new UnauthorizedPermissions() : new SuperuserPermissions());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.authorization.IAuthorizationStrategy#checkPermission(name.martingeisse.common.security.authorization.IPermissions, name.martingeisse.common.security.authorization.IPermissionRequest)
	 */
	@Override
	public boolean checkPermission(IPermissions permissions, IPermissionRequest request) {
		return (permissions instanceof SuperuserPermissions);
	}

}
