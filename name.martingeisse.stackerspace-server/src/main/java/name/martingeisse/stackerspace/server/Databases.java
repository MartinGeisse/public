/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackerspace.server;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Static access to all database descriptors.
 */
public class Databases {

	/**
	 * Prevent instantiation.
	 */
	private Databases() {
	}
	
	/**
	 * The Cassandra-based database cluster (not usually used).
	 */
	public static Cluster cassandraCluster;
	
	/**
	 * The Cassandra database session for the world DB.
	 */
	public static Session world;
	
}
