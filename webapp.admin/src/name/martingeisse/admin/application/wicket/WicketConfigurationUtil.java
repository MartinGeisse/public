/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import name.martingeisse.admin.application.ApplicationConfiguration;

import org.apache.wicket.protocol.http.WebApplication;

/**
 * Utilities to access Wicket-related configuration in the {@link ApplicationConfiguration}.
 */
public final class WicketConfigurationUtil {

	/**
	 * The capability key for web application initialization contributors.
	 */
	public static final Class<IWebApplicationInitializationContributor> WEB_APPLICATION_INITIALIZATION_CONTRIBUTOR_CAPABILITY_KEY = IWebApplicationInitializationContributor.class;

	/**
	 * Prevent instantiation.
	 */
	private WicketConfigurationUtil() {
	}

	/**
	 * Adds the specified application initialization contributor.
	 * @param contributor the contributor to add
	 */
	public static void addWebApplicationInitializationContributor(final IWebApplicationInitializationContributor contributor) {
		ApplicationConfiguration.get().getCapabilities().add(WEB_APPLICATION_INITIALIZATION_CONTRIBUTOR_CAPABILITY_KEY, contributor);
	}

	/**
	 * @return an {@link Iterable} for all application initialization contributors.
	 */
	public static Iterable<IWebApplicationInitializationContributor> getWebApplicationInitializationContributors() {
		return ApplicationConfiguration.get().getCapabilities().getIterable(WEB_APPLICATION_INITIALIZATION_CONTRIBUTOR_CAPABILITY_KEY);
	}

	/**
	 * Invokes all web application initialization contributors for the specified application.
	 * @param webApplication the Wicket web application
	 */
	static void invokeWebApplicationInitializationContributors(final WebApplication webApplication) {
		for (final IWebApplicationInitializationContributor contributor : getWebApplicationInitializationContributors()) {
			contributor.onInitializeWebApplication(webApplication);
		}
	}

}
