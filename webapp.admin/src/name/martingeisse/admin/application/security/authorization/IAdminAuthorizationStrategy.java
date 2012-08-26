/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.security.authorization;

import name.martingeisse.admin.application.security.authentication.IUserIdentity;
import name.martingeisse.admin.application.security.authentication.IUserProperties;
import name.martingeisse.admin.application.security.credentials.ICredentials;

/**
 * This strategy serves two purposes:
 * - to determine a user's permissions based on his/her identity, known properties,
 *   and supplied credentials
 * - to determine whether a specific action is allowed for a permission descriptor
 * 
 * After a user has authenticated, we have the information needed to determine
 * his/her permissions, i.e. set of allowed actions:
 * - the supplied credentials
 * - a (possibly empty) set of trusted properties of the user
 * - if determined successfully, the user's identity
 * 
 * Note that if the user's identity has been authenticated successfully, then the
 * initial set of properties is discarded and the user's up-to-date and complete
 * properties are loaded using the identity.
 * 
 * Based on this information, an {@link IPermissions} object is created that
 * represents the user's set of allowed actions within the admin application.
 * This object is opaque to the framework, therefore this strategy is also
 * needed to check for specific actions whether they are allowed.
 */
public interface IAdminAuthorizationStrategy {

	/**
	 * Determines the user's permissions.
	 * @param credentials the credentials supplied by the user
	 * @param userProperties the known trusted properties of the user, never null
	 * @param userIdentity the user's identity, or null if not successfully determined
	 * @return the user's permissions, never null
	 */
	public IPermissions determinePermissions(ICredentials credentials, IUserProperties userProperties, IUserIdentity userIdentity);

	/**
	 * Checks whether the specified concrete action is allowed when the user has the
	 * specified permissions.
	 * @param permissions the user's permissions
	 * @param action the action
	 * @return true if allowed, false if denied
	 */
	public boolean isActionAllowed(IPermissions permissions, String action);
	
}
