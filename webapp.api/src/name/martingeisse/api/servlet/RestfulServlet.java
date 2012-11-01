/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.servlet;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.i18n.LocalizationUtil;
import name.martingeisse.api.request.MalformedRequestPathException;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestHandlingFinishedException;
import name.martingeisse.api.request.RequestParametersException;
import name.martingeisse.api.request.RequestPathNotFoundException;

/**
 * The servlet that handles all requests.
 */
public class RestfulServlet extends HttpServlet {

	/**
	 * the masterRequestHandler
	 */
	static IRequestHandler masterRequestHandler;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handle(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handle(request, response);
	}
	
	/**
	 * 
	 */
	private void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO remove
		System.out.println("-------------------------------------");
		System.out.println(request.getRequestURI());
		
		// prepare the request cycle object
		RequestCycle requestCycle;
		try {
			requestCycle = new RequestCycle(request, response);
		} catch (MalformedRequestPathException e) {
			ServletUtil.emitResourceNotFoundResponse(request, response);
			return;
		}
		
		// set up localization for this thread
		try {
			LocalizationUtil.createContextStack(Locale.US);

			// invoke the application's main handler
			try {
				masterRequestHandler.handle(requestCycle, requestCycle.getRequestPath());
			} catch (RequestHandlingFinishedException e) {
			} catch (RequestPathNotFoundException e) {
				ServletUtil.emitResourceNotFoundResponse(requestCycle.getRequest(), requestCycle.getResponse());
			} catch (RequestParametersException e) {
				ServletUtil.emitParameterErrorResponse(response, e.getMessage());
			} catch (Exception e) {
				e.printStackTrace(System.out);
				ServletUtil.emitInternalServerErrorResponse(response);
				return;
			}
			
		} finally {
			LocalizationUtil.removeContextStack();
		}
		
		
	}
	
}
