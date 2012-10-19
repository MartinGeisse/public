/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo;

import name.martingeisse.api.handler.misc.NoFaviconDecorator;
import name.martingeisse.api.servlet.Launcher;
import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.MysqlDatabaseDescriptor;

import org.joda.time.DateTimeZone;

/**
 * Main class.
 */
public class Main {

	/**
	 * The main method
	 * @param args ...
	 * @throws Exception on errors
	 */
	public static void main(final String[] args) throws Exception {

		final MysqlDatabaseDescriptor phorumDatabase = new MysqlDatabaseDescriptor();
		phorumDatabase.setDisplayName("Phorum database");
		phorumDatabase.setUrl("jdbc:mysql://localhost/phorum?zeroDateTimeBehavior=convertToNull&useTimezone=false");
		phorumDatabase.setUsername("root");
		phorumDatabase.setPassword("");
		phorumDatabase.setDefaultTimeZone(DateTimeZone.forID("Europe/Berlin"));
		EntityConnectionManager.initializeDatabaseDescriptors(phorumDatabase);

		Launcher.launch(new NoFaviconDecorator(new ApplicationHandler()), null);
	}

}
