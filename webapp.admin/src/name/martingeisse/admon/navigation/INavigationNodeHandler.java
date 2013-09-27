/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admon.navigation;

import name.martingeisse.admon.application.wicket.AdminWicketApplication;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.RequestHandlerStack.ReplaceHandlerException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This interface is implemented to give navigation nodes their behavior.
 */
public interface INavigationNodeHandler extends IPageBarFactory {

	/**
	 * @param node the handled node
	 * @return the fallback id of the handled node -- to be used when the
	 * node has no ID set
	 */
	public String getFallbackId(NavigationNode node);

	/**
	 * @param node the handled node
	 * @return the title of the handled node -- to be used when the node
	 * has no title set
	 */
	public String getFallbackTitle(NavigationNode node);

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
	 * Creates a {@link ReplaceHandlerException}, typically to HTTP-redirect to
	 * the page that is represented by this navigation node, instead of just
	 * showing a link to that page.
	 * 
	 * This method requires a context component that is "the component to which
	 * the link from {{@link #createLink(String, NavigationNode)} would have been
	 * added, were a link needed and not a redirect". Some navigation node handlers
	 * require a context component to determine their page. For example,
	 * entity-instance nodes use the component (and in turn, the page) to determine
	 * the ID of the entity they link to.
	 * 
	 * @param node the handled node
	 * @param context the context component
	 * @return the {@link ReplaceHandlerException}
	 */
	public ReplaceHandlerException createReplaceHandlerException(NavigationNode node, Component context);

	/**
	 * Mounts the request mappers for the specified node in the Wicket Application,
	 * using this handler for the node.
	 * 
	 * @param node the handled node
	 * @param application the wicket application
	 */
	public void mountRequestMappers(AdminWicketApplication application, NavigationNode node);

	/**
	 * Checks whether this handler handles a canonical entity list node. Such a node
	 * is typically the one and only unfiltered list node for that entity. If so,
	 * returns the name of that entity. Otherwise returns null.
	 * 
	 * TODO: check if the concept of a "canonical entity list node" is useful at all.
	 * 
	 * @return the entity name or null
	 */
	public String getEntityNameForCanonicalEntityListNode();

}
