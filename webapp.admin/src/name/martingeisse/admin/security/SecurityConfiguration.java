/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.security;

import name.martingeisse.admin.application.pages.NopLoginPage;
import name.martingeisse.wicket.security.SecurityConfiguration;
import name.martingeisse.wicket.security.authentication.DefaultAuthenticationStrategy;
import name.martingeisse.wicket.security.authentication.IAuthenticationStrategy;
import name.martingeisse.wicket.security.authorization.DefaultAuthorizationStrategy;
import name.martingeisse.wicket.security.authorization.IAuthorizationStrategy;

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
	private IAuthenticationStrategy authenticationStrategy = new DefaultAuthenticationStrategy();

	/**
	 * the authorizationStrategy
	 */
	private IAuthorizationStrategy authorizationStrategy = new DefaultAuthorizationStrategy();

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
	public IAuthenticationStrategy getAuthenticationStrategy() {
		return authenticationStrategy;
	}

	/**
	 * Setter method for the authenticationStrategy.
	 * @param authenticationStrategy the authenticationStrategy to set
	 */
	public void setAuthenticationStrategy(final IAuthenticationStrategy authenticationStrategy) {
		this.authenticationStrategy = authenticationStrategy;
	}

	/**
	 * Getter method for the authorizationStrategy.
	 * @return the authorizationStrategy
	 */
	public IAuthorizationStrategy getAuthorizationStrategy() {
		return authorizationStrategy;
	}

	/**
	 * Setter method for the authorizationStrategy.
	 * @param authorizationStrategy the authorizationStrategy to set
	 */
	public void setAuthorizationStrategy(final IAuthorizationStrategy authorizationStrategy) {
		this.authorizationStrategy = authorizationStrategy;
	}

}
