/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class connects to a JDBC database.
 */
public class JdbcDataSource {

	/**
	 * the url
	 */
	private final String url;

	/**
	 * the username
	 */
	private final String username;

	/**
	 * the password
	 */
	private final String password;

	/**
	 * the connection
	 */
	private Connection connection;

	/**
	 * Constructor.
	 * @param url the database URL
	 * @param username the username to log in to the database
	 * @param password the password to log in to the database
	 */
	public JdbcDataSource(final String url, final String username, final String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	/**
	 * Getter method for the url.
	 * @return the url
	 */
	public String getUrl() {
		return url;
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

	/**
	 * Getter method for the connection.
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Connects to the database.
	 * @throws SQLException on SQL errors
	 */
	public void connect() throws SQLException {
		this.connection = DriverManager.getConnection(url, username, password);
	}

	/**
	 * Disconnects from the database.
	 * @throws SQLException on SQL errors
	 */
	public void disconnect() throws SQLException {
		this.connection.close();
		this.connection = null;
	}
	
}
