/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.security.credentials;

import name.martingeisse.wicket.security.credentials.ICredentials;

/**
 * Simple implementation of {@link ICredentials} that does not
 * contain any data.
 */
public final class EmptyCredentials implements ICredentials {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.security.credentials.ICredentials#purge()
	 */
	@Override
	public void purge() {
	}

}
