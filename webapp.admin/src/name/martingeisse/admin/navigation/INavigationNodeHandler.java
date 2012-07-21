/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.component.pageborder.IPageBorderFactory;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

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
	 * Implementation note: Since callers might modify the returned
	 * link, it must not share state with other objects. Links in
	 * particular must not share state between each other. As an
	 * example, if the returned links are {@link BookmarkablePageLink}s,
	 * then each link must have its own {@link PageParameters},
	 * since the caller might add or modify parameters.
	 * (The fact that callers cannot replace the {@link PageParameters} of
	 * such a link by a cloned {@link PageParameters} -- there is no
	 * such setter method -- has driven the decision to shield callers
	 * from shared state.) 
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

	/**
	 * Checks whether this handler handles a canonical entity list node. Such a node
	 * is typically the one and only unfiltered list node for that entity. If so,
	 * returns the name of that entity. Otherwise returns null.
	 * 
	 * @return the entity name or null
	 */
	public String getEntityNameForCanonicalEntityListNode();
	
}
