/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ucademy;

import name.martingeisse.ucademy.application.Configuration;
import name.martingeisse.ucademy.application.Initializer;
import org.apache.log4j.Logger;

/**
 * The main class.
 */
public class Main {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(Main.class);
	
	/**
	 * The main method.
	 * @param args command-line arguments
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		
		// parse command line arguments
		String configurationFilePath;
		if (args.length > 1) {
			System.out.println("invalid arguments. expected: ...Main [configfile.properties]");
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
		logger.trace("configuration file read.");
		
		// initialize the base system
		Initializer.initializeBase();
		
		// initialize the sub-systems in parallel for faster startup
		new Thread() {
			@Override
			public void run() {
				Initializer.initializeSqlDatabase();
			}
		}.start();
		// initialize the sub-systems in parallel for faster startup
		new Thread() {
			@Override
			public void run() {
				Initializer.initializeCassandraDatabase();
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				Initializer.initializeHazelcast();
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				Initializer.initializeWeb();
			}
		}.start();
		
	}
}
