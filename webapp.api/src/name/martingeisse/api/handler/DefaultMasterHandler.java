/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.api.handler.intercept.JqueryHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestHandlingFinishedException;
import name.martingeisse.api.request.RequestParametersException;
import name.martingeisse.api.request.RequestPathChain;
import name.martingeisse.api.request.RequestPathNotFoundException;

import org.apache.log4j.Logger;

/**
 * The default implementation for the master handler.
 * This handler basically stores multiple handlers to which it
 * delegates the request:
 * 
 * - first, this handler checks "intercept handlers" that are
 *   stored for fixed URLs. Such handlers can provide a favicon,
 *   CSS or javascript files.
 *   
 * - any non-intercepted handler is passed to the application
 *   handler. This handler performs the main logic. Any
 *   other handlers are only invoked if the intercept handler
 *   or application handler throw an exception.
 * 
 * - any {@link RequestHandlingFinishedException} or
 *   {@link RequestParametersException} is re-thrown
 *   to be handled by the servlet
 *   
 * - any {@link RequestPathNotFoundException} causes the request
 *   to be passed to the "not found" handler
 *   
 * - any other exception, or any exception in the "not found" handler,
 *   or a {@link RequestPathNotFoundException} with no "no found"
 *   handler set, causes the request to be passed to the "exception"
 *   handler.
 * 
 * - if the exception handler is also missing or throws an exception,
 *   the exception is passed to the servlet.
 * 
 */
public class DefaultMasterHandler implements IRequestHandler {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(DefaultMasterHandler.class);
	
	/**
	 * the interceptHandlers
	 */
	private final Map<String, IRequestHandler> interceptHandlers;
	
	/**
	 * the applicationRequestHandler
	 */
	private IRequestHandler applicationRequestHandler;

	/**
	 * the notFoundRequestHandler
	 */
	private IRequestHandler notFoundRequestHandler;

	/**
	 * the exceptionHandler
	 */
	private IRequestHandler exceptionHandler;

	/**
	 * Constructor.
	 */
	public DefaultMasterHandler() {
		this.interceptHandlers = new HashMap<String, IRequestHandler>();
		this.interceptHandlers.put("/jquery.js", new JqueryHandler());
	}

	/**
	 * Getter method for the interceptHandlers.
	 * @return the interceptHandlers
	 */
	public Map<String, IRequestHandler> getInterceptHandlers() {
		return interceptHandlers;
	}
	
	/**
	 * Getter method for the applicationRequestHandler.
	 * @return the applicationRequestHandler
	 */
	public IRequestHandler getApplicationRequestHandler() {
		return applicationRequestHandler;
	}

	/**
	 * Setter method for the applicationRequestHandler.
	 * @param applicationRequestHandler the applicationRequestHandler to set
	 */
	public void setApplicationRequestHandler(final IRequestHandler applicationRequestHandler) {
		this.applicationRequestHandler = applicationRequestHandler;
	}

	/**
	 * Getter method for the notFoundRequestHandler.
	 * @return the notFoundRequestHandler
	 */
	public IRequestHandler getNotFoundRequestHandler() {
		return notFoundRequestHandler;
	}

	/**
	 * Setter method for the notFoundRequestHandler.
	 * @param notFoundRequestHandler the notFoundRequestHandler to set
	 */
	public void setNotFoundRequestHandler(final IRequestHandler notFoundRequestHandler) {
		this.notFoundRequestHandler = notFoundRequestHandler;
	}

	/**
	 * Getter method for the exceptionHandler.
	 * @return the exceptionHandler
	 */
	public IRequestHandler getExceptionHandler() {
		return exceptionHandler;
	}

	/**
	 * Setter method for the exceptionHandler.
	 * @param exceptionHandler the exceptionHandler to set
	 */
	public void setExceptionHandler(final IRequestHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		logger.debug("DefaultMasterHandler: dispatching request");
		try {
			try {
				IRequestHandler interceptHandler = interceptHandlers.get(path.getUri());
				if (interceptHandler == null) {
					logger.debug("DefaultMasterHandler: invoking application handler");
					applicationRequestHandler.handle(requestCycle, path);
					logger.debug("DefaultMasterHandler: application handler finished normally");
				} else {
					logger.debug("DefaultMasterHandler: invoking intercept handler " + interceptHandler);
					interceptHandler.handle(requestCycle, path);
					logger.debug("DefaultMasterHandler: intercept handler finished normally");
				}
				return;
			} catch (RequestPathNotFoundException e) {
				logger.debug("DefaultMasterHandler: caught RequestPathNotFoundException");
				if (notFoundRequestHandler == null) {
					logger.debug("DefaultMasterHandler: no notFoundRequestHandler -- rethrowing");
					throw e;
				} else {
					requestCycle.setException(e);
					logger.debug("DefaultMasterHandler: invoking notFoundRequestHandler handler");
					notFoundRequestHandler.handle(requestCycle, path);
					logger.debug("DefaultMasterHandler: notFoundRequestHandler finished normally");
					requestCycle.setException(null);
					return;
				}
			}
		} catch (Exception e) {
			logger.debug("DefaultMasterHandler: caught exception: " + e);
			if (exceptionHandler == null) {
				logger.debug("DefaultMasterHandler: no exceptionHandler -- rethrowing");
				throw e;
			} else {
				requestCycle.setException(e);
				logger.debug("DefaultMasterHandler: invoking exceptionHandler handler");
				exceptionHandler.handle(requestCycle, path);
				logger.debug("DefaultMasterHandler: exceptionHandler finished normally");
				requestCycle.setException(null);
				return;
			}
		}
	}
	
}
