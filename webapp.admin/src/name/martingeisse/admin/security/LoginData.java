/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.security;

import name.martingeisse.admin.security.authentication.IUserIdentity;
import name.martingeisse.admin.security.authentication.IUserProperties;
import name.martingeisse.admin.security.authorization.IPermissions;
import name.martingeisse.admin.security.credentials.ICredentials;
import name.martingeisse.common.util.ParameterUtil;

/**
 * An instance of this class in the session indicates that the
 * user is logged in, his/her purged credentials, known and
 * trusted user properties, and if known, the user's identity.
 * If, on the other hand, the session does not contain an
 * instance of this class, then the user is not logged in.
 */
public final class LoginData {

	/**
	 * the credentials
	 */
	private final ICredentials credentials;

	/**
	 * the userProperties
	 */
	private final IUserProperties userProperties;

	/**
	 * the userIdentity
	 */
	private final IUserIdentity userIdentity;
	
	/**
	 * the permissions
	 */
	private final IPermissions permissions;

	/**
	 * Constructor.
	 * @param credentials the user's credentials
	 * @param userProperties the user's properties
	 * @param userIdentity the user's identity (may be null)
	 * @param permissions the user's permissions
	 */
	public LoginData(final ICredentials credentials, final IUserProperties userProperties, final IUserIdentity userIdentity, final IPermissions permissions) {
		this.credentials = ParameterUtil.ensureNotNull(credentials, "credentials");
		this.userProperties = ParameterUtil.ensureNotNull(userProperties, "userProperties");
		this.userIdentity = ParameterUtil.ensureNotNull(userIdentity, "userIdentity");
		this.permissions = ParameterUtil.ensureNotNull(permissions, "permissions");
	}

	/**
	 * Getter method for the credentials.
	 * @return the credentials
	 */
	public ICredentials getCredentials() {
		return credentials;
	}

	/**
	 * Getter method for the userProperties.
	 * @return the userProperties
	 */
	public IUserProperties getUserProperties() {
		return userProperties;
	}

	/**
	 * Getter method for the userIdentity.
	 * @return the userIdentity
	 */
	public IUserIdentity getUserIdentity() {
		return userIdentity;
	}
	
	/**
	 * Getter method for the permissions.
	 * @return the permissions
	 */
	public IPermissions getPermissions() {
		return permissions;
	}

	/**
	 * Looks for an instance of this class in the web session associated with
	 * the current thread (i.e. with the current HTTP request).
	 * 
	 * Note that this is just an alias for {@link SecurityUtil#getLoginData()}.
	 * 
	 * @return the login data, or null if not logged in
	 */
	public static LoginData get() {
		return SecurityUtil.getLoginData();
	}
	
}
