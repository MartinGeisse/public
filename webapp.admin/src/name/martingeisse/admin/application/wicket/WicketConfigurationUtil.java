/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import name.martingeisse.admin.application.ApplicationConfiguration;

import org.apache.wicket.authentication.IAuthenticationStrategy;
import org.apache.wicket.authentication.strategy.NoOpAuthenticationStrategy;

/**
 * Utilities to access Wicket-related configuration in the {@link ApplicationConfiguration}.
 */
public final class WicketConfigurationUtil {

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
