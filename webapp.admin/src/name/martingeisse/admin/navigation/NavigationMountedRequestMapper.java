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
 * When bookmarkable page links generate their URL, this mapper senses
 * the fake page parameter and returns its mounted path only if this
 * parameter matches. That is, a bookmarkable page link will use the
 * mount point generated from a navigation node if that parameter is
 * included, and not use that mount point if the parameter is absent
 * (some other explicit mount point or the default mount point for
 * bookmarkable pages will kick in if that happens).
 * 
 * Note that this mapper does not work for the root URL. To mount an
 * arbitrary page at the root URL and still have it return the root
 * navigation path, subclass that page and implement {@link INavigationLocator}
 * manually.
 * 
 * This mapper also allows to specify page parameters that are implicitly
 * added when a request is mapped to a page, and removed when a page
 * class / parameters pair is mapped to an URL. This allows a
 * navigation path (and its URL) to represent parameter assignments
 * not explicitly present in the URL.
 */
public class NavigationMountedRequestMapper extends MountedMapper {

	/**
	 * the parametersEncoder
	 */
	private final MyParametersEncoder parametersEncoder;
	
	/**
	 * Constructor.
	 * @param navigationPath the navigation path, which is also the mount path
	 * @param pageClass the page class to mount
	 */
	public NavigationMountedRequestMapper(final String navigationPath, final Class<? extends IRequestablePage> pageClass) {
		this(navigationPath, pageClass, new MyParametersEncoder(navigationPath));
	}

	/**
	 * Helper constructor to get access to the parameters encoder.
	 */
	private NavigationMountedRequestMapper(final String navigationPath, final Class<? extends IRequestablePage> pageClass, MyParametersEncoder parametersEncoder) {
		super(navigationPath, pageClass, parametersEncoder);
		this.parametersEncoder = parametersEncoder;
	}
	
	/**
	 * Getter method for the navigationPath.
	 * @return the navigationPath
	 */
	public String getNavigationPath() {
		return parametersEncoder.getNavigationPath();
	}
	
	/**
	 * Getter method for the implicitParameters.
	 * @return the implicitParameters
	 */
	public PageParameters getImplicitParameters() {
		return parametersEncoder.getImplicitParameters();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.request.mapper.MountedMapper#buildUrl(org.apache.wicket.request.mapper.AbstractBookmarkableMapper.UrlInfo)
	 */
	@Override
	protected Url buildUrl(UrlInfo info) {
		/* This methods performs an additional check so it only maps page/parameter combinations
		 * back to URLs that have the implicit navigation path parameter set to the value
		 * stored in this mapper.
		 */
		if (StringUtils.equals(getNavigationPath(), NavigationPageParameterUtil.getParameterValue(info.getPageParameters()))) {
			return super.buildUrl(info);
		} else {
			return null;
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
		 * the implicitParameters
		 */
		private final PageParameters implicitParameters;
		
		/**
		 * Constructor.
		 */
		MyParametersEncoder(String navigationPath) {
			this.navigationPath = navigationPath;
			this.implicitParameters = new PageParameters();
		}
		
		/**
		 * Getter method for the navigationPath.
		 * @return the navigationPath
		 */
		public String getNavigationPath() {
			return navigationPath;
		}
		
		/**
		 * Getter method for the implicitParameters.
		 * @return the implicitParameters
		 */
		public PageParameters getImplicitParameters() {
			return implicitParameters;
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
			parameters.mergeWith(implicitParameters);
			NavigationPageParameterUtil.setParameterValue(parameters, navigationPath);
			return parameters;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.request.mapper.parameter.PageParametersEncoder#encodePageParameters(org.apache.wicket.request.mapper.parameter.PageParameters)
		 */
		@Override
		public Url encodePageParameters(final PageParameters pageParameters) {
			final PageParameters copy = new PageParameters(pageParameters);
			NavigationPageParameterUtil.setParameterValue(copy, null);
			for (String key : implicitParameters.getNamedKeys()) {
				copy.remove(key);
			}
			return super.encodePageParameters(copy);
		}

	}

}
