/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.leaf;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;

/**
 * This navigation leaf uses a fixed URL and is meant for linking external
 * pages from the navigation menu. See {@link AbstractBookmarkablePageNavigationLeaf}
 * for a navigation leaf that refers to a bookmarkable page instead.
 */
public final class UrlNavigationLeaf extends AbstractNavigationLeaf {

	/**
	 * the url
	 */
	private String url;

	/**
	 * Constructor.
	 * @param url the URL
	 */
	public UrlNavigationLeaf(final String url) {
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
	 * @see name.martingeisse.admin.navigation.AbstractNavigationNode#createLink(java.lang.String)
	 */
	@Override
	public AbstractLink createLink(final String id) {
		return new ExternalLink(id, url);
	}

}
