/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.MountedMapper;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;
import org.apache.wicket.util.string.StringValue;

/**
 * This mapper is used to mount pages based on navigation nodes. It creates
 * a "fake" page parameter for the navigation path that can be used by
 * the page. The primary use is to allow the page to return this path for
 * back-mapping the page to the navigation tree to mark the "current
 * location" in navigation panels.
 * 
 * By default, the URL used for mounting is the same as the navigation path.
 * Note that links to the mounted page must use the URL specified here
 * unless the page class is mounted only once. Otherwise the mapping from
 * page class to URL is ambiguous and you cannot be sure that the link uses
 * the URL for this request mapper; other URLs will pass through other
 * request mappers and thus not include the fake page parameter.
 */
public class NavigationMountedRequestMapper extends MountedMapper {

	/**
	 * the PAGE_PARAMETER_NAME
	 */
	public static final String PAGE_PARAMETER_NAME = "name.martingeisse.admin.navigation.path";

	/**
	 * the navigationPath
	 */
	private String navigationPath;
	
	/**
	 * Constructor.
	 * @param navigationPath the navigation path, which is also the mount path
	 * @param pageClass the page class to mount
	 */
	public NavigationMountedRequestMapper(final String navigationPath, final Class<? extends IRequestablePage> pageClass) {
		super(navigationPath, pageClass, new MyParametersEncoder(navigationPath));
		this.navigationPath = navigationPath;
	}

	/**
	 * Constructor.
	 * @param mountPath the mount path
	 * @param pageClass the page class to mount
	 * @param navigationPath the navigation path
	 */
	public NavigationMountedRequestMapper(final String mountPath, final Class<? extends IRequestablePage> pageClass, final String navigationPath) {
		super(mountPath, pageClass, new MyParametersEncoder(navigationPath));
		this.navigationPath = navigationPath;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.request.mapper.MountedMapper#buildUrl(org.apache.wicket.request.mapper.AbstractBookmarkableMapper.UrlInfo)
	 */
	@Override
	protected Url buildUrl(UrlInfo info) {
		// only map request handlers with our navigation path back to URLs
		final PageParameters parameters = info.getPageParameters();
		final StringValue navigationPath = (parameters == null ? null : parameters.get(PAGE_PARAMETER_NAME));
		if (navigationPath != null && navigationPath.toString().equals(this.navigationPath)) {
			return super.buildUrl(info);
		} else {
			return null;
		}
	}
	
	/**
	 * Obtains the current navigation node from the page parameters.
	 * @param parameters the page parameters
	 * @param required whether the parameter for the current node is required
	 * @return the current node, or null if no node was found and required is false
	 * @throws IllegalArgumentException if no node was found and required is true
	 */
	public static INavigationNode getCurrentNode(final PageParameters parameters, final boolean required) throws IllegalArgumentException {

		// obtain the navigation path
		final String navigationPath = parameters.get(PAGE_PARAMETER_NAME).toString();
		if (navigationPath == null) {
			if (required) {
				throw new IllegalArgumentException("page parameter for the navigation path is missing");
			} else {
				return null;
			}
		}

		// obtain the resulting node
		final INavigationNode node = NavigationConfigurationUtil.getNavigationTree().getNodesByPath().get(navigationPath);
		if (node == null) {
			if (required) {
				throw new IllegalArgumentException("page parameter for the navigation path does not match any known path: " + navigationPath);
			} else {
				return null;
			}
		}

		return node;
	}

	/**
	 * Obtains the current navigation node from the page parameters and casts it to the specified
	 * expected node class.
	 * @param <T> the expected node type
	 * @param parameters the page parameters
	 * @param required whether the parameter for the current node is required
	 * @param expectedNodeClass the expected node class
	 * @return the current node, or null if no node was found and required is false
	 * @throws IllegalArgumentException if no node was found and required is true
	 */
	public static <T extends INavigationNode> T getCurrentNode(final PageParameters parameters, final boolean required, final Class<T> expectedNodeClass) throws IllegalArgumentException {
		final INavigationNode node = getCurrentNode(parameters, required);
		if (node == null) {
			return null;
		}
		try {
			return expectedNodeClass.cast(node);
		} catch (final ClassCastException e) {
			throw new IllegalArgumentException("invalid node type for navigation path '" + node.getPath() + "'. Expected " + expectedNodeClass.getCanonicalName() + ", found " + node.getClass().getCanonicalName());
		}
	}

	/**
	 * Customized {@link PageParametersEncoder} implementation.
	 */
	private static class MyParametersEncoder extends PageParametersEncoder {

		/**
		 * the navigationPath
		 */
		private final String navigationPath;

		/**
		 * Constructor.
		 */
		MyParametersEncoder(final String navigationPath) {
			this.navigationPath = navigationPath;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.request.mapper.parameter.PageParametersEncoder#decodePageParameters(org.apache.wicket.request.Request)
		 */
		@Override
		public PageParameters decodePageParameters(final Request request) {
			PageParameters parameters = super.decodePageParameters(request);
			if (parameters == null) {
				parameters = new PageParameters();
			}
			parameters.add(PAGE_PARAMETER_NAME, navigationPath);
			return parameters;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.request.mapper.parameter.PageParametersEncoder#encodePageParameters(org.apache.wicket.request.mapper.parameter.PageParameters)
		 */
		@Override
		public Url encodePageParameters(final PageParameters pageParameters) {
			final PageParameters copy = new PageParameters(pageParameters);
			copy.remove(PAGE_PARAMETER_NAME);
			return super.encodePageParameters(copy);
		}

	}

}
