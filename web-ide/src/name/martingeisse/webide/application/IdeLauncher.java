/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.application;

import java.io.IOException;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.MysqlDatabaseDescriptor;
import name.martingeisse.common.javascript.JavascriptAssembler;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

/**
 * Common functionality to run the IDE and related programs.
 */
public class IdeLauncher {

	/**
	 * Initializes URL handlers, time zones and the database.
	 * @throws IOException on I/O errors
	 */
	public static void initialize() throws IOException {
	
		// read the configuration
		Configuration.initialize();
		
		// initialize URL handlers
        System.setProperty("java.protocol.handler.pkgs", "name.martingeisse.webide.util.url");
		
		// initialize time zone
		DateTimeZone timeZone = DateTimeZone.forID("Europe/Berlin");
		JavascriptAssembler.defaultDateFormatter = DateTimeFormat.forPattern("YYYY-MM-dd").withZone(timeZone);
		JavascriptAssembler.defaultDateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss").withZone(timeZone);
		
		// initialize database
		MysqlDatabaseDescriptor mainDatabase = new MysqlDatabaseDescriptor();
		mainDatabase.setDisplayName("Local database");
		mainDatabase.setUrl(Configuration.getMainDatabaseUrl());
		mainDatabase.setUsername(Configuration.getMainDatabaseUsername());
		mainDatabase.setPassword(Configuration.getMainDatabasePassword());
		mainDatabase.setDefaultTimeZone(timeZone);
		mainDatabase.initialize();
		EntityConnectionManager.initializeDatabaseDescriptors(mainDatabase);
		
	}
	
}
