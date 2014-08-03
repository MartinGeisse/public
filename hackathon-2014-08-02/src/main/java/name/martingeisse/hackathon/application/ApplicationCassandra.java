/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.hackathon.application;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * Simplified interface for the Application's Cassandra database.
 * Offers access to the Cassandra driver as well as higher-level
 * functions.
 */
public final class ApplicationCassandra {

	/**
	 * the Cassandra DB cluster (not usually needed)
	 */
	public static Cluster cluster;
	
	/**
	 * the Cassandra DB session
	 */
	public static Session session;

	/**
	 * Fetches a single row by string-typed ID.
	 * 
	 * @param tableName the name of the table to fetch from
	 * @param id the row's ID
	 * @return the row, or null if not found
	 */
	public static Row getById(String tableName, String id) {
		return session.execute(QueryBuilder.select().all().from(tableName).where(QueryBuilder.eq("id", id)).limit(1)).one();
	}

	/**
	 * Fetches a single row by long-typed ID.
	 * 
	 * @param tableName the name of the table to fetch from
	 * @param id the row's ID
	 * @return the row, or null if not found
	 */
	public static Row getById(String tableName, long id) {
		return session.execute(QueryBuilder.select().all().from(tableName).where(QueryBuilder.eq("id", id)).limit(1)).one();
	}
	
	/**
	 * Prevent instantiation.
	 */
	private ApplicationCassandra() {
	}
	
}
