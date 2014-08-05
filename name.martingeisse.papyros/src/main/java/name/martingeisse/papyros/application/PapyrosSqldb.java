/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.papyros.application;

import name.martingeisse.sql.MysqlDatabaseDescriptor;


/**
 * Simplified interface for Papyros's SQL database.
 */
public final class PapyrosSqldb {

	/**
	 * the database
	 */
	public static MysqlDatabaseDescriptor database;
	
	/**
	 * Prevent instantiation.
	 */
	private PapyrosSqldb() {
	}
	
}
