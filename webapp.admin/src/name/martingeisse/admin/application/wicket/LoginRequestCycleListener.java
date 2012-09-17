/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import name.martingeisse.admin.application.security.SecurityConfiguration;
import name.martingeisse.admin.application.security.SecurityUtil;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.ReturnValueUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.IPageClassRequestHandler;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;

/**
 * This listener causes an intercept-redirect to the login page if the
 * user is not logged in, and also blocks access to requests other than
 * resources in that case.
 */
public class LoginRequestCycleListener extends AbstractRequestCycleListener {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(LoginRequestCycleListener.class);
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.request.cycle.AbstractRequestCycleListener#onRequestHandlerResolved(org.apache.wicket.request.cycle.RequestCycle, org.apache.wicket.request.IRequestHandler)
	 */
	@Override
	public void onRequestHandlerResolved(final RequestCycle cycle, final IRequestHandler handler) {
		logger.trace("LoginRequestCycleListener: checking...");
		ParameterUtil.ensureNotNull(cycle, "cycle");
		ParameterUtil.ensureNotNull(handler, "handler");
		
		// this handler has no business with the request if he user is logged in
		if (SecurityUtil.isLoggedIn()) {
			logger.trace("user is logged in -> ok");
			return;
		}

		// decide by handler type whether the request is allowed, blocked or redirected
		logger.trace("user is not logged in -> checking handler...");
		if (handler instanceof IPageClassRequestHandler) {

			// handler for a page -- allowed if it is the login page
			final IPageClassRequestHandler pageClassRequestHandler = (IPageClassRequestHandler)handler;
			final Class<?> pageClass = pageClassRequestHandler.getPageClass();
			logger.trace("handler is for page: " + pageClass);
			final Class<? extends Page> loginPageClass = SecurityConfiguration.getInstanceSafe().getLoginPageClass();
			ReturnValueUtil.nullMeansMissing(loginPageClass, "security configuration: login page class");
			if (pageClass != loginPageClass) {
				logger.trace("sending intercept-redirect to the login page");
				throw new RestartResponseAtInterceptPageException(loginPageClass);
			}
			logger.trace("accepted.");
			return;

		} else if (handler instanceof ResourceReferenceRequestHandler || handler instanceof ResourceRequestHandler || handler instanceof ResourceStreamRequestHandler) {

			// accessing resources is allowed
			logger.trace("handler is for a resource -> ok");
			return;

		} else if (handler.getClass().getCanonicalName().equals("org.apache.wicket.request.flow.ResetResponseException.ResponseResettingDecorator")) {
			
			// needed for ResetResponseException to work
			logger.trace("handler is from a ResetResponseException");
			return;
			
		} else if (handler.getClass().getCanonicalName().equals("org.apache.wicket.protocol.https.SwitchProtocolRequestHandler")) {
			
			// needed for automatically switching to HTTPS
			logger.trace("handler is a SwitchProtocolRequestHandler");
			return;
			
		}
		
		// any other type of request handler is blocked
		// TODO: BufferedResponseRequestHandler probably needed for redirect-to-render in more complex login pages
		logger.trace("blocking handler: " + handler + " / " + handler.getClass().getCanonicalName());
		throw new AbortWithHttpErrorCodeException(403);

	}

}
