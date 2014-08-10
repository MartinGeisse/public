/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.servlet;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import name.martingeisse.api.i18n.LocalizationUtil;
import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.api.request.ApiRequestException;
import name.martingeisse.api.request.ApiRequestHandlingFinishedException;
import name.martingeisse.api.request.ApiRequestPathNotFoundException;
import name.martingeisse.api.request.MalformedRequestPathException;
import org.apache.log4j.Logger;

/**
 * Helper class that contains common request cycle bootstrapping code for
 * both the servlet and the filter.
 */
public final class RequestCycleBootstrap {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(RequestCycleBootstrap.class);
	
	/**
	 * 
	 */
	static void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("----- new request -----------------------------");
		logger.debug(request.getRequestURI());
		
		// prepare the request cycle object
		ApiRequestCycle requestCycle;
		try {
			requestCycle = new ApiRequestCycle(request, response);
		} catch (MalformedRequestPathException e) {
			ServletUtil.emitResourceNotFoundResponse(request, response);
			return;
		}
		
		try {
			
			// set up localization for this thread
			Locale locale = null;
			String explicitLocaleId = request.getParameter("locale");
			if (explicitLocaleId != null) {
				if (explicitLocaleId.length() != 5 || explicitLocaleId.charAt(2) != '_') {
					ServletUtil.emitParameterErrorResponse(response, "invalid locale identifier: " + explicitLocaleId);
					return;
				}
				locale = new Locale(explicitLocaleId.substring(0, 2), explicitLocaleId.substring(3, 5));
			}
			if (locale == null) {
				locale = ApiRequestCycle.LOCALE_SESSION_KEY.get(requestCycle);
			}
			if (locale == null) {
				locale = Locale.US;
			}
			LocalizationUtil.createContextStack(locale);

			// invoke the application's main handler
			try {
				ApiConfiguration.getInstance().getMasterRequestHandler().handle(requestCycle, requestCycle.getRequestPath());
			} catch (ApiRequestHandlingFinishedException e) {
			} catch (ApiRequestPathNotFoundException e) {
				ServletUtil.emitResourceNotFoundResponse(requestCycle.getRequest(), requestCycle.getResponse());
			} catch (ApiRequestException e) {
				ServletUtil.emitParameterErrorResponse(response, e.getMessage());
			} catch (Exception e) {
				logger.error("unexpected exception", e);
				ServletUtil.emitInternalServerErrorResponse(response);
				return;
			}
			
		} finally {
			LocalizationUtil.removeContextStack();
		}
		
		
	}

}
