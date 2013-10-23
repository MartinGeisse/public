/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;


import java.util.ArrayList;

import name.martingeisse.common.util.ParameterUtil;

import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.INamedParameters.NamedPair;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;
import org.apache.wicket.util.lang.Objects;

/**
 * This mapper takes a {@link PageParameters} object containing
 * implicit parameters. It will merge these parameters with the
 * incoming parameters, and only map requests with matching values
 * back to an URL.
 * 
 * When merging parameters, the implicit ones take precedence. This
 * ensures (a) that mapping an URL to a request and back to an URL,
 * the same URL results, and (b) that the request cannot override the
 * application's expectations when mounting the page.
 * 
 * This class also supports default parameters with lower precedence
 * that can be overridden by incoming parameters. These parameters 
 * do not take part in back-mapping to an URL, so they just allow
 * to specify different defaults for missing parameters at different
 * mount points.
 * 
 * Terminology: The implicit parameters affecting mapping are called
 * "identifying parameters". The lower-precedence ones are called
 * "implicit default parameters".
 * 
 * Both the identifying parameters, and parameters matching their
 * implicit default ones, are removed when back-mapping to an URL,
 * to produce a nice clean URL without exposing the fact that the
 * same page class is used in the background.
 * 
 * By being a {@link MountedMapper}, this mapper also allows to specify
 * parameters as path segments as well as in the URL's query string.
 * These two are handled as before, and take precedence between the
 * identifying and implicit default parameters.
 * 
 * Note that this mapper does not work for the root URL, because that
 * URL uses a special mapper.
 * 
 * NOTE: Only named parameters are currently supported! Other (e.g.
 * indexed) parameters in the identifying parameters or implicit
 * default parameters are currently ignored!
 */
public final class ParameterMountedRequestMapper extends MountedMapper {

	/**
	 * the mountPath
	 */
	private final String mountPath;
	
	/**
	 * the identifyingParameters
	 */
	private final PageParameters identifyingParameters;
	
	/**
	 * the implicitDefaultParameters
	 */
	private final PageParameters implicitDefaultParameters;
	
	/**
	 * Constructor.
	 * @param mountPath the mount path as defined by {@link MountedMapper}
	 * @param pageClass the page class to mount
	 * @param identifyingParameters the identifying parameters for this mapper
	 */
	public ParameterMountedRequestMapper(final String mountPath, final Class<? extends IRequestablePage> pageClass, PageParameters identifyingParameters) {
		this(mountPath, pageClass, identifyingParameters, null, new MyParametersEncoder());
	}
	
	/**
	 * Constructor.
	 * @param mountPath the mount path as defined by {@link MountedMapper}
	 * @param pageClass the page class to mount
	 * @param identifyingParameters the identifying parameters for this mapper
	 * @param implicitDefaultParameters the implicit default parameters for this mapper
	 */
	public ParameterMountedRequestMapper(final String mountPath, final Class<? extends IRequestablePage> pageClass, PageParameters identifyingParameters, PageParameters implicitDefaultParameters) {
		this(mountPath, pageClass, identifyingParameters, implicitDefaultParameters, new MyParametersEncoder());
	}
	
	/**
	 * Constructor.
	 * @param mountPath the mount path as defined by {@link MountedMapper}
	 * @param pageClass the page class to mount
	 * @param identifyingParameters the identifying parameters for this mapper
	 * @param implicitDefaultParameters the implicit default parameters for this mapper
	 * @param parametersEncoder the parameters encoder
	 */
	private ParameterMountedRequestMapper(final String mountPath, final Class<? extends IRequestablePage> pageClass, PageParameters identifyingParameters, PageParameters implicitDefaultParameters, final MyParametersEncoder parametersEncoder) {
		super(ParameterUtil.ensureNotNull(mountPath, "mountPath"), ParameterUtil.ensureNotNull(pageClass, "pageClass"), parametersEncoder);
		this.mountPath = mountPath;
		this.identifyingParameters = (identifyingParameters == null ? new PageParameters() : new PageParameters(identifyingParameters));
		this.implicitDefaultParameters = implicitDefaultParameters;
		parametersEncoder.mapper = this;
	}

