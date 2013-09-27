/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.database.IDatabaseDescriptor;
import name.martingeisse.common.util.ParameterUtil;

import org.apache.log4j.Logger;

/**
 * This class acts as a singleton to group all objects related to
 * the application configuration.
 * 
 * The instance can be modified until it gets sealed. This happens
 * when the {@link Launcher} launches the application. After that,
 * no modification is possible. Application modules can later
 * read information from this class and behave accordingly.
 * 
 * The application sets databases, plugins and parameters in this
 * class. Capabilities are not set by the application but contributed
 * by plugins.
 */
public final class ApplicationConfiguration {

	/**
	 * the databases
	 */
	private final List<IDatabaseDescriptor> databases = new ArrayList<IDatabaseDescriptor>();

	/**
	 * Adds a database.
	 * @param database the database to add
	 */
	public void addDatabase(final IDatabaseDescriptor database) {
		ParameterUtil.ensureNotNull(database, "database");
		checkChangesAllowed();
		logger.info("Adding database to the ApplicationConfiguration: " + database.getDisplayName());
		databases.add(database);
	}

	/**
	 * Returns an iterable for all databases
	 * @return the database iterable
	 */
	public Iterable<IDatabaseDescriptor> getDatabases() {
		return databases;
	}

}
