/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor;
import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationTree;
import name.martingeisse.admin.util.ParameterUtil;
import name.martingeisse.admin.util.SealableClassKeyedContainer;
import name.martingeisse.admin.util.SealableClassKeyedListContainer;
import name.martingeisse.common.util.ClassKeyedContainer;
import name.martingeisse.common.util.ClassKeyedListContainer;

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
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(ApplicationConfiguration.class);
	
	/**
	 * the instance
	 */
	private static ApplicationConfiguration instance = new ApplicationConfiguration();
	
	/**
	 * @return the singleton instance of this class
	 */
	public static ApplicationConfiguration get() {
		return instance;
	}
	
	/**
	 * the sealed
	 */
	private boolean sealed = false;

	/**
	 * the databases
	 */
	private final List<AbstractDatabaseDescriptor> databases = new ArrayList<AbstractDatabaseDescriptor>();

	/**
	 * the plugins
	 */
	private final List<IPlugin> plugins = new ArrayList<IPlugin>();

	/**
	 * the parameters
	 */
	private final SealableClassKeyedContainer<Object> parameters = new SealableClassKeyedContainer<Object>();
	
	/**
	 * the capabilities
	 */
	private final SealableClassKeyedListContainer<Object> capabilities = new SealableClassKeyedListContainer<Object>();
	
	/**
	 * Constructor.
	 */
	private ApplicationConfiguration() {
		parameters.set(NavigationConfigurationUtil.NAVIGATION_TREE_PARAMETER_KEY, new NavigationTree());
	}
	
	/**
	 * Checks whether this class is sealed, and if so, throws an {@link IllegalStateException}.
	 */
	private void checkChangesAllowed() {
		if (sealed) {
			logger.error("Trying to modify ApplicationConfiguration after it has been sealed.");
			throw new IllegalStateException("Application configuration has been sealed");
		}
	}

	/**
	 * Adds a database.
	 * @param database the database to add
	 */
	public void addDatabase(final AbstractDatabaseDescriptor database) {
		ParameterUtil.ensureNotNull(database, "database");
		checkChangesAllowed();
		logger.info("Adding database to the ApplicationConfiguration: " + database.getDisplayName());
		databases.add(database);
	}

	/**
	 * Returns an iterable for all databases
	 * @return the database iterable
	 */
	public Iterable<AbstractDatabaseDescriptor> getDatabases() {
		return databases;
	}

	/**
	 * Adds a plugin.
	 * @param plugin the plugin to add
	 */
	public void addPlugin(final IPlugin plugin) {
		ParameterUtil.ensureNotNull(plugin, "plugin");
		checkChangesAllowed();
		logger.info("Adding plugin to the ApplicationConfiguration: " + plugin);
		plugins.add(plugin);
	}

	/**
	 * Returns an iterable for all plugins
	 * @return the plugin iterable
	 */
	public Iterable<IPlugin> getPlugins() {
		return plugins;
	}

	/**
	 * Getter method for the parameters.
	 * @return the parameters
	 */
	public ClassKeyedContainer<Object> getParameters() {
		return parameters;
	}
	
	/**
	 * Getter method for the capabilities.
	 * @return the capabilities
	 */
	public ClassKeyedListContainer<Object> getCapabilities() {
		return capabilities;
	}

	/**
	 * This method is called by the framework upon initialization.
	 * 
	 * Collects capabilities from all plugins and fills the
	 * capabilities container in this configuration. This method
	 * seals the configuration.
	 */
	public void initialize() {
		logger.debug("ApplicationConfiguration.initialize(): begin");

		// this seals the configuration
		checkChangesAllowed();
		sealed = true;
		parameters.seal();
		
		// gather all capabilities
		for (final IPlugin plugin : plugins) {
			logger.trace("adding contributions from plugin: " + plugin);
			plugin.contribute();
		}
		capabilities.seal();

		logger.debug("ApplicationConfiguration.initialize(): end");
	}
	
}
