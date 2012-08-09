/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysema.query.sql.SQLQuery;

/**
 * This class describes a database used by the application.
 */
public abstract class AbstractDatabaseDescriptor {

	/**
	 * the displayName
	 */
	private String displayName;

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
	public AbstractDatabaseDescriptor() {
	}

	/**
	 * Getter method for the displayName.
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Setter method for the displayName.
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
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

	/**
	 * Creates an SQL connection to this database
	 * @return the connection
	 * @throws SQLException on SQL errors
	 */
	public Connection createConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	/**
	 * Creates a QueryDSL {@link SQLQuery} object for this database.
	 * @param connection the database connection
	 * @return the query
	 */
	public abstract SQLQuery createQuery(Connection connection);
	
}
