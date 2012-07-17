/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;


import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.navigation.INavigationLocator;
import name.martingeisse.admin.navigation.INavigationNodeHandler;
import name.martingeisse.admin.navigation.NavigationMountedRequestMapper;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.NavigationPageParameterUtil;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Base class for handlers that use a bookmarkable page. This mapper
 * mounts the page at the navigation path using a mapper that
 * includes the implicit navigation path parameter in the page
 * parameters. For navigation back-mapping, it is sufficient that
 * the page properly passes the page parameters to {@link WebPage}
 * in its constructor; if this is not possible, it must implement
 * {@link INavigationLocator}.
 * 
 * This class stores two {@link PageParameters} objects that
 * represent the explicit and implicit page parameters to use
 * for the mounted page. Explicit parameters are visible to the
 * client as URL segments or in the query string. Implicit
 * parameters are not visible but implied by the base URL
 * of the mount point.
 * 
 * The mount path parameter is special: It must be specified
 * in the parameters when generating an URL (e.g. for
 * {@link BookmarkablePageLink}, but is then removed from the
 * parameters, not visible to the client and added implicitly
 * when the mount point is used to access a page. This class
 * stores this special parameter in the explicit parameter
 * set (in the mountRequestMappers() method); code using this
 * class should not change or remove that parameter.
 */
public class BookmarkablePageNavigationHandler extends AbstractNavigationNodeHandler {

	/**
	 * the pageClass
	 */
	private final Class<? extends WebPage> pageClass;

	/**
	 * the explicitPageParameters
	 */
	private final PageParameters explicitPageParameters;

	/**
	 * the implicitPageParameters
	 */
	private final PageParameters implicitPageParameters;

	/**
	 * Constructor.
	 * @param pageClass the page class to link to
	 */
	public BookmarkablePageNavigationHandler(final Class<? extends WebPage> pageClass) {
		this.pageClass = pageClass;
		this.explicitPageParameters = new PageParameters();
		this.implicitPageParameters = new PageParameters();
	}

	/**
	 * Getter method for the pageClass.
	 * @return the pageClass
	 */
	public final Class<? extends WebPage> getPageClass() {
		return pageClass;
	}

	/**
	 * Getter method for the explicitPageParameters.
	 * @return the explicitPageParameters
	 */
	public final PageParameters getExplicitPageParameters() {
		return explicitPageParameters;
	}
	
	/**
	 * Getter method for the implicitPageParameters.
	 * @return the implicitPageParameters
	 */
	public final PageParameters getImplicitPageParameters() {
		return implicitPageParameters;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createClone()
	 */
	@Override
	public INavigationNodeHandler createClone() {
		BookmarkablePageNavigationHandler clone = new BookmarkablePageNavigationHandler(pageClass);
		clone.mergeDataFrom(this);
		return clone;
	}
	
	/**
	 * Merges the implicit and explicit parameters, id and title from the specified other handler
	 * into the this handler. This method must not be used after mounting. Used to
	 * implement clone().
	 */
	protected final void mergeDataFrom(BookmarkablePageNavigationHandler other) {
		setId(other.getId());
		setTitle(other.getTitle());
		getImplicitPageParameters().mergeWith(other.getImplicitPageParameters());
		getExplicitPageParameters().mergeWith(other.getExplicitPageParameters());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.AbstractNavigationNodeHandler#mountRequestMappers(name.martingeisse.admin.application.wicket.AdminWicketApplication, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public final void mountRequestMappers(AdminWicketApplication application, NavigationNode node) {
		explicitPageParameters.remove(NavigationPageParameterUtil.NAVIGATION_PATH_PAGE_PARAMETER_NAME);
		explicitPageParameters.add(NavigationPageParameterUtil.NAVIGATION_PATH_PAGE_PARAMETER_NAME, node.getPath());
		NavigationMountedRequestMapper mapper = new NavigationMountedRequestMapper(node.getPath(), pageClass);
		mapper.getImplicitParameters().mergeWith(implicitPageParameters);
		application.mount(mapper);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createLink(java.lang.String, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public final AbstractLink createLink(String id, NavigationNode node) {
		return new BookmarkablePageLink<Void>(id, pageClass, explicitPageParameters);
	}

}
