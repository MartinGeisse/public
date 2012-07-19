/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages.border;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.NavigationUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Utilities to access pages configuration in the {@link ApplicationConfiguration}.
 */
public final class PageBorderUtil {

	/**
	 * The wicket:id of page borders.
	 */
	public static final String PAGE_BORDER_ID = "pageBorder";
	
	/**
	 * Prevent instantiation.
	 */
	private PageBorderUtil() {
	}

	/**
	 * Creates all page borders and returns them as a combined border.
	 * @param page the page to create the borders for
	 * @return the combined page borders
	 */
	public static WebMarkupContainer createAllPageBorders(Page page) {

		// determine the navigation location so navigation nodes can contribute page borders
		String currentNavigationPath = StringUtils.defaultString(NavigationUtil.getNavigationPathForPage(page));
		NavigationNode currentNavigationNode = NavigationConfigurationUtil.getNavigationTree().getRoot().findMostSpecificNode(currentNavigationPath);

		// create a list of all page borders
		List<WebMarkupContainer> pageBorders = new ArrayList<WebMarkupContainer>();
		currentNavigationNode.createPageBorders(pageBorders);
		
		// combine them into a single border
		return DoublePageBorder.combineBorders(pageBorders.toArray(new WebMarkupContainer[pageBorders.size()]));
		
	}
	
}
