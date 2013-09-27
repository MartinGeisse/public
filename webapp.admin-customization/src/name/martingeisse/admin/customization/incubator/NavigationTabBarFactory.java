/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.incubator;

import name.martingeisse.admin.component.pagebar.IPageBarFactory;
import name.martingeisse.admon.navigation.NavigationChildrenModel;
import name.martingeisse.admon.navigation.NavigationNode;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * This factory creates {@link NavigationTabBar}s.
 */
public class NavigationTabBarFactory implements IPageBarFactory {

	/**
	 * the node
	 */
	private final NavigationNode node;

	/**
	 * Constructor.
	 * @param node the path of the navigation node
	 */
	public NavigationTabBarFactory(final NavigationNode node) {
		this.node = node;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.component.pagebar.IPageBarFactory#createPageTopBar(java.lang.String)
	 */
	@Override
	public Panel createPageTopBar(final String id) {
		return new NavigationTabBar(id, new NavigationChildrenModel(node.getPath()));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.component.pagebar.IPageBarFactory#createPageBottomBar(java.lang.String)
	 */
	@Override
	public Panel createPageBottomBar(final String id) {
		return null;
	}

	/**
	 * Sets a new factory of this type for the specified navigation node.
	 * @param node the node
	 */
	public static void apply(final NavigationNode node) {
		node.setPageBarFactory(new NavigationTabBarFactory(node));
	}

}
