/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handlers;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.navigation.INavigationNodeHandler;
import name.martingeisse.admin.navigation.NavigationNode;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Base implementation for {@link INavigationNodeHandler}.
 */
public abstract class AbstractNavigationNodeHandler implements INavigationNodeHandler {

	/**
	 * Constructor.
	 */
	public AbstractNavigationNodeHandler() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#getFallbackId(name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public String getFallbackId(final NavigationNode node) {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#getFallbackTitle(name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public String getFallbackTitle(final NavigationNode node) {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#mountRequestMappers(name.martingeisse.admin.application.wicket.AdminWicketApplication, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public void mountRequestMappers(final AdminWicketApplication application, final NavigationNode node) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.component.pagebar.IPageBarFactory#createPageTopBar(java.lang.String)
	 */
	@Override
	public Panel createPageTopBar(final String id) {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.component.pagebar.IPageBarFactory#createPageBottomBar(java.lang.String)
	 */
	@Override
	public Panel createPageBottomBar(final String id) {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#getEntityNameForCanonicalEntityListNode()
	 */
	@Override
	public String getEntityNameForCanonicalEntityListNode() {
		return null;
	}

}
