/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import name.martingeisse.admin.application.capabilities.ApplicationCapabilities;
import name.martingeisse.admin.application.capabilities.IEntityNameMappingStrategy;
import name.martingeisse.admin.application.capabilities.INavigationBackMapper;
import name.martingeisse.admin.application.capabilities.IPageBorderFactory;
import name.martingeisse.admin.multi.IGlobalEntityListPresenter;
import name.martingeisse.admin.navigation.NavigationTree;
import name.martingeisse.admin.schema.AbstractDatabaseDescriptor;
import name.martingeisse.admin.schema.EntityDescriptor;
import name.martingeisse.admin.schema.EntityPropertyDescriptor;
import name.martingeisse.admin.util.ScoreComparator;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * This class can be used to configure the administration application
 * before starting it, e.g. to add plugins. No changes are allowed
 * once the application has been started.
 * 
 * All access to this class is through static methods.
 */
public final class ApplicationConfiguration {

	/**
	 * the sealed
	 */
	private static boolean sealed = false;

	/**
	 * the databases
	 */
	private static List<AbstractDatabaseDescriptor> databases = new ArrayList<AbstractDatabaseDescriptor>();

	/**
	 * the plugins
	 */
	private static List<IPlugin> plugins = new ArrayList<IPlugin>();

	/**
	 * the defaultEntityListPresenter
	 */
	private static IGlobalEntityListPresenter defaultEntityListPresenter;

	/**
	 * the rawEntityListFieldOrder
	 */
	private static Comparator<EntityPropertyDescriptor> rawEntityListFieldOrder;

	/**
	 * the entityNameMappingStrategy
	 */
	private static IEntityNameMappingStrategy entityNameMappingStrategy;

	/**
	 * the pageBorderFactory
	 */
	private static IPageBorderFactory pageBorderFactory;

	/**
	 * the navigationTree
	 */
	private static NavigationTree navigationTree = new NavigationTree();

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
	public static void addDatabase(final AbstractDatabaseDescriptor database) {
		checkChangesAllowed();
		databases.add(database);
	}

	/**
	 * Adds a plugin.
	 * @param plugin the plugin to add
	 */
	public static void addPlugin(final IPlugin plugin) {
		checkChangesAllowed();
		plugins.add(plugin);
	}

	/**
	 * Getter method for the defaultEntityListPresenter.
	 * @return the defaultEntityListPresenter
	 */
	public static IGlobalEntityListPresenter getDefaultEntityListPresenter() {
		return defaultEntityListPresenter;
	}

	/**
	 * Setter method for the defaultEntityListPresenter.
	 * @param defaultEntityListPresenter the defaultEntityListPresenter to set
	 */
	public static void setDefaultEntityListPresenter(final IGlobalEntityListPresenter defaultEntityListPresenter) {
		checkChangesAllowed();
		ApplicationConfiguration.defaultEntityListPresenter = defaultEntityListPresenter;
	}

	/**
	 * Getter method for the rawEntityListFieldOrder.
	 * @return the rawEntityListFieldOrder
	 */
	public static Comparator<EntityPropertyDescriptor> getRawEntityListFieldOrder() {
		return rawEntityListFieldOrder;
	}

	/**
	 * Setter method for the rawEntityListFieldOrder.
	 * @param rawEntityListFieldOrder the rawEntityListFieldOrder to set
	 */
	public static void setRawEntityListFieldOrder(final Comparator<EntityPropertyDescriptor> rawEntityListFieldOrder) {
		checkChangesAllowed();
		ApplicationConfiguration.rawEntityListFieldOrder = rawEntityListFieldOrder;
	}

	/**
	 * Getter method for the entityNameMappingStrategy.
	 * @return the entityNameMappingStrategy
	 */
	public static IEntityNameMappingStrategy getEntityNameMappingStrategy() {
		return entityNameMappingStrategy;
	}

	/**
	 * Setter method for the entityNameMappingStrategy.
	 * @param entityNameMappingStrategy the entityNameMappingStrategy to set
	 */
	public static void setEntityNameMappingStrategy(final IEntityNameMappingStrategy entityNameMappingStrategy) {
		checkChangesAllowed();
		ApplicationConfiguration.entityNameMappingStrategy = entityNameMappingStrategy;
	}

	/**
	 * Returns the entity name for the specified entity.
	 * @param entity the entity
	 * @return the name to display
	 */
	public static String getEntityName(final EntityDescriptor entity) {
		return (entityNameMappingStrategy == null ? entity.getTableName() : entityNameMappingStrategy.getEntityName(entity));
	}

	/**
	 * Getter method for the pageBorderFactory.
	 * @return the pageBorderFactory
	 */
	public static IPageBorderFactory getPageBorderFactory() {
		return pageBorderFactory;
	}

	/**
	 * Setter method for the pageBorderFactory.
	 * @param pageBorderFactory the pageBorderFactory to set
	 */
	public static void setPageBorderFactory(final IPageBorderFactory pageBorderFactory) {
		checkChangesAllowed();
		ApplicationConfiguration.pageBorderFactory = pageBorderFactory;
	}

	/**
	 * Creates either a page border (if a page border factory is set) or a 
	 * simple {@link WebMarkupContainer}.
	 * @param id the wicket id of the container to create
	 * @return the page border or other container
	 */
	public static WebMarkupContainer createPageBorder(final String id) {
		return (pageBorderFactory == null ? new WebMarkupContainer(id) : pageBorderFactory.createPageBorder(id));
	}

	/**
	 * Getter method for the navigationTree.
	 * @return the navigationTree
	 */
	public static NavigationTree getNavigationTree() {
		return navigationTree;
	}

	/**
	 * Setter method for the navigationTree.
	 * @param navigationTree the navigationTree to set
	 */
	public static void setNavigationTree(final NavigationTree navigationTree) {
		checkChangesAllowed();
		ApplicationConfiguration.navigationTree = navigationTree;
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
		for (final IPlugin plugin : plugins) {
			plugin.contribute(capabilities);
		}

		// sort property read-only renderer contributors by score
		Collections.sort(capabilities.getPropertyReadOnlyRendererContributors(), ScoreComparator.DESCENDING_INSTANCE);

		// initialize navigation back-mappers (page-to-navigation node)
		for (final INavigationBackMapper navigationBackMapper : capabilities.getNavigationBackMappers()) {
			navigationBackMapper.initialize(ApplicationConfiguration.getNavigationTree());
		}

	}

	/**
	 * Returns an iterable for all databases
	 * @return the database iterable
	 */
	public static Iterable<AbstractDatabaseDescriptor> getDatabases() {
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
