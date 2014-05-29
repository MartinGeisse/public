/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security;

import name.martingeisse.common.security.authentication.EmptyUserProperties;
import name.martingeisse.common.security.authentication.IAuthenticationStrategy;
import name.martingeisse.common.security.authentication.IUserIdentity;
import name.martingeisse.common.security.authentication.IUserProperties;
import name.martingeisse.common.security.authorization.CorePermissionRequest;
import name.martingeisse.common.security.authorization.IAuthorizationStrategy;
import name.martingeisse.common.security.authorization.IPermissionRequest;
import name.martingeisse.common.security.authorization.IPermissions;
import name.martingeisse.common.security.authorization.LoginFailedException;
import name.martingeisse.common.security.authorization.PermissionDeniedException;
import name.martingeisse.common.security.authorization.UnauthorizedPermissions;
import name.martingeisse.common.security.credentials.ICredentials;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.ReturnValueUtil;
import org.apache.log4j.Logger;

/**
 * Utility methods to deal with the security features of the
 * application.
 */
public class SecurityUtil {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(SecurityUtil.class);

	/**
	 * the provider
	 */
	private static ISecurityProvider provider;

	/**
	 * Prevent instantiation.
	 */
	private SecurityUtil() {
	}

	/**
	 * Initializes the security subsystem.
	 * @param provider the implementation-specific glue code provider
	 */
	public static void initialize(final ISecurityProvider provider) {
		SecurityUtil.provider = provider;
	}

	/**
	 * Attempts to perform a login with the specified credentials.
	 * If successful, the corresponding user properties, user identity
	 * and permissions are associated with the current session.
	 * Otherwise, this method throws an exception but does not cause
	 * any side-effects.
	 * 
	 * Logging in performs the following steps:
	 * - determine user properties using the supplied credentials (authentication step 1)
	 * - try to determine the user's identity (authentication step 2)
	 * - determine the user's permissions (authorization step 1)
	 * - check the user's login permission ({@link name.martingeisse.common.security.authorization.CorePermissionRequest.Type#LOGIN})
	 *   (authorization step 2).
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
		ParameterUtil.ensureNotNull(credentials, "credentials");
		logger.debug("a user is trying to log in");

		// authenticate
		final IAuthenticationStrategy authenticationStrategy = provider.getAuthenticationStrategy();
		logger.trace("authentication strategy: " + authenticationStrategy);
		ReturnValueUtil.nullMeansMissing(authenticationStrategy, "security configuration: authentication strategy");
		IUserProperties userProperties = authenticationStrategy.determineProperties(credentials);
		if (userProperties == null) {
			userProperties = new EmptyUserProperties();
		}
		logger.debug("detected user properties: " + userProperties);
		final IUserIdentity userIdentity = authenticationStrategy.determineIdentity(userProperties);
		logger.debug("detected user identity: " + userIdentity);

		// authorization
		final IAuthorizationStrategy authorizationStrategy = provider.getAuthorizationStrategy();
		logger.trace("authorization strategy: " + authorizationStrategy);
		ReturnValueUtil.nullMeansMissing(authorizationStrategy, "security configuration: authorization strategy");
		final IPermissions permissions = authorizationStrategy.determinePermissions(credentials, userProperties, userIdentity);
		logger.debug("permissions: " + permissions);
		ReturnValueUtil.nullMeansMissing(permissions, "permissions returned by the authorization strategy");
		final IPermissionRequest loginPermissionRequest = new CorePermissionRequest(CorePermissionRequest.Type.LOGIN);
		logger.trace("checking permission: " + loginPermissionRequest);
		if (!authorizationStrategy.checkPermission(permissions, loginPermissionRequest)) {
			logger.debug("login permission denied");
			throw new LoginFailedException(permissions, loginPermissionRequest);
		}
		logger.trace("login permission granted");

		// purge sensitive data that is now irrelevant
		logger.trace("purging sensitive data");
		credentials.purge();
		userProperties.purge();
		if (userIdentity != null) {
			userIdentity.purge();
		}
		logger.info("sensitive data purged");

		// store the login data in the session
		logger.trace("performing on-login actions");
		final LoginData loginData = new LoginData(credentials, userProperties, userIdentity, permissions);
		provider.onLogin(loginData);
		logger.info("user logged in");

	}

	/**
	 * Logs the current user out, clearing and then invalidating
	 * the HTTP session. This method does not, however, perform a
	 * redirect to the login page.
	 */
	public static void logout() {
		logger.debug("a user is trying to log out");
		provider.onLogout();
		logger.info("user logged out");
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
		return provider.getLoginData();
	}

	/**
	 * Checks whether the user is currently logged in. This is the case if
	 * and only if getLoginData() returns a non-null value,
	 * 
	 * @return the login data, or null if not logged in
	 */
	public static boolean isLoggedIn() {
		return (getLoginData() != null);
	}

	/**
	 * Returns the permissions of the logged in user, or the shared
	 * {@link UnauthorizedPermissions} instance if the user is not logged in.
	 * @return the effective permissions
	 */
	public static IPermissions getEffectivePermissions() {
		final LoginData loginData = getLoginData();
		return (loginData == null ? UnauthorizedPermissions.INSTANCE : loginData.getPermissions());
	}

	/**
	 * Checks whether the currently logged in user is granted the specified
	 * request for permission, and returns the result.
	 * 
	 * @param request the request
	 * @return true if permission is granted, false if permission is denied
	 */
	public static boolean getPermission(final IPermissionRequest request) {
		logger.debug("checking permission: " + request);
		final IAuthorizationStrategy authorizationStrategy = provider.getAuthorizationStrategy();
		ReturnValueUtil.nullMeansMissing(authorizationStrategy, "security configuration: authorization strategy");
		final IPermissions permissions = getEffectivePermissions();
		final boolean result = authorizationStrategy.checkPermission(permissions, request);
		logger.debug((result ? "permission granted: " : "permission denied: ") + request);
		return result;
	}

	/**
	 * Checks whether the currently logged in user is granted the specified
	 * request for permission, and throws an exception if the permission
	 * was denied.
	 * 
	 * This is just a utility method. For example, callers that want to throw
	 * a more specific exception in case of missing permissions should call
	 * {@link #getPermission(IPermissionRequest)} directly.
	 * 
	 * @param request the request
	 */
	public static void enforcePermission(final IPermissionRequest request) {
		if (!getPermission(request)) {
			throw new PermissionDeniedException(getEffectivePermissions(), request);
		}
	}

}
