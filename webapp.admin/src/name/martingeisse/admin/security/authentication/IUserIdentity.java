/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.security.authentication;

import name.martingeisse.wicket.security.authentication.IUserProperties;

/**
 * This is a marker interface for type safety, used for a user
 * whose identity is known after authentication.
 */
public interface IUserIdentity extends IUserProperties {
}
