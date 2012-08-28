/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.database;

import java.sql.Connection;
import java.sql.SQLException;

import name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor;

import org.apache.log4j.Logger;

import com.mysema.query.sql.SQLQuery;

/**
 * JDBC-based implementation of {@link IEntityDatabaseConnection}.
 */
public class JdbcEntityDatabaseConnection implements IEntityDatabaseConnection {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(JdbcEntityDatabaseConnection.class);
	
	/**
	 * the database
	 */
	private AbstractDatabaseDescriptor database;
	
	/**
	 * the jdbcConnection
	 */
	private Connection jdbcConnection;
	
	/**
	 * Constructor.
	 * @param database the database
	 */
	public JdbcEntityDatabaseConnection(AbstractDatabaseDescriptor database) {
		this.database = database;
		try {
			this.jdbcConnection = database.createJdbcConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Getter method for the database.
	 * @return the database
	 */
	public AbstractDatabaseDescriptor getDatabase() {
		return database;
	}
	
	/**
	 * Getter method for the jdbcConnection.
	 * @return the jdbcConnection
	 */
	public Connection getJdbcConnection() {
		return jdbcConnection;
	}
	
	/**
	 * Expects a running transaction, i.e. throws an {@link IllegalStateException}
	 * if no running transaction can be detected.
	 * @throws SQLException on SQL errors
	 */
	private final void expectRunningTransaction() throws SQLException {
		if (jdbcConnection.getAutoCommit()) {
			logger.error("no running transaction detected");
			throw new IllegalStateException("No transaction is running");
		}
	}

	/**
	 * Expects no running transaction, i.e. throws an {@link IllegalStateException}
	 * if a running transaction can be detected.
	 * @throws SQLException on SQL errors
	 */
	private final void expectNoRunningTransaction() throws SQLException {
		if (!jdbcConnection.getAutoCommit()) {
			logger.error("running transaction detected");
			throw new IllegalStateException("A transaction was already running");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.database.IEntityDatabaseConnection#isTransactionRunning()
	 */
	@Override
	public boolean isTransactionRunning() {
		try {
			logger.trace("isTransactionRunning() called...");
			boolean autocommit = jdbcConnection.getAutoCommit();
			logger.trace("isTransactionRunning() finished");
			return !autocommit;
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.database.IEntityDatabaseConnection#begin()
	 */
	@Override
	public void begin() {
		try {
			logger.trace("begin() called...");
			expectNoRunningTransaction();
			jdbcConnection.setAutoCommit(false);
			logger.debug("begin() finished");
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.database.IEntityDatabaseConnection#commit()
	 */
	@Override
	public void commit() {
		try {
			logger.trace("commit() called...");
			expectRunningTransaction();
			jdbcConnection.setAutoCommit(true);
			logger.debug("commit() finished");
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.database.IEntityDatabaseConnection#commitBegin()
	 */
	@Override
	public void commitBegin() {
		try {
			logger.trace("commitBegin() called...");
			expectRunningTransaction();
			jdbcConnection.commit();
			logger.debug("commitBegin() finished");
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.database.IEntityDatabaseConnection#rollback()
	 */
	@Override
	public void rollback() {
		try {
			logger.trace("rollback() called...");
			expectRunningTransaction();
			jdbcConnection.rollback();
			jdbcConnection.setAutoCommit(true);
			logger.debug("rollback() finished");
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.database.IEntityDatabaseConnection#rollbackBegin()
	 */
	@Override
	public void rollbackBegin() {
		try {
			logger.trace("rollbackBegin() called...");
			expectRunningTransaction();
			jdbcConnection.rollback();
			logger.debug("rollbackBegin() finished");
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.database.IEntityDatabaseConnection#dispose()
	 */
	@Override
	public void dispose() {
		try {
			logger.trace("checking for active transaction...");
			try {
				if (!jdbcConnection.getAutoCommit()) {
					logger.debug("found an uncommitted transaction. rolling back...");
					jdbcConnection.rollback();
					logger.debug("rollback finished");
				}
			} catch (SQLException e) {
				logger.error("an exception occurred during forced rollback", e);
			}
			logger.trace("checking for active transaction finished");
		} finally {
			logger.trace("about to close connection");
			try {
				jdbcConnection.close();
			} catch (SQLException e) {
				logger.error("an exception occurred while closing the JDBC connection", e);
			}
			logger.debug("connection closed");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.database.IEntityDatabaseConnection#createQuery()
	 */
	@Override
	public SQLQuery createQuery() {
		return database.createQuery(jdbcConnection);
	}
	
}