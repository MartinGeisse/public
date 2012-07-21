/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An {@link IConnectionProvider} that returns the same connection
 * for all callers.
 */
public final class SharedConnectionProvider implements IConnectionProvider {

	/**
	 * the connection
	 */
	private Connection connection;

	/**
	 * Constructor.
	 * @param connection the connection
	 */
	public SharedConnectionProvider(final Connection connection) {
		this.connection = connection;
	}

	/**
	 * Getter method for the connection.
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Setter method for the connection.
	 * @param connection the connection to set
	 */
	public void setConnection(final Connection connection) {
		this.connection = connection;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.jdbc.IConnectionProvider#createConnection()
	 */
	@Override
	public Connection createConnection() {
		return connection;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.jdbc.IConnectionProvider#disposeConnection(java.sql.Connection)
	 */
	@Override
	public void disposeConnection(Connection connection) {
	}

	/**
	 * Actually closes the connection.
	 * @throws SQLException on SQL errors
	 */
	public void closeConnection() throws SQLException {
		connection.close();
		connection = null;
	}
	
}
