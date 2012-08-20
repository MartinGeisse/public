/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.database;

import java.sql.Connection;
import java.sql.SQLException;

import name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor;

import org.apache.log4j.Logger;

/**
 * This class keeps, per thread, a lazily created JDBC connection. Upon first use,
 * the connection is created from a central database descriptor that, in turn must
 * be initialized once at startup by the application. The connection is stored until
 * disposed through this class -- for example by a {@link JdbcConnectionServletFilter} --
 * and allows arbitrary clients in the same thread to use it without passing
 * it around.
 */
public class JdbcConnectionManager {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(JdbcConnectionManager.class);
	
	/**
	 * the databaseDescriptor
	 */
	private static AbstractDatabaseDescriptor databaseDescriptor;
	
	/**
	 * the connectionContainer
	 */
	private static final ThreadLocal<Connection> connectionContainer = new ThreadLocal<Connection>();
	
	/**
	 * Initializes the database descriptor. This method must be called once at startup
	 * before any other use of this class.
	 * @param databaseDescriptor the database descriptor to use
	 */
	public static synchronized void initializeDatabaseDescriptor(AbstractDatabaseDescriptor databaseDescriptor) {
		if (JdbcConnectionManager.databaseDescriptor != null) {
			throw new IllegalStateException("database descriptor is already initialized");
		}
		JdbcConnectionManager.databaseDescriptor = databaseDescriptor;
	}
	
	/**
	 * Getter method for the database descriptor.
	 * @return the database descriptor
	 */
	public static synchronized AbstractDatabaseDescriptor getDatabaseDescriptor() {
		return databaseDescriptor;
	}

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
		try {
			Connection connection = connectionContainer.get();
			if (connection == null) {
				connection = getDatabaseDescriptor().createConnection();
				if (!connection.getAutoCommit()) {
					throw new RuntimeException("new connection is not in auto-commit mode");
				}
				connectionContainer.set(connection);
			}
			return connection;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Begins a transaction in the connection for the current thread. Throws an exception
	 * if any unexpected condition occurs, including that a transaction was already running.
	 */
	public static void begin() {
		try {
			Connection connection = getConnection();
			if (!connection.getAutoCommit()) {
				throw new IllegalStateException("A transaction was already running");
			}
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Commits the transaction in the connection for the current thread. Throws an exception
	 * if any unexpected condition occurs, including that no transaction was running.
	 */
	public static void commit() {
		try {
			Connection connection = getConnection();
			if (connection.getAutoCommit()) {
				throw new IllegalStateException("No transaction is running");
			}
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Commits the transaction in the connection for the current thread and immediately
	 * starts another transaction. Throws an exception
	 * if any unexpected condition occurs, including that no transaction was running.
	 */
	public static void commitBegin() {
		try {
			Connection connection = getConnection();
			if (connection.getAutoCommit()) {
				throw new IllegalStateException("No transaction is running");
			}
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Rolls back the transaction in the connection for the current thread. Throws an exception
	 * if any unexpected condition occurs, including that no transaction was running.
	 */
	public static void rollback() {
		try {
			Connection connection = getConnection();
			if (connection.getAutoCommit()) {
				throw new IllegalStateException("No transaction is running");
			}
			connection.rollback();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Tries to roll back the transaction in the connection for the current thread.
	 * Returns true if a transaction was running, false if none was running.
	 * Throws an exception if any unexpected condition occurs.
	 * @return whether a transaction was running
	 */
	public static boolean tryRollback() {
		try {
			Connection connection = getConnection();
			boolean wasTransactionRunning = !connection.getAutoCommit();
			if (wasTransactionRunning) {
				connection.rollback();
				connection.setAutoCommit(true);
			}
			return wasTransactionRunning;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Disposes of the connection for the current thread. Does nothing
	 * if no connection is cached for the current thread. Throws an exception
	 * if the connection has an open transaction.
	 */
	public static void disposeConnection() {
		logger.debug("disposing of connection");
		Connection connection = connectionContainer.get();
		if (connection != null) {
			logger.trace("connection exists, will dispose of it now");
			try {
				try {
					logger.trace("checking for active transaction");
					if (tryRollback()) {
						logger.debug("found an uncommitted transaction");
					}
					logger.trace("no more active transactions");
				} finally {
					logger.trace("about to close connection");
					connection.close();
					logger.trace("connection closed");
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				logger.trace("about to remove thread-local connection reference");
				connectionContainer.remove();
				logger.trace("thread-local connection reference removed");
			}
		} else {
			logger.trace("no connection found");
		}
		logger.debug("finished disposing of connection");
	}
	
}
