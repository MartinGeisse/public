/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo;

import java.io.File;
import java.util.Locale;

import name.martingeisse.api.handler.DefaultMasterHandler;
import name.martingeisse.api.handler.misc.NotFoundHandler;
import name.martingeisse.api.servlet.ApiConfiguration;
import name.martingeisse.api.servlet.Launcher;
import name.martingeisse.api.tools.CollectLocalizationPropertiesAction;
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

		/*
		new CollectLocalizationPropertiesAction().run(new File("src"));
		System.exit(0);
		*/
		
		final MysqlDatabaseDescriptor phorumDatabase = new MysqlDatabaseDescriptor();
		phorumDatabase.setDisplayName("Phorum database");
		phorumDatabase.setUrl("jdbc:mysql://localhost/phorum?zeroDateTimeBehavior=convertToNull&useTimezone=false");
		phorumDatabase.setUsername("root");
		phorumDatabase.setPassword("");
		phorumDatabase.setDefaultTimeZone(DateTimeZone.forID("Europe/Berlin"));
		EntityConnectionManager.initializeDatabaseDescriptors(phorumDatabase);

		DefaultMasterHandler masterHandler = new DefaultMasterHandler();
		masterHandler.setApplicationRequestHandler(new ApplicationHandler());
		masterHandler.getInterceptHandlers().put("/favicon.ico", new NotFoundHandler(false));
		
		ApiConfiguration configuration = new ApiConfiguration();
		configuration.setMasterRequestHandler(masterHandler);
		configuration.getLocalizationConfiguration().setGlobalFallback(Locale.US);
		Launcher.launch(configuration);
		
	}

}
