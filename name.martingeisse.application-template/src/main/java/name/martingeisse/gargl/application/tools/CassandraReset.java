/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.gargl.application.tools;

import name.martingeisse.gargl.application.Configuration;
import name.martingeisse.gargl.application.GarglCassandra;
import name.martingeisse.gargl.application.Initializer;
import name.martingeisse.sql.EntityConnectionManager;
import org.apache.log4j.Logger;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.sql.SQLQuery;

/**
 * Resets the Cassandra database to a known state.
 */
@SuppressWarnings("unused")
public final class CassandraReset {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(CassandraReset.class);
	
	/**
	 * the cluster
	 */
	protected final Cluster cluster;
	
	/**
	 * the session
	 */
	protected final Session session;

	/**
	 * Constructor.
	 */
	public CassandraReset() {
		cluster = GarglCassandra.cluster;
		session = GarglCassandra.session;
	}

	/**
	 * Runs the database reset.
	 */
	public void run() {
		
		// initialize the keyspace
		try {
			session.execute("DROP KEYSPACE gargl;");
		} catch (InvalidQueryException e) {
			// may happen if the keyspace didn't exist at all
		}
		session.execute("CREATE KEYSPACE gargl WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};");
		session.execute("USE gargl;");
		
	}

	/**
	 * Creates a table.
	 */
	private final void createTable(String name, String idType, String... fields) {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE ").append(name).append(" (id ");
		builder.append(idType).append(" PRIMARY KEY");
		for (String field : fields) {
			builder.append(", ").append(field);
		}
		builder.append(");");
		String command = builder.toString();
		logger.info(command);
		session.execute(command);
	}
	
	/**
	 * The main method.
	 * @param args command-line arguments
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		
		// parse command line arguments
		String configurationFilePath;
		if (args.length > 1) {
			System.out.println("invalid arguments. expected: ...CassandraResetMain [configfile.properties]");
			return;
		} else if (args.length == 1) {
			configurationFilePath = args[0];
		} else {
			configurationFilePath = "test.properties";
		}
		logger.info("----- starting frontend with config file: " + configurationFilePath + " -----");
		
		// read the configuration file
		logger.trace("reading config file...");
		Configuration.initialize(configurationFilePath);
		logger.trace("Konfigurationsdatei ausgelesen.");
		
		// initialize the base system and subsystems necessary for the database reset
		Initializer.initializeBase();
		Initializer.initializeSqlDatabase();
		Initializer.initializeCassandraDatabase();
		
		// run the database reset, then clean up
		try {
			new CassandraReset().run();
		} finally {
			GarglCassandra.session.close();
			GarglCassandra.cluster.close();
		}
		
	}
	
}
