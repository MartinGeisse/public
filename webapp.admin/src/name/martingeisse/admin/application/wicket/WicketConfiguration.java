/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import name.martingeisse.admin.application.CapabilityKey;
import name.martingeisse.admin.application.ParameterKey;

import org.apache.wicket.authentication.IAuthenticationStrategy;
import org.apache.wicket.authentication.strategy.NoOpAuthenticationStrategy;

/**
 * Wicket-related capability and parameter keys.
 */
public final class WicketConfiguration {

	/**
	 * The capability key for web application initialization contributors.
	 */
	public static final CapabilityKey<IWebApplicationInitializationContributor> webApplicationInitializationCapability = new CapabilityKey<IWebApplicationInitializationContributor>();
	
	/**
	 * The parameter key for the Wicket authentication strategy.
	 */
	public static final ParameterKey<IAuthenticationStrategy> wicketAuthenticationStrategyParameter = new ParameterKey<IAuthenticationStrategy>();

	/**
	 * Prevent instantiation.
	 */
	private WicketConfiguration() {
	}

	/**
	 * Determines the Wicket {@link IAuthenticationStrategy} to actually use.
	 * @return the authentication strategy
	 */
	public static IAuthenticationStrategy determineEffectiveWicketAuthenticationStrategy() {
		IAuthenticationStrategy strategy = wicketAuthenticationStrategyParameter.get();
		return (strategy == null ? new NoOpAuthenticationStrategy() : strategy);
	}

}
