/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security;

import name.martingeisse.common.security.authentication.IAuthenticationStrategy;
import name.martingeisse.common.security.authorization.IAuthorizationStrategy;

/**
 * This interface hides implementation-specific glue code between
 * the security subsystem and the application.
 */
public interface ISecurityProvider {

	/**
	 * Used by the security subsystem to obtain the authentication strategy.
	 * @return the authentication strategy
	 */
	public IAuthenticationStrategy getAuthenticationStrategy();

	/**
	 * Used by the security subsystem to obtain the authorization strategy.
	 * @return the authorization strategy
	 */
	public IAuthorizationStrategy getAuthorizationStrategy();
	
	/**
	 * Stores the specified login data and performs application-specific "on login"
	 * actions.
	 * @param loginData the login data to store
	 */
	public void onLogin(LoginData loginData);
	
	/**
	 * This method is called by the security subsystem to obtain the login data
	 * associated with the calling thread.
	 * @return the login data, or null if not logged in
	 */
	public LoginData getLoginData();
	
	/**
	 * Deletes the stored login data associated with the calling thread and
	 * performs application-specific "on logout" actions.
	 */
	public void onLogout();
	
}
