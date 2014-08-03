/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.hackathon.application;

import name.martingeisse.sql.MysqlDatabaseDescriptor;


/**
 * Simplified interface for the application's SQL database.
 */
public final class ApplicationSqldb {

	/**
	 * the database
	 */
	public static MysqlDatabaseDescriptor database;
	
	/**
	 * Prevent instantiation.
	 */
	private ApplicationSqldb() {
	}
	
}
