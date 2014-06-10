/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.forum.application;

import name.martingeisse.sql.MysqlDatabaseDescriptor;


/**
 * Simplified interface for Forum's SQL database.
 */
public final class ForumSqldb {

	/**
	 * the database
	 */
	public static MysqlDatabaseDescriptor database;
	
	/**
	 * Prevent instantiation.
	 */
	private ForumSqldb() {
	}
	
}
