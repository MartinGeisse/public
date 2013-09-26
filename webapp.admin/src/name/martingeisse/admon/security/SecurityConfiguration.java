/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admon.security;

import name.martingeisse.admon.application.pages.NopLoginPage;
import name.martingeisse.admon.security.authentication.DefaultAuthenticationStrategy;
import name.martingeisse.admon.security.authentication.IAdminAuthenticationStrategy;
import name.martingeisse.admon.security.authorization.DefaultAuthorizationStrategy;
import name.martingeisse.admon.security.authorization.IAdminAuthorizationStrategy;

import org.apache.wicket.markup.html.WebPage;

/**
 * This class contains security-related application parameters.
 */
public final class SecurityConfiguration {

	/**
	 * the instance
	 */
	private static SecurityConfiguration instance;
	
	/**
	 * Getter method for the instance.
	 * @return the instance
	 */
	public static SecurityConfiguration getInstance() {
		return instance;
	}
	
	/**
	 * Setter method for the instance.
	 * @param instance the instance to set
	 */
	public static void setInstance(SecurityConfiguration instance) {
		SecurityConfiguration.instance = instance;
	}
	
	/**
	 * Getter method for the security configuration. Throws an exception if the configuration is missing (null).
	 * @return the security configuration
	 */
	public static SecurityConfiguration getInstanceSafe() {
		if (SecurityConfiguration.instance == null) {
			throw new IllegalStateException("no security configuration");
		}
		return SecurityConfiguration.instance;
	}
	
	/**
	 * the loginPageClass
	 */
	private Class<? extends WebPage> loginPageClass = NopLoginPage.class;

	/**
	 * the authenticationStrategy
	 */
	private IAdminAuthenticationStrategy authenticationStrategy = new DefaultAuthenticationStrategy();

	/**
	 * the authorizationStrategy
	 */
	private IAdminAuthorizationStrategy authorizationStrategy = new DefaultAuthorizationStrategy();

	/**
	 * Constructor.
	 */
	public SecurityConfiguration() {
	}

	/**
	 * Getter method for the loginPageClass.
	 * @return the loginPageClass
	 */
	public Class<? extends WebPage> getLoginPageClass() {
		return loginPageClass;
	}

	/**
	 * Setter method for the loginPageClass.
	 * @param loginPageClass the loginPageClass to set
	 */
	public void setLoginPageClass(final Class<? extends WebPage> loginPageClass) {
		this.loginPageClass = loginPageClass;
	}

	/**
	 * Getter method for the authenticationStrategy.
	 * @return the authenticationStrategy
	 */
	public IAdminAuthenticationStrategy getAuthenticationStrategy() {
		return authenticationStrategy;
	}

	/**
	 * Setter method for the authenticationStrategy.
	 * @param authenticationStrategy the authenticationStrategy to set
	 */
	public void setAuthenticationStrategy(final IAdminAuthenticationStrategy authenticationStrategy) {
		this.authenticationStrategy = authenticationStrategy;
	}

	/**
	 * Getter method for the authorizationStrategy.
	 * @return the authorizationStrategy
	 */
	public IAdminAuthorizationStrategy getAuthorizationStrategy() {
		return authorizationStrategy;
	}

	/**
	 * Setter method for the authorizationStrategy.
	 * @param authorizationStrategy the authorizationStrategy to set
	 */
	public void setAuthorizationStrategy(final IAdminAuthorizationStrategy authorizationStrategy) {
		this.authorizationStrategy = authorizationStrategy;
	}

}
