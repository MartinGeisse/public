/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import name.martingeisse.admin.application.capabilities.ApplicationCapabilities;
import name.martingeisse.admin.schema.DatabaseDescriptor;
import name.martingeisse.admin.util.ScoreComparator;

/**
 * This class can be used to configure the administration application
 * before starting it, e.g. to add plugins. No changes are allowed
 * once the application has been started.
 * 
 * All access to this class is through static methods.
 */
public class ApplicationConfiguration {

	/**
	 * the sealed
	 */
	private static boolean sealed = false;
	
	/**
	 * the databases
	 */
	private static List<DatabaseDescriptor> databases = new ArrayList<DatabaseDescriptor>();
	
	/**
	 * the plugins
	 */
	private static List<IPlugin> plugins = new ArrayList<IPlugin>();
	
	/**
	 * the capabilities
	 */
	private static ApplicationCapabilities capabilities;
	
	/**
	 * Prevent instantiation.
	 */
	private ApplicationConfiguration() {
	}

	/**
	 * 
	 */
	private static void checkChangesAllowed() {
		if (sealed) {
			throw new IllegalStateException("Application configuration has been sealed");
		}
	}
	
	/**
	 * Adds a database.
	 * @param database the database to add
	 */
	public static void addDatabase(DatabaseDescriptor database) {
		checkChangesAllowed();
		databases.add(database);
	}
	
	/**
	 * Adds a plugin.
	 * @param plugin the plugin to add
	 */
	public static void addPlugin(IPlugin plugin) {
		checkChangesAllowed();
		plugins.add(plugin);
	}

	/**
	 * Seals this configuration to disallow further changes.
	 */
	static void seal() {
		sealed = true;
	}

	/**
	 * Collects capabilities from all plugins and creates the
	 * capabilities object in this configuration.
	 */
	static void initializeCapabilities() {
		
		// gather all capabilities
		capabilities = new ApplicationCapabilities();
		for (IPlugin plugin : plugins) {
			plugin.contribute(capabilities);
		}
		
		// sort property read-only renderer contributors by score
		Collections.sort(capabilities.getPropertyReadOnlyRendererContributors(), ScoreComparator.DESCENDING_INSTANCE);
		
	}
	
	/**
	 * Returns an iterable for all databases
	 * @return the database iterable
	 */
	public static Iterable<DatabaseDescriptor> getDatabases() {
		return databases;
	}
	
	/**
	 * Returns an iterable for all plugins
	 * @return the plugin iterable
	 */
	public static Iterable<IPlugin> getPlugins() {
		return plugins;
	}
	
	/**
	 * Getter method for the capabilities.
	 * @return the capabilities
	 */
	public static ApplicationCapabilities getCapabilities() {
		return capabilities;
	}
	
}
