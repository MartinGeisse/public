/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.leaf;


import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This navigation leaf implementation stores the linked page class and
 * page parameters directly.
 */
public final class BookmarkablePageNavigationLeaf extends AbstractBookmarkablePageNavigationLeaf {

	/**
	 * the pageClass
	 */
	private Class<? extends WebPage> pageClass;

	/**
	 * the pageParameters
	 */
	private PageParameters pageParameters;

	/**
	 * Constructor.
	 */
	public BookmarkablePageNavigationLeaf() {
		this.pageClass = null;
		this.pageParameters = null;
	}

	/**
	 * Constructor.
	 * @param pageClass the page class to link to
	 */
	public BookmarkablePageNavigationLeaf(final Class<? extends WebPage> pageClass) {
		this.pageClass = pageClass;
		this.pageParameters = null;
	}

	/**
	 * Constructor.
	 * @param pageClass the page class to link to
	 * @param pageParameters the page parameters to use in the link
	 */
	public BookmarkablePageNavigationLeaf(final Class<? extends WebPage> pageClass, final PageParameters pageParameters) {
		this.pageClass = pageClass;
		this.pageParameters = pageParameters;
	}

	/**
	 * Getter method for the pageClass.
	 * @return the pageClass
	 */
	public Class<? extends WebPage> getPageClass() {
		return pageClass;
	}

	/**
	 * Setter method for the pageClass.
	 * @param pageClass the pageClass to set
	 */
	public void setPageClass(final Class<? extends WebPage> pageClass) {
		this.pageClass = pageClass;
	}

	/**
	 * Getter method for the pageParameters.
	 * @return the pageParameters
	 */
	public PageParameters getPageParameters() {
		return pageParameters;
	}

	/**
	 * Setter method for the pageParameters.
	 * @param pageParameters the pageParameters to set
	 */
	public void setPageParameters(final PageParameters pageParameters) {
		this.pageParameters = pageParameters;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.AbstractNavigationNode#createLink(java.lang.String)
	 */
	@Override
	public AbstractLink createLink(String id) {
		return new BookmarkablePageLink<Void>(id, pageClass, pageParameters);
	}

}
