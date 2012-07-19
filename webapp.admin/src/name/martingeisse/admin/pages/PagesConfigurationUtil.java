/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.NavigationPageParameterUtil;
import name.martingeisse.admin.util.wicket.Constants;
import name.martingeisse.admin.util.wicket.DoublePageBorder;
import name.martingeisse.admin.util.wicket.IPageBorderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
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
	 * Creates the main page border, i.e. the outmost one. Additional page borders can be contributed by
	 * navigation nodes.
	 * @return the page border
	 */
	public static WebMarkupContainer createMainPageBorder() {
		IPageBorderFactory pageBorderFactory = getPageBorderFactory();
		return (pageBorderFactory == null ? new WebMarkupContainer(Constants.PAGE_BORDER_ID) : pageBorderFactory.createPageBorder());
	}
	
	/**
	 * Creates all page borders and returns them as a combined border.
	 * @param page the page to create the borders for
	 * @return the combined page borders
	 */
	public static WebMarkupContainer createAllPageBorders(Page page) {

		// determine the navigation location so navigation nodes can contribute page borders
		String currentNavigationPath = StringUtils.defaultString(NavigationPageParameterUtil.getNavigationPathForPage(page));
		NavigationNode currentNavigationNode = NavigationConfigurationUtil.getNavigationTree().getRoot().findMostSpecificNode(currentNavigationPath);

		// create a list of all page borders
		List<WebMarkupContainer> pageBorders = new ArrayList<WebMarkupContainer>();
		pageBorders.add(createMainPageBorder());
		currentNavigationNode.createPageBorders(pageBorders);
		
		// combine them into a single border
		return DoublePageBorder.combineBorders(pageBorders.toArray(new WebMarkupContainer[pageBorders.size()]));
		
	}
	
}
