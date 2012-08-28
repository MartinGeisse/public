/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.jdbc.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import name.martingeisse.restful.jdbc.JdbcConnectionManager;

/**
 * Base class for {@link IReductionQuery} implementations.
 * This class deals with JDBC connections and exception handling.
 * @param <T> the result type
 */
public abstract class AbstractReductionQuery<T> implements IReductionQuery<T> {

	/**
	 * Constructor.
	 */
	public AbstractReductionQuery() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.jdbc.query.IReductionQuery#execute()
	 */
	@Override
	public T execute() {
		try {
			return execute(JdbcConnectionManager.getConnection());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method can be implemented to execute multiple statements.
	 * The default implementation creates a single statement, invokes
	 * execute(connection, statement), then disposes of the single
	 * statement.
	 * 
	 * @param connection the JDBC connection
	 * @return the query result
	 * @throws SQLException on SQL errors
	 */
	protected T execute(Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		try {
			return execute(connection, statement);
		} finally {
			statement.close();
		}
	}

	/**
	 * This method can be implemented to execute a single statement.
	 * The default implementation returns null.
	 * 
	 * @param connection the JDBC connection
	 * @param statement the JDBC statement resource (automatically disposed of by the caller)
	 * @return the query result
	 * @throws SQLException on SQL errors
	 */
	protected T execute(Connection connection, Statement statement) throws SQLException {
		return null;
	}
	
}
