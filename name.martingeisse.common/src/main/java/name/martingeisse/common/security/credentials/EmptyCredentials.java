/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security.credentials;

import java.io.Serializable;

/**
 * Simple implementation of {@link ICredentials} that does not
 * contain any data.
 */
public final class EmptyCredentials implements ICredentials, Serializable {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.credentials.ICredentials#purge()
	 */
	@Override
	public void purge() {
	}

}
