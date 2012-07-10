/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import java.io.Serializable;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;

/**
 * This interface is implemented to give navigation nodes their behavior.
 * 
 * TODO: This interface should not extend {@link Serializable}. Instead,
 * the {@link NavigationNode}s should keep an index of all nodes and
 * pages containing links generated from nodes should refer to the
 * nodes using an index number model.
 */
public interface INavigationNodeHandler extends Serializable {

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
	 * This method may return null to indicate that a node cannot be
	 * linked to. This happens for folder nodes if the node presentation
	 * does not support folder pages. It may happen for leaf nodes,
	 * in which case the node should be presented as "disabled".
	 * 
	 * @param id the wicket id
	 * @param node the handled node
	 * @return the link or null
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
