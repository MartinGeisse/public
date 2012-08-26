/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.security;

import name.martingeisse.admin.application.security.authentication.IAdminAuthenticationStrategy;
import name.martingeisse.admin.application.security.authentication.IUserIdentity;
import name.martingeisse.admin.application.security.authentication.IUserProperties;
import name.martingeisse.admin.application.security.authentication.UserProperties;
import name.martingeisse.admin.application.security.authorization.IAdminAuthorizationStrategy;
import name.martingeisse.admin.application.security.authorization.IPermissions;
import name.martingeisse.admin.application.security.credentials.ICredentials;
import name.martingeisse.wicket.application.MyWicketSession;

/**
 * Utility methods to deal with the security features of the
 * admin framework.
 */
public class SecurityUtil {

	/**
	 * Prevent instantiation.
	 */
	private SecurityUtil() {
	}

	/**
	 * Attempts to perform a login with the specified credentials.
	 * If successful, the corresponding user properties, user identity
	 * and permissions are associated with the current session.
	 * Otherwise, this method throws an exception but does not cause
	 * any side-effects.
	 * 
	 * TODO: will currently succeed in any case, but result in empty
	 * permissions. make "web login" an authorizable action and check for permission;
	 * throw an exception if denied. Only replace session if successful.
	 * 
	 * Successfully logging in will replace the current HTTP session with a new
	 * one, therefore losing any data from a previously logged
	 * in user. This has two advantages:
	 * - the new user cannot access any data from the previous user
	 * - the new session has a new ID, preventing session fixation attacks
	 * 
	 * @param credentials the credentials used to log in
	 */
	public static void login(final ICredentials credentials) {
		
		// authenticate
		IAdminAuthenticationStrategy authenticationStrategy = SecurityConfigurationUtil.getSecurityConfiguration().getAuthenticationStrategy();
		IUserProperties userProperties = authenticationStrategy.determineProperties(credentials);
		if (userProperties == null) {
			userProperties = new UserProperties();
		}
		IUserIdentity userIdentity = authenticationStrategy.determineIdentity(userProperties);
		if (userIdentity != null) {
			userProperties = userIdentity;
		}
		
		// authorization
		IAdminAuthorizationStrategy authorizationStrategy = SecurityConfigurationUtil.getSecurityConfiguration().getAuthorizationStrategy();
		IPermissions permissions = authorizationStrategy.determinePermissions(credentials, userProperties, userIdentity);
		// TODO check login action
		
		// clear previous session
		MyWicketSession session = MyWicketSession.get();
		session.getDataContainer().clear();
		session.replaceSession();
		
		// establish new session
		LoginData loginData = new LoginData(credentials, userProperties, userIdentity, permissions);
		session.getDataContainer().set(LoginData.class, loginData);
		
	}

	/**
	 * Logs the current user out, clearing and then invalidating
	 * the HTTP session. This method does not, however, perform a
	 * redirect to the login page.
	 */
	public static void logout() {
		MyWicketSession session = MyWicketSession.get();
		session.getDataContainer().clear();
		session.invalidateNow();
	}

	/**
	 * Looks for an instance of this class in the web session associated with
	 * the current thread (i.e. with the current HTTP request).
	 * 
	 * This method is also available as {@link LoginData#get()}.
	 * 
	 * @return the login data, or null if not logged in
	 */
	public static LoginData getLoginData() {
		return MyWicketSession.get().getDataContainer().get(LoginData.class);
	}
	
	/**
	 * Checks whether the currently logged in user is granted permission
	 * for the specified action.
	 * 
	 * @param action the action
	 * @return true if permission is granted, false if permission is denied
	 */
	public static boolean checkPermission(String action) {
		IAdminAuthorizationStrategy authorizationStrategy = SecurityConfigurationUtil.getSecurityConfiguration().getAuthorizationStrategy();
		IPermissions permissions = getLoginData().getPermissions();
		return authorizationStrategy.isActionAllowed(permissions, action);
	}

}
