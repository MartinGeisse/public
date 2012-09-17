/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.security;

import name.martingeisse.admin.application.ParameterKey;
import name.martingeisse.admin.application.security.authentication.DefaultAuthenticationStrategy;
import name.martingeisse.admin.application.security.authentication.IAdminAuthenticationStrategy;
import name.martingeisse.admin.application.security.authorization.DefaultAuthorizationStrategy;
import name.martingeisse.admin.application.security.authorization.IAdminAuthorizationStrategy;
import name.martingeisse.admin.component.page.login.NopLoginPage;

import org.apache.wicket.markup.html.WebPage;

/**
 * This class contains security-related application parameters.
 */
public final class SecurityConfiguration {

	/**
	 * the parameterKey
	 */
	public static final ParameterKey<SecurityConfiguration> parameterKey = new ParameterKey<SecurityConfiguration>();
	
	/**
	 * Getter method for the security configuration. Throws an exception if the configuration is missing (null).
	 * @return the security configuration
	 */
	public static SecurityConfiguration getInstanceSafe() {
		SecurityConfiguration configuration = parameterKey.get();
		if (configuration == null) {
			throw new IllegalStateException("no security configuration");
		}
		return configuration;
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
