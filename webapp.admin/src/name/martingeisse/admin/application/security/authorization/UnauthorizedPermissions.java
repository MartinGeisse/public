/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.security.authorization;

/**
 * This is a simple permissions object that can be used to represent
 * the permissions of users that have not otherwise provided any
 * useful credentials, or do not have any additional permissions.
 * That is, it represents the "lowest permissions level" in the
 * system.
 * 
 * The authorization strategy specifies which concrete actions are
 * allowed for this permissions object. 
 */
public final class UnauthorizedPermissions implements IPermissions {
}
