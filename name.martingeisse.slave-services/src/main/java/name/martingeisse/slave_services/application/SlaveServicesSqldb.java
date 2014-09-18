/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.application;

import name.martingeisse.sql.MysqlDatabaseDescriptor;


/**
 * Simplified interface for SlaveServices's SQL database.
 */
public final class SlaveServicesSqldb {

	/**
	 * the database
	 */
	public static MysqlDatabaseDescriptor database;
	
	/**
	 * Prevent instantiation.
	 */
	private SlaveServicesSqldb() {
	}
	
}
