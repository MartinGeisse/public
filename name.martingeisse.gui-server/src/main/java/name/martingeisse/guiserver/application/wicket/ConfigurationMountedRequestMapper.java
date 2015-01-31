/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.application.wicket;


import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;
import org.apache.wicket.util.string.StringValue;

/**
 * This mapper is used to mount pages based on the GUI configuration.
 * It creates a "fake" page parameter for the configuration path that
 * can be used by the page to locate its configuration.
 * 
 * When bookmarkable page links generate their URL, this mapper senses
 * the fake page parameter and returns its mounted path only if this
 * parameter matches. That is, a bookmarkable page link will use the
 * mount point matched by this mapper if that parameter is included,
 * and not use that mount point if the parameter is absent or doesn't
 * match. Usually, the default mount point for bookmarkable pages will
 * kick in if that happens).
 * 
 * Note that this mapper does not work for the root URL.
 * 
 * This mapper also allows to specify page parameters that are implicitly
 * added when a request is mapped to a page, and removed when a page
 * class / parameters pair is mapped to an URL. This allows a URL to
 * represent parameter assignments not explicitly present in the URL.
 * These assignments aren't used for matching or generating URLs, though,
 * nor are they checked when checking if this mapper is the right one to
 * generate a URL.
 */
public final class ConfigurationMountedRequestMapper extends MountedMapper {

	/**
	 * The name of the implicit page parameter.
	 */
	public static final String CONFIGURATION_PATH_PAGE_PARAMETER_NAME = "__INTERNAL__CONFIGURATION__PATH__";

	/**
	 * the parametersEncoder
	 */
	private final MyParametersEncoder parametersEncoder;

	/**
	 * Constructor.
	 * @param urlPath the path in the URL to mount this page at
	 * @param pageClass the page class to mount
	 */
	public ConfigurationMountedRequestMapper(final String urlPath, final Class<? extends IRequestablePage> pageClass) {
		this(urlPath, pageClass, new MyParametersEncoder(urlPath));
	}

	/**
	 * Helper constructor to get access to the parameters encoder.
	 */
	private ConfigurationMountedRequestMapper(final String urlPath, final Class<? extends IRequestablePage> pageClass, final MyParametersEncoder parametersEncoder) {
		super(urlPath, pageClass, parametersEncoder);
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
	protected Url buildUrl(final UrlInfo info) {
		/* This methods performs an additional check so it only maps page/parameter combinations
		 * back to URLs that have the implicit navigation path parameter set to the value
		 * stored in this mapper.
		 */
		if (StringUtils.equals(getNavigationPath(), NavigationUtil.getParameterValue(info.getPageParameters()))) {
			return super.buildUrl(info);
		} else {
			return null;
		}
	}

	/**
	 * Returns the value of the implicit navigation path parameter.
	 * 
	 * @param parameters the page parameters to take the value from (may be null)
	 * @return the parameter value, or null if no such parameter is set
	 * or if the parameters argument is null
	 */
	public static String getParameterValue(final PageParameters parameters) {
		if (parameters == null) {
			return null;
		}
		final StringValue value = parameters.get(CONFIGURATION_PATH_PAGE_PARAMETER_NAME);
		return (value == null ? null : value.toString());
	}

	/**
	 * Returns the value of the implicit navigation path parameter,
	 * optionally enforcing that the parameter is present.
	 * 
	 * @param parameters the page parameters to take the value from (may be null)
	 * @param required whether the path is required
	 * @return the parameter value, or null if no such parameter is set
	 * or if the parameters argument is null (only possible if the required flag
	 * is false).
	 * @throws IllegalArgumentException if no path was found and required is true
	 */
	public static String getParameterValue(final PageParameters parameters, final boolean required) throws IllegalArgumentException {
		final String navigationPath = getParameterValue(parameters);
		if (navigationPath != null) {
			return navigationPath;
		} else if (required) {
			throw new IllegalArgumentException("page parameter for the navigation path is missing from parameters: " + parameters);
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the implicit navigation path parameter to the specified
	 * value, or clears it if the value is null.
	 * 
	 * @param parameters the parameters collection to set the parameter for
	 * @param value the value, or null to clear
	 */
	public static void setParameterValue(final PageParameters parameters, final String value) {
		if (parameters == null) {
			throw new IllegalArgumentException("'parameters' argument cannot be null");
		} else if (value == null) {
			parameters.remove(CONFIGURATION_PATH_PAGE_PARAMETER_NAME);
		} else {
			parameters.set(CONFIGURATION_PATH_PAGE_PARAMETER_NAME, value);
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
		MyParametersEncoder(final String navigationPath) {
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
		public PageParameters decodePageParameters(final Url url) {
			PageParameters parameters = super.decodePageParameters(url);
			if (parameters == null) {
				parameters = new PageParameters();
			}
			for (final String key : implicitParameters.getNamedKeys()) {
				parameters.remove(key);
			}
			parameters.mergeWith(implicitParameters);
			NavigationUtil.setParameterValue(parameters, navigationPath);
			return parameters;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.request.mapper.parameter.PageParametersEncoder#encodePageParameters(org.apache.wicket.request.mapper.parameter.PageParameters)
		 */
		@Override
		public Url encodePageParameters(final PageParameters pageParameters) {
			final PageParameters copy = new PageParameters(pageParameters);
			NavigationUtil.setParameterValue(copy, null);
			for (final String key : implicitParameters.getNamedKeys()) {
				copy.remove(key);
			}
			return super.encodePageParameters(copy);
		}

	}
	
}
