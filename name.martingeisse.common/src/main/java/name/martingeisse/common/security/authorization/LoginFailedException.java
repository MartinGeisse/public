/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security.authorization;

/**
 * A specific exception that is thrown when a login attempt fails.
 */
public class LoginFailedException extends PermissionDeniedException {

	/**
	 * Constructor.
	 * @param permissions the user's permissions
	 * @param request the failed permission request
	 */
	public LoginFailedException(final IPermissions permissions, final IPermissionRequest request) {
		super(permissions, request);
	}

}
