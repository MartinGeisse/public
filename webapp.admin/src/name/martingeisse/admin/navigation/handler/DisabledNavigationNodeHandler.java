/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.navigation.INavigationNodeHandler;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.util.LinkUtil;

import org.apache.wicket.markup.html.link.AbstractLink;

/**
 * This handler mounts no pages and just returns a disabled link. It has a
 * fixed id (specified in the constructor) and uses its ID as its title.
 * This node type is typically used to specify the structure of local
 * navigation trees for entity instances, where the actual node handlers
 * come from a global tree template, and the local tree adds nodes to
 * that template.
 */
public final class DisabledNavigationNodeHandler implements INavigationNodeHandler {

	/**
	 * the id
	 */
	private final String id;

	/**
	 * Constructor.
	 * @param id the node id
	 */
	public DisabledNavigationNodeHandler(final String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#getId(name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public String getId(final NavigationNode node) {
		return id;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#getTitle(name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public String getTitle(final NavigationNode node) {
		return id;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#mountRequestMappers(name.martingeisse.admin.application.wicket.AdminWicketApplication, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public void mountRequestMappers(final AdminWicketApplication application, final NavigationNode node) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createClone()
	 */
	@Override
	public INavigationNodeHandler createClone() {
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createLink(java.lang.String, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public AbstractLink createLink(final String id, final NavigationNode node) {
		return LinkUtil.createDisabledLink(id);
	}

}
