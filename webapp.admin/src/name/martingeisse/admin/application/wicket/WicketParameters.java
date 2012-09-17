/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import name.martingeisse.admin.application.ParameterKey;

import org.apache.wicket.authentication.IAuthenticationStrategy;
import org.apache.wicket.authentication.strategy.NoOpAuthenticationStrategy;

/**
 * Wicket-related parameter keys.
 */
public final class WicketParameters {

	/**
	 * The parameter key for the Wicket authentication strategy.
	 */
	public static final ParameterKey<IAuthenticationStrategy> wicketAuthenticationStrategyParameter = new ParameterKey<IAuthenticationStrategy>();

	/**
	 * Prevent instantiation.
	 */
	private WicketParameters() {
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
