/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handlers;

import name.martingeisse.admin.navigation.NavigationNode;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.request.RequestHandlerStack.ReplaceHandlerException;
import org.apache.wicket.request.flow.RedirectToUrlException;

/**
 * This handler uses a fixed URL and is meant for linking external
 * pages from the navigation menu. See {@link BookmarkablePageNavigationHandler}
 * for a navigation handler that refers to a bookmarkable page instead.
 */
public final class UrlNavigationHandler extends AbstractNavigationNodeHandler {

	/**
	 * the url
	 */
	private String url;

	/**
	 * Constructor.
	 * @param url the URL
	 */
	public UrlNavigationHandler(final String url) {
		this.url = url;
	}

	/**
	 * Getter method for the url.
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Setter method for the url.
	 * @param url the url to set
	 */
	public void setUrl(final String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createLink(java.lang.String, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public AbstractLink createLink(final String id, final NavigationNode node) {
		return new ExternalLink(id, url);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createReplaceHandlerException(name.martingeisse.admin.navigation.NavigationNode, org.apache.wicket.Component)
	 */
	@Override
	public ReplaceHandlerException createReplaceHandlerException(NavigationNode node, Component context) {
		return new RedirectToUrlException(url);
	}
	
}
