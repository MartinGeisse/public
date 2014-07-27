/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security.credentials;

import java.io.Serializable;


/**
 * Simple username / password pair credentials.
 */
public final class UsernameAndPassword implements ICredentials, Serializable {

	/**
	 * the username
	 */
	private final String username;
	
	/**
	 * the password
	 */
	private transient String password;

	/**
	 * Constructor.
	 * @param username the username
	 * @param password the password
	 */
	public UsernameAndPassword(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Getter method for the username.
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Getter method for the password.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.IPurgeable#purge()
	 */
	@Override
	public void purge() {
		password = null;
	}

}
