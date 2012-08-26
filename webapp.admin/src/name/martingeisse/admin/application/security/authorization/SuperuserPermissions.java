/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.security.authorization;

/**
 * This is a simple permissions object that can be used to represent
 * the permissions of a "super-user" without any denied actions.
 * That is, it represents the "highest permissions level" in systems
 * that have such a permission level at all.
 * 
 * Authorization strategies should either allow all actions for
 * this permissions object (for systems that have a "super-user")
 * or deny all actions for this permissions object (for systems that
 * do not have "super-users").
 */
public final class SuperuserPermissions implements IPermissions {
}
