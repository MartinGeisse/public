/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security.authorization;

import name.martingeisse.common.security.SecurityUtil;

/**
 * This exception type is thrown by {@link SecurityUtil#enforcePermission(IPermissionRequest)}
 * when the permission was denied.
 */
public class PermissionDeniedException extends RuntimeException {

	/**
	 * the permissions
	 */
	private final IPermissions permissions;

	/**
	 * the request
	 */
	private final IPermissionRequest request;

	/**
	 * Constructor.
	 * @param permissions the permissions set of the user
	 * @param request the request that was denied
	 */
	public PermissionDeniedException(final IPermissions permissions, final IPermissionRequest request) {
		super("permission denied; permissions: " + permissions + "; request: " + request);
		this.permissions = permissions;
		this.request = request;
	}

	/**
	 * Getter method for the permissions.
	 * @return the permissions
	 */
	public IPermissions getPermissions() {
		return permissions;
	}
	
	/**
	 * Getter method for the request.
	 * @return the request
	 */
	public IPermissionRequest getRequest() {
		return request;
	}
	
}
