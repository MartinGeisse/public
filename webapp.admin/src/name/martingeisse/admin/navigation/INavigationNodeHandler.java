/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.pages.border.IPageBorderFactory;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;

/**
 * This interface is implemented to give navigation nodes their behavior.
 */
public interface INavigationNodeHandler extends IPageBorderFactory {

	/**
	 * @param node the handled node
	 * @return the id of the handled node
	 */
	public String getId(NavigationNode node);
	
	/**
	 * @param node the handled node
	 * @return the title of the handled node
	 */
	public String getTitle(NavigationNode node);
	
	/**
	 * Creates a Wicket {@link Link} for this node. The link will
	 * refer to the correct URL for this node, but has no child
	 * components or associated markup. That is, the appearance of
	 * the link is up to the caller.
	 * 
	 * @param id the wicket id
	 * @param node the handled node
	 * @return the link
	 */
	public AbstractLink createLink(String id, NavigationNode node);

	/**
	 * Mounts the request mappers for this node and its descendants in the Wicket
	 * Application. This method is called by the framework.
	 * @param node the handled node
	 * @param application the wicket application
	 */
	public void mountRequestMappers(AdminWicketApplication application, NavigationNode node);
	
}
