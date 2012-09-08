/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import name.martingeisse.common.util.ObjectStateUtil;
import name.martingeisse.common.util.ParameterUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.resource.loader.IStringResourceLoader;

/**
 * This class contributes an {@link IStringResourceLoader} to the
 * wicket application. The resource loader uses either a class
 * specified at construction or the concrete subclass of itself
 * as the origin for resource loading.
 */
public class StringResourceContributor extends AbstractWebApplicationInitializationContributor {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(StringResourceContributor.class);

	/**
	 * the origin
	 */
	private Class<?> origin;

	/**
	 * Constructor.
	 */
	public StringResourceContributor() {
		this.origin = getClass();
	}

	/**
	 * Constructor.
	 * @param origin the origin for resource loading
	 */
	public StringResourceContributor(final Class<?> origin) {
		this.origin = origin;
	}

	/**
	 * Getter method for the origin.
	 * @return the origin
	 */
	public Class<?> getOrigin() {
		return origin;
	}

	/**
	 * Setter method for the origin.
	 * @param origin the origin to set
	 */
	public void setOrigin(final Class<?> origin) {
		this.origin = origin;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.wicket.IWebApplicationInitializationContributor#onInitializeWebApplication(org.apache.wicket.protocol.http.WebApplication)
	 */
	@Override
	public void onInitializeWebApplication(final WebApplication webApplication) {
		ParameterUtil.ensureNotNull(webApplication, "webApplication");
		ObjectStateUtil.nullMeansMissing(origin, "origin");
		logger.debug("Adding StringResourceLoader for class: " + origin);
		webApplication.getResourceSettings().getStringResourceLoaders().add(new ClassStringResourceLoader(origin));
	}

}
