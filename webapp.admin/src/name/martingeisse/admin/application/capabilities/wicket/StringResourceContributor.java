/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities.wicket;

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
	public StringResourceContributor(Class<?> origin) {
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
	public void setOrigin(Class<?> origin) {
		this.origin = origin;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.wicket.IWebApplicationInitializationContributor#onInitializeWebApplication(org.apache.wicket.protocol.http.WebApplication)
	 */
	@Override
	public void onInitializeWebApplication(WebApplication webApplication) {
		// TODO: proper logging, e.g. the fact that this contributor is active should be logged!
		webApplication.getResourceSettings().getStringResourceLoaders().add(new ClassStringResourceLoader(origin));
	}

}
