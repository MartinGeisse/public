/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security.authentication;

import name.martingeisse.common.security.credentials.ICredentials;


/**
 * This strategy serves two purposes:
 * - to find authenticated user properties based on credentials
 * - to determine a user's identity based on such properties
 * 
 * When a user is being authenticated, he/she presents certain credentials,
 * represented as an {@link ICredentials} object. The credentials
 * object is opaque to the admin framework. This strategy analyzes
 * the credentials to determine properties of the user whose values can
 * be trusted by the admin application. Such properties are returned as
 * {@link IUserProperties}. Which properties can be trusted depends on the
 * credentials; in the extreme case, the credentials use an unknown
 * format, and no trusted properties can be determined.
 * 
 * As the next step, the same properties are passed to the second method
 * to determine the user's identity. Depending on the properties available,
 * this may or may not succeed. If it does succeed, however, then the
 * returned {@link IUserIdentity} behaves in the following way:
 * 
 * - the properties returned from the identity shall be the same as the
 *   properties used to find the identity, or at worst shall be outdated
 *   or erroneous information. This allows the admin framework to use the
 *   returned user identity for all purposes, and discard the initial
 *   properties. The returned identity is assumed to have a more complete,
 *   more up-to-date set of properties.
 *   
 * - the properties returned from the identity shall be complete in the
 *   sense that they either contain anything that can be said about
 *   the identity (in the context of the admin application), or can be
 *   used to obtain all such information. It shall not be possible that
 *   the admin framework holds information about the same identity that
 *   can only be found using other credentials (notwithstanding the
 *   question of whether the current credentials give *permission* to
 *   access that information).
 *   
 * - the returned identity is unique: There is no other identity in the
 *   admin application with exactly the same property values. Each
 *   smallest subset of properties that is unique among all possible
 *   identities (according to the current database schema) is an
 *   *identifier* for the user.
 *
 * If no user identity can be found for a set of properties, then
 * the admin application continues with just those properties. An
 * example would be a user that is previously unknown to the system,
 * but has gone through validation of his email address.
 */
public interface IAdminAuthenticationStrategy {

	/**
	 * Determines user properties from the specified credentials.
	 * @param credentials the credentials provided by the user, never null
	 * @return the user's trusted properties. May return null instead
	 * of an empty properties object to indicate no known trusted
	 * properties.
	 */
	public IUserProperties determineProperties(ICredentials credentials);

	/**
	 * Determines a user's identity from the specified trusted properties.
	 * NOTE: Implementation cannot rely on the properties to be exactly
	 * the same as returned from determineProperties(); there may
	 * be other code calling this code with other properties!
	 * 
	 * @param properties the set of trusted properties, never null
	 * @return the identity, or null to indicate that the properties
	 * are not known, or are not sufficient to determine the
	 * user's identity.
	 */
	public IUserIdentity determineIdentity(IUserProperties properties);
	
}
