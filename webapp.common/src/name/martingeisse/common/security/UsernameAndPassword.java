/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security;

import name.martingeisse.common.security.authentication.IUserProperties;
import name.martingeisse.common.security.credentials.ICredentials;

/**
 * Simple username / password pair. Can be used as credentials
 * or as known user properties.
 */
public final class UsernameAndPassword implements ICredentials, IUserProperties {

	/**
	 * the username
	 */
	private final String username;
	
	/**
	 * the password
	 */
	private String password;

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
