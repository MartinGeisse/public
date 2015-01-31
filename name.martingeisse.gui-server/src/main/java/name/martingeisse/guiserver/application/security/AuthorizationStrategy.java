/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.application.security;

import name.martingeisse.common.security.authentication.IUserIdentity;
import name.martingeisse.common.security.authentication.IUserProperties;
import name.martingeisse.common.security.authorization.IAuthorizationStrategy;
import name.martingeisse.common.security.authorization.IPermissionRequest;
import name.martingeisse.common.security.authorization.IPermissions;
import name.martingeisse.common.security.authorization.PermissionBundle;
import name.martingeisse.common.security.authorization.UnauthorizedPermissions;
import name.martingeisse.common.security.credentials.ICredentials;

/**
 * GUI-server-specific authorization strategy.
 */
public class AuthorizationStrategy implements IAuthorizationStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.authorization.IAuthorizationStrategy#determinePermissions(name.martingeisse.common.security.credentials.ICredentials, name.martingeisse.common.security.authentication.IUserProperties, name.martingeisse.common.security.authentication.IUserIdentity)
	 */
	@Override
	public IPermissions determinePermissions(ICredentials credentials, IUserProperties userProperties, IUserIdentity userIdentity) {
		return UnauthorizedPermissions.INSTANCE;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.authorization.IAuthorizationStrategy#checkPermission(name.martingeisse.common.security.authorization.IPermissions, name.martingeisse.common.security.authorization.IPermissionRequest)
	 */
	@Override
	public boolean checkPermission(IPermissions permissions, IPermissionRequest request) {
		
		// support bundles; grant permission if any bundle element is sufficient
		if (permissions instanceof PermissionBundle) {
			PermissionBundle bundle = (PermissionBundle)permissions;
			for (IPermissions element : bundle) {
				if (checkPermission(element, request)) {
					return true;
				}
			}
		}
		
		return false;
	}

}
