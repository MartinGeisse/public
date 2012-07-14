/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;


import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.navigation.INavigationLocator;
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
 * This class stores its own {@link PageParameters} object that
 * is a copy of the one passed in the constructor. mountRequestMappers()
 * adds the implicit parameter to the copy. Clients and subclasses
 * should not remove or change this parameter.
 * 
 * TODO: Implement other parameters that are implicitly set by the
 * navigation-mounted request mapper. Such parameters should be
 * passed on request->handler mapping, cleared on ->URL mapping
 * but need not be checked in ->URL mapping since the navigation
 * path is unique.
 * The extra parameters should be stored in a container inside this
 * class. Distinguish visible and implicit parameter; special case
 * is the mount path.
 * 
 */
public class BookmarkablePageNavigationHandler extends AbstractNavigationNodeHandler {

	/**
	 * the pageClass
	 */
	private final Class<? extends WebPage> pageClass;

	/**
	 * the pageParameters
	 */
	private final PageParameters pageParameters;

	/**
	 * Constructor.
	 * @param pageClass the page class to link to
	 */
	public BookmarkablePageNavigationHandler(final Class<? extends WebPage> pageClass) {
		this(pageClass, null);
	}

	/**
	 * Constructor.
	 * @param pageClass the page class to link to
	 * @param pageParameters the page parameters to use in the link (will be copied)
	 */
	public BookmarkablePageNavigationHandler(final Class<? extends WebPage> pageClass, final PageParameters pageParameters) {
		this.pageClass = pageClass;
		this.pageParameters = new PageParameters(pageParameters);
		this.pageParameters.remove(NavigationPageParameterUtil.NAVIGATION_PATH_PAGE_PARAMETER_NAME);
	}

	/**
	 * Getter method for the pageClass.
	 * @return the pageClass
	 */
	public Class<? extends WebPage> getPageClass() {
		return pageClass;
	}

	/**
	 * Getter method for the pageParameters.
	 * @return the pageParameters
	 */
	public PageParameters getPageParameters() {
		return pageParameters;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.AbstractNavigationNodeHandler#mountRequestMappers(name.martingeisse.admin.application.wicket.AdminWicketApplication, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public void mountRequestMappers(AdminWicketApplication application, NavigationNode node) {
		
		// The navigation path lies somewhere between implicit and explicit parameters --
		// it must be specified in pageClass/pageParameter when generating an URL (e.g. in
		// a bookmarable page link), but it is added and removed on request mapping like
		// an implicit parameter. Here we add it to the explicit parameters used for
		// URL generation (URL generation itself will recognize, then remove it).
		pageParameters.add(NavigationPageParameterUtil.NAVIGATION_PATH_PAGE_PARAMETER_NAME, node.getPath());

		// mount the URL for this node, allowing subclasses to add implicit parameters
		NavigationMountedRequestMapper mapper = new NavigationMountedRequestMapper(node.getPath(), pageClass);
		addImplicitParameters(mapper.getImplicitParameters());
		application.mount(mapper);
		
	}
	
	/**
	 * Subclasses may implement this method to add implicit page parameters to the specified
	 * parameter set. These parameters are passed to the mounted page implicitly whenever
	 * the page is reached through its navigation-mounted URL. The default implementation
	 * does nothing.
	 * 
	 * @param parameters the parameter set to add to
	 */
	protected void addImplicitParameters(PageParameters parameters) {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createLink(java.lang.String, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public AbstractLink createLink(String id, NavigationNode node) {
		return new BookmarkablePageLink<Void>(id, pageClass, pageParameters);
	}

}
