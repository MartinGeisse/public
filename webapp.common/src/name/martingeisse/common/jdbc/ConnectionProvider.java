/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The default {@link IConnectionProvider}. This class creates a new
 * connection for each caller.
 */
public final class ConnectionProvider implements IConnectionProvider {

	/**
	 * the url
	 */
	private String url;

	/**
	 * the username
	 */
	private String username;

	/**
	 * the password
	 */
	private String password;

	/**
	 * Constructor.
	 */
	public ConnectionProvider() {
	}

	/**
	 * Getter method for the url.
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Setter method for the url.
	 * @param url the url to set
	 */
	public void setUrl(final String url) {
		this.url = url;
	}

	/**
	 * Getter method for the username.
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter method for the username.
	 * @param username the username to set
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * Getter method for the password.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter method for the password.
	 * @param password the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.jdbc.IConnectionProvider#createConnection()
	 */
	@Override
	public Connection createConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.jdbc.IConnectionProvider#disposeConnection(java.sql.Connection)
	 */
	@Override
	public void disposeConnection(Connection connection) throws SQLException {
		connection.close();
	}

}
