/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.database;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.admin.entity.schema.database.IDatabaseDescriptor;

import org.apache.log4j.Logger;

/**
 * This class keeps, per thread, a lazily created {@link IEntityDatabaseConnection}.
 * Upon first use, the connection is created from one of the central database
 * descriptors which, in turn, must be initialized once at startup by the application.
 * The connection is stored until disposed through this class -- for example by a
 * {@link EntityConnectionServletFilter} -- and allows arbitrary clients in the same
 * thread to use it without passing it around.
 * 
 * This class stores all database descriptors, and also keeps one of them as the
 * default descriptor. The latter is used when no explicit descriptor is specified
 * to a method. Any other descriptor must be specified explicitly. They cannot be
 * specified by name; the database descriptor itself must be passed to methods.
 * This adds safety against changes and mistyped names. It is recommended that
 * applications keep a simple container class with all database descriptors in
 * static variables for quick access.
 */
public class EntityConnectionManager {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(EntityConnectionManager.class);

	/**
	 * the databaseDescriptors
	 */
	private static IDatabaseDescriptor[] databaseDescriptors;

	/**
	 * the defaultDatabaseDescriptor
	 */
	private static IDatabaseDescriptor defaultDatabaseDescriptor;

	/**
	 * the connectionContainer
	 */
	private static final ThreadLocal<Map<IDatabaseDescriptor, IEntityDatabaseConnection>> connectionContainer = new ThreadLocal<Map<IDatabaseDescriptor, IEntityDatabaseConnection>>();

	/**
	 * Initializes the database descriptors. This method must be called once at startup
	 * before any other use of this class.
	 * 
	 * The first descriptor is the default descriptor and is used when methods are called without an
	 * explicit descriptor.
	 * 
	 * @param databaseDescriptors the database descriptor to use
	 */
	public static synchronized void initializeDatabaseDescriptors(final IDatabaseDescriptor... databaseDescriptors) {
		if (databaseDescriptors == null) {
			throw new IllegalArgumentException("databaseDescriptors array is null");
		}
		if (databaseDescriptors.length == 0) {
			throw new IllegalArgumentException("databaseDescriptors array is empty");
		}
		if (EntityConnectionManager.databaseDescriptors != null) {
			throw new IllegalStateException("database descriptors are already initialized");
		}
		EntityConnectionManager.databaseDescriptors = databaseDescriptors;
	}

	/**
	 * Getter method for the database descriptors.
	 * @return the database descriptors
	 */
	public static synchronized IDatabaseDescriptor[] getDatabaseDescriptors() {
		return databaseDescriptors;
	}

	/**
	 * Getter method for the default database descriptor.
	 * @return the default database descriptor
	 */
	public static synchronized IDatabaseDescriptor getDefaultDatabaseDescriptor() {
		return defaultDatabaseDescriptor;
	}
	
	/**
	 * Returns the connection for the current thread and default database, creating and
	 * caching it if it does not yet exist.
	 * @return the connection for the current thread and default database
	 */
	public static IEntityDatabaseConnection getConnection() {
		return getConnection(defaultDatabaseDescriptor);
	}

	/**
	 * Returns the connection for the current thread and specified database, creating and caching it
	 * if it does not yet exist.
	 * @param database the database descriptor
	 * @return the connection for the current thread and specified database
	 */
	public static IEntityDatabaseConnection getConnection(final IDatabaseDescriptor database) {
		Map<IDatabaseDescriptor, IEntityDatabaseConnection> connections = connectionContainer.get();
		if (connections == null) {
			connections = new HashMap<IDatabaseDescriptor, IEntityDatabaseConnection>();
			connectionContainer.set(connections);
		}
		IEntityDatabaseConnection connection = connections.get(database);
		if (connection == null) {
			connection = database.createConnection();
			connections.put(database, connection);
		}
		return connection;
	}

	/**
	 * Disposes of the connections for the current thread and for all databases.
	 */
	public static void disposeConnections() {
		logger.debug("disposing of all connections for the current thread");
		Map<IDatabaseDescriptor, IEntityDatabaseConnection> connections = connectionContainer.get();
		if (connections == null) {
			logger.trace("connection map entry is null -> no open connections");
		} else {
			for (final IDatabaseDescriptor database : databaseDescriptors) {
				logger.debug("disposing of connection for database: " + database.getDisplayName());
				final IEntityDatabaseConnection connection = connections.get(database);
				if (connection != null) {
					logger.trace("connection exists, will dispose of it now");
					try {
						connection.dispose();
					} catch (Exception e) {
						logger.error("error while disposing of connection to database: " + database.getDisplayName(), e);
					}
					logger.trace("finished disposing of this connection");
				} else {
					logger.trace("no connection found");
				}
				logger.debug("finished disposing of connection");
			}
			connectionContainer.remove();
		}
		logger.debug("finished disposing of all connections for the current thread");
	}
	
}
