/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * This class encapsulates the data sources used to fill the
 * report with data.
 */
public class DataSources extends HashMap<String, JdbcDataSource> {

	/**
	 * Constructor.
	 */
	public DataSources() {
	}
	
	/**
	 * Connects to the database.
	 * @throws SQLException on SQL errors
	 */
	public void connect() throws SQLException {
		for (Map.Entry<String, JdbcDataSource> entry : entrySet()) {
			entry.getValue().connect();
		}
	}

	/**
	 * Disconnects from the database.
	 * @throws SQLException on SQL errors
	 */
	public void disconnect() throws SQLException {
		for (Map.Entry<String, JdbcDataSource> entry : entrySet()) {
			entry.getValue().disconnect();
		}
	}

	/**
	 * Returns the connection for the specified key.
	 * @param key the key
	 * @return the connection
	 */
	public Connection getConnection(String key) {
		return get(key).getConnection();
	}

	/**
	 * Returns the statement for the specified key.
	 * @param key the key
	 * @return the statement
	 */
	public Statement getStatement(String key) {
		return get(key).getStatement();
	}
	
}
