/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.component.pagebar;

import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.NavigationUtil;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * Utilities to deal with page bars.
 */
public final class PageBarUtil {

	/**
	 * Prevent instantiation.
	 */
	private PageBarUtil() {
	}

	/**
	 * Creates all page top-bars and collects them in a {@link RepeatingView} with the specified wicket:id.
	 * @param page the page to create the top bars for
	 * @param id the wicket id of the {@link RepeatingView}
	 * @return the {@link RepeatingView} that contains all top-bars
	 * 
	 * TODO: if NavigationUtil.getNavigationNodeForPage(page) returns null then
	 * add an empty component instead of the RepeatingView.
	 */
	public static WebMarkupContainer createAllPageTopBars(final Page page, final String id) {
		final RepeatingView view = new RepeatingView(id);
		createAllPageTopBars(NavigationUtil.getNavigationNodeForPage(page), view);
		return view;
	}

	/**
	 * Creates all page bottom-bars and collects them in a {@link RepeatingView} with the specified wicket:id.
	 * @param page the page to create the bottom bars for
	 * @param id the wicket id of the {@link RepeatingView}
	 * @return the {@link RepeatingView} that contains all bottom-bars
	 * 
	 * TODO: if NavigationUtil.getNavigationNodeForPage(page) returns null then
	 * add an empty component instead of the RepeatingView.
	 */
	public static WebMarkupContainer createAllPageBottomBars(final Page page, final String id) {
		final RepeatingView view = new RepeatingView(id);
		createAllPageBottomBars(NavigationUtil.getNavigationNodeForPage(page), view);
		return view;
	}

	/**
	 * 
	 */
	private static void createAllPageTopBars(final NavigationNode node, final RepeatingView view) {
		if (node.getParent() != null) {
			createAllPageTopBars(node.getParent(), view);
		}
		if (node.getPageBarFactory() != null) {
			final Panel topBar = node.getPageBarFactory().createPageTopBar(view.newChildId());
			if (topBar != null) {
				view.add(topBar);
			}
		}
		{
			final Panel topBar = node.getHandler().createPageTopBar(view.newChildId());
			if (topBar != null) {
				view.add(topBar);
			}
		}
	}

	/**
	 * 
	 */
	private static void createAllPageBottomBars(final NavigationNode node, final RepeatingView view) {
		{
			final Panel bottomBar = node.getHandler().createPageBottomBar(view.newChildId());
			if (bottomBar != null) {
				view.add(bottomBar);
			}
		}
		if (node.getPageBarFactory() != null) {
			final Panel bottomBar = node.getPageBarFactory().createPageBottomBar(view.newChildId());
			if (bottomBar != null) {
				view.add(bottomBar);
			}
		}
		if (node.getParent() != null) {
			createAllPageBottomBars(node.getParent(), view);
		}
	}

}
