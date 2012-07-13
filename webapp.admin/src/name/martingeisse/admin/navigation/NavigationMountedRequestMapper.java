/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.MountedMapper;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;

/**
 * This mapper is used to mount pages based on navigation nodes. It creates
 * a "fake" page parameter for the navigation path that can be used by
 * the page. The primary use is to allow the page to return this path for
 * back-mapping the page to the navigation tree to mark the "current
 * location" in navigation panels.
 * 
 * This class actually maps nodes in multiple navigation trees as defined
 * by {@link NavigationTreeSelector}, and uses multiple page parameters
 * for that. The global navigation path is mandatory for all
 * navigation-mounted pages, while the entity-instance navigation path
 * is optional since it only applies to entity-instance pages.
 * 
 * When bookmarkable page links generate their URL, this mapper senses
 * the fake page parameters and returns its mounted path only if those
 * parameters match. That is, a bookmarkable page link will use the
 * mount point generated from a navigation node if that parameter is
 * included, and not use that mount point if the parameter is absent
 * (some other explicit mount point or the default mount point for
 * bookmarkable pages will kick in if that happens).
 * 
 * Note that this mapper does not work for the root URL. To mount an
 * arbitrary page at the root URL and still have it return the root
 * navigation path, subclass that page and implement {@link INavigationLocator}
 * manually.
 */
public class NavigationMountedRequestMapper extends MountedMapper {
	
	/**
	 * the globalNavigationPath
	 */
	private String globalNavigationPath;
	
	/**
	 * the entityInstanceNavigationPath
	 */
	private String entityInstanceNavigationPath;
	
	/**
	 * Constructor.
	 * @param globalNavigationPath the navigation path in the global navigation tree
	 * @param entityInstanceNavigationPath the navigation path in the entity-instance navigation
	 * tree, or null if this is not an entity-instance request mapper
	 * @param pageClass the page class to mount
	 */
	public NavigationMountedRequestMapper(final String globalNavigationPath, final String entityInstanceNavigationPath, final Class<? extends IRequestablePage> pageClass) {
		super(createMountPath(globalNavigationPath, entityInstanceNavigationPath), pageClass, new MyParametersEncoder(globalNavigationPath, entityInstanceNavigationPath));
		this.globalNavigationPath = globalNavigationPath;
		this.entityInstanceNavigationPath = entityInstanceNavigationPath;
	}
	
	/**
	 * Creates the mount path for the parent MountedMapper.
	 */
	private static String createMountPath(final String globalNavigationPath, final String entityInstanceNavigationPath) {
		if (entityInstanceNavigationPath == null) {
			return globalNavigationPath;
		} else if (entityInstanceNavigationPath.equals("/")) {
			return globalNavigationPath + "/${id}";
		} else {
			return globalNavigationPath + "/${id}" + entityInstanceNavigationPath;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.request.mapper.MountedMapper#buildUrl(org.apache.wicket.request.mapper.AbstractBookmarkableMapper.UrlInfo)
	 */
	@Override
	protected Url buildUrl(UrlInfo info) {
		
		// TODO: debugging
		System.out.println("buildUrl(): " + info.getPageClass() + " / {" + info.getPageParameters() +
			"}, globalNavigationPath: " + globalNavigationPath + ", entityInstanceNavigationPath: " + entityInstanceNavigationPath);
		
		// only map request handlers with our navigation path back to URLs
		final PageParameters parameters = info.getPageParameters();
		if (parameters == null) {
			System.out.println("no parameters -> failure");
			return null;
		}
		System.out.println("global path from parameters: " + NavigationTreeSelector.GLOBAL.getParameterValue(parameters));
		final boolean globalMatch = StringUtils.equals(globalNavigationPath, NavigationTreeSelector.GLOBAL.getParameterValue(parameters));
		System.out.println("entity instance path from parameters: " + NavigationTreeSelector.ENTITY_INSTANCE.getParameterValue(parameters));
		final boolean entityInstanceMatch = StringUtils.equals(entityInstanceNavigationPath, NavigationTreeSelector.ENTITY_INSTANCE.getParameterValue(parameters));
		if (globalMatch && entityInstanceMatch) {
			Url url =  super.buildUrl(info);
			System.out.println("success -> " + url);
			return url;
		} else {
			System.out.println("mismatch -> failure");
			return null;
		}
		
	}

	/**
	 * Obtains the current navigation path from the page parameters.
	 * @param treeSelector specifies the navigation tree to obtain the current node for
	 * @param parameters the page parameters
	 * @param required whether the parameter for the current node is required
	 * @return the current path, or null if no path was found and required is false
	 * @throws IllegalArgumentException if no path was found and required is true
	 */
	public static String getCurrentNavigationPath(final NavigationTreeSelector treeSelector, final PageParameters parameters, final boolean required) throws IllegalArgumentException {
		final String navigationPath = treeSelector.getParameterValue(parameters);
		if (navigationPath != null) {
			return navigationPath;
		} else if (required) {
			throw new IllegalArgumentException("page parameter for the navigation path is missing, selector: " + treeSelector);
		} else {
			return null;
		}
	}
	
	/**
	 * Obtains the current navigation node from the page parameters.
	 * @param tree the navigation tree to obtain the current node for
	 * @param parameters the page parameters
	 * @param required whether the parameter for the current node is required
	 * @return the current node, or null if no node was found and required is false
	 * @throws IllegalArgumentException if no node was found and required is true
	 */
	public static NavigationNode getCurrentNode(final NavigationTree tree, final PageParameters parameters, final boolean required) throws IllegalArgumentException {
		final String navigationPath = getCurrentNavigationPath(tree.getSelector(), parameters, required);
		final NavigationNode node = (navigationPath == null ? null : tree.getNodesByPath().get(navigationPath));
		if (node == null) {
			if (required) {
				throw new IllegalArgumentException("page parameter for the navigation path does not match any known path in tree " + tree.getSelector() + ": " + navigationPath);
			} else {
				return null;
			}
		}
		return node;
	}

	/**
	 * Customized {@link PageParametersEncoder} implementation.
	 */
	private static class MyParametersEncoder extends PageParametersEncoder {
		
		/**
		 * the globalNavigationPath
		 */
		private String globalNavigationPath;
		
		/**
		 * the entityInstanceNavigationPath
		 */
		private String entityInstanceNavigationPath;

		/**
		 * Constructor.
		 */
		MyParametersEncoder(String globalNavigationPath, String entityInstanceNavigationPath) {
			this.globalNavigationPath = globalNavigationPath;
			this.entityInstanceNavigationPath = entityInstanceNavigationPath;
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
			NavigationTreeSelector.GLOBAL.setParameterValue(parameters, globalNavigationPath);
			NavigationTreeSelector.ENTITY_INSTANCE.setParameterValue(parameters, entityInstanceNavigationPath);
			return parameters;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.request.mapper.parameter.PageParametersEncoder#encodePageParameters(org.apache.wicket.request.mapper.parameter.PageParameters)
		 */
		@Override
		public Url encodePageParameters(final PageParameters pageParameters) {
			final PageParameters copy = new PageParameters(pageParameters);
			NavigationTreeSelector.GLOBAL.setParameterValue(copy, null);
			NavigationTreeSelector.ENTITY_INSTANCE.setParameterValue(copy, null);
			return super.encodePageParameters(copy);
		}

	}

}
