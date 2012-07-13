/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.navigation.INavigationNodeHandler;
import name.martingeisse.admin.navigation.NavigationMountedRequestMapper;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.pages.GlobalNavigationFolderPage;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;

/**
 * This is an implementation of {@link INavigationNodeHandler}
 * that just displays links to its children. It is typically used
 * for nodes that have no other content.
 * 
 * This class is intended for usein the global navigation tree.
 * See {@link EntityInstanceNavigationFolderHandler} for the
 * entity-instance counterpart.
 */
public class GlobalNavigationFolderHandler extends AbstractNavigationNodeHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.AbstractNavigationNodeHandler#mountRequestMappers(name.martingeisse.admin.application.wicket.AdminWicketApplication, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public void mountRequestMappers(AdminWicketApplication application, NavigationNode node) {
		application.mount(new NavigationMountedRequestMapper(node.getPath(), null, GlobalNavigationFolderPage.class));
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createLink(java.lang.String, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public AbstractLink createLink(String id, NavigationNode node) {
		return new ExternalLink(id, node.getPath());
	}

}
