/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.navigation.NavigationTree;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Utilities to access pages configuration in the {@link ApplicationConfiguration}.
 */
public final class PagesConfigurationUtil {

	/**
	 * The parameter key for the page border factory.
	 */
	public static final Class<IPageBorderFactory> PAGE_BORDER_FACTORY_PARAMETER_KEY = IPageBorderFactory.class;
	
	/**
	 * The parameter key for the navigation tree.
	 */
	public static final Class<NavigationTree> NAVIGATION_TREE_PARAMETER_KEY = NavigationTree.class;

	/**
	 * Prevent instantiation.
	 */
	private PagesConfigurationUtil() {
	}

	/**
	 * Getter method for the page border factory.
	 * @return the page border factory
	 */
	public static IPageBorderFactory getPageBorderFactory() {
		return ApplicationConfiguration.get().getParameters().get(PAGE_BORDER_FACTORY_PARAMETER_KEY);
	}

	/**
	 * Setter method for the page border factory.
	 * @param pageBorderFactory the page border factory to set
	 */
	public static void setPageBorderFactory(final IPageBorderFactory pageBorderFactory) {
		ApplicationConfiguration.get().getParameters().set(PAGE_BORDER_FACTORY_PARAMETER_KEY, pageBorderFactory);
	}
	
	/**
	 * Creates either a page border (if a page border factory is set) or a 
	 * simple {@link WebMarkupContainer}.
	 * @param id the wicket id of the container to create
	 * @return the page border or other container
	 */
	public static WebMarkupContainer createPageBorder(final String id) {
		IPageBorderFactory pageBorderFactory = getPageBorderFactory();
		return (pageBorderFactory == null ? new WebMarkupContainer(id) : pageBorderFactory.createPageBorder(id));
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
	
}
