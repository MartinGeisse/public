/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.slave_services.application.security;

import name.martingeisse.common.security.ISecurityProvider;
import name.martingeisse.common.security.LoginData;
import name.martingeisse.common.security.authentication.IAuthenticationStrategy;
import name.martingeisse.common.security.authorization.IAuthorizationStrategy;
import name.martingeisse.wicket.application.MyWicketSession;

import org.apache.log4j.Logger;

/**
 * Main security strategy.
 */
public class SlaveServicesSecurityProvider implements ISecurityProvider {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(SlaveServicesSecurityProvider.class);
	
	/**
	 * the authenticationStrategy
	 */
	private final IAuthenticationStrategy authenticationStrategy = new AuthenticationStrategy();
	
	/**
	 * the authorizationStrategy
	 */
	private final IAuthorizationStrategy authorizationStrategy = new AuthorizationStrategy();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.ISecurityProvider#getAuthenticationStrategy()
	 */
	@Override
	public IAuthenticationStrategy getAuthenticationStrategy() {
		return authenticationStrategy;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.ISecurityProvider#getAuthorizationStrategy()
	 */
	@Override
	public IAuthorizationStrategy getAuthorizationStrategy() {
		return authorizationStrategy;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.ISecurityProvider#onLogin(name.martingeisse.common.security.LoginData)
	 */
	@Override
	public void onLogin(LoginData loginData) {
		
		// clear previous session
		logger.trace("replacing HTTP session");
		final MyWicketSession session = MyWicketSession.get();
		session.getDataContainer().clear();
		session.replaceSession();
		logger.trace("HTTP session replaced");

		// establish new session
		logger.trace("storing login data");
		session.getDataContainer().set(LoginData.class, loginData);
		session.dirty();
		logger.trace("login data stored");
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.ISecurityProvider#getLoginData()
	 */
	@Override
	public LoginData getLoginData() {
		return MyWicketSession.get().getDataContainer().get(LoginData.class);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.security.ISecurityProvider#onLogout()
	 */
	@Override
	public void onLogout() {
		logger.debug("a user is trying to log out");
		final MyWicketSession session = MyWicketSession.get();
		session.getDataContainer().clear();
		session.invalidateNow();
		logger.info("user logged out");
	}
	
}
