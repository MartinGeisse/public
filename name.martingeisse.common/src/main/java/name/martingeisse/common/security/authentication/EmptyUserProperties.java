/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security.authentication;

import java.io.Serializable;

/**
 * Empty {@link IUserProperties} implementation for the case that
 * nothing is known about the user beyond the supplied
 * credentials.
 */
public final class EmptyUserProperties implements IUserProperties, Serializable {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.IPurgeable#purge()
	 */
	@Override
	public void purge() {
	}

}
