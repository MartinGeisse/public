/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides a JDBC connection.
 * 
 * This interface does not specify whether the connection is
 * created on demand or shared between multiple clients. Calling
 * code should therefore not close the connection directly
 * but use the disposeConnection(Connection) method of this interface.
 */
public interface IConnectionProvider {

	/**
	 * Creates a connection.
	 * @return the connection
	 * @throws SQLException on SQL errors
	 */
	public Connection createConnection() throws SQLException;
	
	/**
	 * Disposes of a connection.
	 * @param connection the connection to dispose of
	 * @throws SQLException on SQL errors
	 */
	public void disposeConnection(Connection connection) throws SQLException;
	
}
