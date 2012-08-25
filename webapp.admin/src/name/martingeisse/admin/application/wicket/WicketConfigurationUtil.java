/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import name.martingeisse.admin.application.ApplicationConfiguration;

import org.apache.wicket.authentication.IAuthenticationStrategy;
import org.apache.wicket.authentication.strategy.NoOpAuthenticationStrategy;
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
	 * The parameter key for the Wicket authentication strategy.
	 */
	public static final Class<IAuthenticationStrategy> WICKET_AUTHENTICATION_STRATEGY_PARAMETER_KEY = IAuthenticationStrategy.class;

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

	/**
	 * Getter method for the Wicket authentication strategy.
	 * @return the Wicket authentication strategy
	 */
	public static IAuthenticationStrategy getWicketAuthenticationStrategy() {
		return ApplicationConfiguration.get().getParameters().get(WICKET_AUTHENTICATION_STRATEGY_PARAMETER_KEY);
	}

	/**
	 * Setter method for the Wicket authentication strategy.
	 * @param wicketAuthenticationStrategy the Wicket authentication strategy to set
	 */
	public static void setWicketAuthenticationStrategy(final IAuthenticationStrategy wicketAuthenticationStrategy) {
		ApplicationConfiguration.get().getParameters().set(WICKET_AUTHENTICATION_STRATEGY_PARAMETER_KEY, wicketAuthenticationStrategy);
	}
	
	/**
	 * Determines the Wicket {@link IAuthenticationStrategy} to actually use.
	 * @return the authentication strategy
	 */
	public static IAuthenticationStrategy determineEffectiveWicketAuthenticationStrategy() {
		IAuthenticationStrategy strategy = getWicketAuthenticationStrategy();
		return (strategy == null ? new NoOpAuthenticationStrategy() : strategy);
	}

}
