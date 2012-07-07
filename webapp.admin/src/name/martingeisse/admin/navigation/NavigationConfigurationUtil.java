/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.navigation.backmapper.INavigationBackMapper;

import org.apache.wicket.Page;

/**
 * Utilities to access navigation-related configuration in the {@link ApplicationConfiguration}.
 */
public final class NavigationConfigurationUtil {

	/**
	 * The parameter key for the navigation tree.
	 */
	public static final Class<NavigationTree> NAVIGATION_TREE_PARAMETER_KEY = NavigationTree.class;

	/**
	 * The capability key for navigation back mappers.
	 */
	public static final Class<INavigationBackMapper> NAVIGATION_BACK_MAPPER_CAPABILITY_KEY = INavigationBackMapper.class;

	/**
	 * Prevent instantiation.
	 */
	private NavigationConfigurationUtil() {
	}

	/**
	 * Getter method for the navigation tree.
	 * @return the navigation tree
	 */
	public static NavigationTree getNavigationTree() {
		return ApplicationConfiguration.get().getParameters().get(NAVIGATION_TREE_PARAMETER_KEY);
	}

	/**
	 * Setter method for the navigation tree.
	 * @param navigationTree the navigation tree to set
	 */
	public static void setNavigationTree(final NavigationTree navigationTree) {
		ApplicationConfiguration.get().getParameters().set(NAVIGATION_TREE_PARAMETER_KEY, navigationTree);
	}
	
	/**
	 * Adds the specified navigation back mapper.
	 * @param backMapper the back-mapper to add
	 */
	public static void addNavigationBackMapper(final INavigationBackMapper backMapper) {
		ApplicationConfiguration.get().getCapabilities().add(NAVIGATION_BACK_MAPPER_CAPABILITY_KEY, backMapper);
	}

	/**
	 * @return an {@link Iterable} for all navigation back mappers.
	 */
	public static Iterable<INavigationBackMapper> getNavigationBackMappers() {
		return ApplicationConfiguration.get().getCapabilities().getIterable(NAVIGATION_BACK_MAPPER_CAPABILITY_KEY);
	}

	/**
	 * Given a page, returns the navigation node to which the page belongs.
	 * Returns null if no appropriate navigation node was found.
	 * @param page the page
	 * @return the navigation node or null
	 */
	public static INavigationNode mapPageToNavigationNode(final Page page) {
		// TODO: handle conflicts by returning the most specific node
		// TODO: for this, implement a set of global debugging flags to enable
		// debugging like this at runtime and just for some users; disable
		// for others to prevent excessive logging and increase performance
		// TODO: allow pages and/or navigation nodes to do back-mapping for convenience
		for (final INavigationBackMapper navigationBackMapper : getNavigationBackMappers()) {
			final INavigationNode node = navigationBackMapper.mapPageToNavigationNode(page);
			if (node != null) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Prepares the application configuration with respect to read-only rendering contributors.
	 */
	public static void prepareConfiguration() {
		for (final INavigationBackMapper navigationBackMapper : getNavigationBackMappers()) {
			navigationBackMapper.initialize(getNavigationTree());
		}
	}
	
}
