/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.security.authorization;

import name.martingeisse.admin.security.authentication.IUserIdentity;
import name.martingeisse.admin.security.authentication.IUserProperties;
import name.martingeisse.admin.security.credentials.ICredentials;


/**
 * This authorization strategy returns either {@link UnauthorizedPermissions}
 * or {@link SuperuserPermissions}, depending on whether the user's identity
 * was successfully authenticated.
 */
public final class DefaultAuthorizationStrategy implements IAdminAuthorizationStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.security.IAdminAuthorizationStrategy#determinePermissions(name.martingeisse.admin.application.security.ICredentials, name.martingeisse.admin.application.security.IUserProperties, name.martingeisse.admin.application.security.IUserIdentity)
	 */
	@Override
	public IPermissions determinePermissions(ICredentials credentials, IUserProperties userProperties, IUserIdentity userIdentity) {
		return (userIdentity == null ? new UnauthorizedPermissions() : new SuperuserPermissions());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.security.authorization.IAdminAuthorizationStrategy#checkPermission(name.martingeisse.admin.application.security.authorization.IPermissions, name.martingeisse.admin.application.security.authorization.IPermissionRequest)
	 */
	@Override
	public boolean checkPermission(IPermissions permissions, IPermissionRequest request) {
		return (permissions instanceof SuperuserPermissions);
	}

}
