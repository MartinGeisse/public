/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * This class keeps, per thread, a lazily created JDBC connection.
 * Upon first use, the connection is created.
 * The connection is stored until disposed through this class and allows
 * arbitrary clients in the same thread to use it without passing it around.
 */
public class JdbcConnectionManager {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(JdbcConnectionManager.class);
	
	/**
	 * the connectionContainer
	 */
	private static final ThreadLocal<Connection> connectionContainer = new ThreadLocal<Connection>();

	/**
	 * Returns the cached connection for the current thread, or null if no
	 * connection is cached (yet).
	 * @return the cached connection for the current thread
	 */
	public static Connection getCachedConnection() {
		return connectionContainer.get();
	}

	/**
	 * Returns the connection for the current thread, creating and caching it
	 * if it does not yet exist.
	 * @return the connection for the current thread
	 */
	public static Connection getConnection() {
		Connection connection = connectionContainer.get();
		if (connection == null) {
			try {
				connection = DriverManager.getConnection("jdbc:postgresql://localhost/leckerMittag", "postgres", "postgres");
			} catch (SQLException e) {
				throw new RuntimeException("Could not create SQL connection");
			}
			connectionContainer.set(connection);
		}
		return connection;
	}
	
	/**
	 * If any unfinished transaction exists in the connection,
	 * rolls it back.
	 */
	public static void rollbackAnyUnfinishedTransactions() {
		logger.debug("starting rollback...");
		Connection connection = connectionContainer.get();
		try {
			if (connection != null && !connection.getAutoCommit()) {
				connection.rollback();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Could not rollback SQL statements");
		}
		logger.debug("rollback finished");
	}

	/**
	 * Disposes of the connection for the current thread, rolling
	 * back any active transaction prior to closing. Does nothing
	 * if no connection is cached for the current thread.
	 */
	public static void disposeConnection() {
		try {
			logger.debug("disposing of connection");
			Connection connection = connectionContainer.get();
			if (connection != null) {
				logger.trace("connection exists, will dispose of it now");
				try {
					try {
						logger.trace("checking for active transaction");
						if (!connection.getAutoCommit()) {
							logger.trace("autocommit off, going to rollback now");
							connection.rollback();
							logger.trace("rollback finished");
						}
						logger.trace("no more active transactions");
					} finally {
						logger.trace("about to close connection");
						connection.close();
						logger.trace("connection closed");
					}
				} finally {
					logger.trace("about to remove thread-local connection reference");
					connectionContainer.remove();
					logger.trace("thread-local connection reference removed");
				}
			} else {
				logger.trace("no connection found");
			}
			logger.debug("finished disposing of connection");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
