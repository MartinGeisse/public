/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admon.security.authorization;

/**
 * This is a simple permissions object that can be used to represent
 * the permissions of users that have not otherwise provided any
 * useful credentials, or do not have any additional permissions.
 * That is, it represents the "lowest permissions level" in the
 * system.
 * 
 * This class has a special meaning to the security system: If
 * the user is not logged in, then the system implicitly assumes
 * "unauthorized permissions".
 * 
 * The authorization strategy specifies which concrete requests
 * for permission are granted for this permissions object. 
 */
public final class UnauthorizedPermissions implements IPermissions {
	
	/**
	 * Shared instance of this class. Note that this is not guaranteed
	 * to be the only instance of this class.
	 */
	public static final UnauthorizedPermissions INSTANCE = new UnauthorizedPermissions();
	
}