	/**
	 * Getter method for the mountPath.
	 * @return the mountPath
	 */
	public String getMountPath() {
		return mountPath;
	}
	
	/**
	 * Getter method for the identifyingParameters.
	 * @return the identifyingParameters
	 */
	public PageParameters getIdentifyingParameters() {
		return identifyingParameters;
	}
	
	/**
	 * Getter method for the implicitDefaultParameters.
	 * @return the implicitDefaultParameters
	 */
	public PageParameters getImplicitDefaultParameters() {
		return implicitDefaultParameters;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.request.mapper.MountedMapper#buildUrl(org.apache.wicket.request.mapper.AbstractBookmarkableMapper.UrlInfo)
	 */
	@Override
	protected Url buildUrl(final UrlInfo info) {
		
		// only build the URL if the identifying parameters match
		PageParameters actualParameters = info.getPageParameters();
		for (NamedPair expectedPair : identifyingParameters.getAllNamed()) {
			String expectedValue = expectedPair.getValue();
			String actualValue = (actualParameters == null ? null : actualParameters.get(expectedPair.getKey()).toString());
			if (!Objects.equal(expectedValue, actualValue)) {
				return null;
			}
		}
		return super.buildUrl(info);
		
	}

	/**
	 * Customized {@link PageParametersEncoder} implementation.
	 */
	private static class MyParametersEncoder extends PageParametersEncoder {

		/**
		 * the mapper
		 */
		private ParameterMountedRequestMapper mapper;

		/**
		 * Constructor.
		 */
		MyParametersEncoder() {
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.request.mapper.parameter.PageParametersEncoder#decodePageParameters(org.apache.wicket.request.Request)
		 */
		@Override
		public PageParameters decodePageParameters(final Url url) {
			PageParameters specifiedParameters = super.decodePageParameters(url);
			PageParameters returnedParameters = new PageParameters();
			if (mapper.implicitDefaultParameters != null) {
				returnedParameters.mergeWith(mapper.implicitDefaultParameters);
			}
			if (specifiedParameters != null) {
				returnedParameters.mergeWith(specifiedParameters);
			}
			returnedParameters.mergeWith(mapper.identifyingParameters);
			return returnedParameters;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.request.mapper.parameter.PageParametersEncoder#encodePageParameters(org.apache.wicket.request.mapper.parameter.PageParameters)
		 */
		@Override
		public Url encodePageParameters(PageParameters pageParameters) {
			
			// make a copy so we don't destroy the caller's object
			pageParameters = new PageParameters(pageParameters);
			
			// the identifying parameters must match to reach this point; remove them for a nice clean URL
			for (final String key : mapper.identifyingParameters.getNamedKeys()) {
				pageParameters.remove(key);
			}
			
			// then remove the parameters with an implicit default if their value is the implicit default one
			if (mapper.implicitDefaultParameters != null) {
				ArrayList<String> defaultMatchingParameterNames = new ArrayList<String>(mapper.implicitDefaultParameters.getAllNamed().size());
				for (final NamedPair pair : mapper.implicitDefaultParameters.getAllNamed()) {
					String name = pair.getKey();
					String defaultValue = pair.getValue();
					String actualValue = pageParameters.get(name).toString();
					if (Objects.equal(defaultValue, actualValue)) {
						defaultMatchingParameterNames.add(name);
					}
				}
				for (String defaultMatchingParameterName : defaultMatchingParameterNames) {
					pageParameters.remove(defaultMatchingParameterName);
				}
			}
			
			// encode the remaining parameters as usual
			return super.encodePageParameters(pageParameters);
			
		}

	}

}
