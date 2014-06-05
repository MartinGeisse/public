/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ucademy.application;

import name.martingeisse.sql.MysqlDatabaseDescriptor;


/**
 * Simplified interface for Ucademy's SQL database.
 */
public final class GarglSqldb {

	/**
	 * the database
	 */
	public static MysqlDatabaseDescriptor database;
	
	/**
	 * Prevent instantiation.
	 */
	private GarglSqldb() {
	}
	
}
