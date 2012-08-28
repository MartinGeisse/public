/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import name.martingeisse.restful.application.ApplicationRequestHandler;
import name.martingeisse.restful.handler.RequestHandlingFinishedException;
import name.martingeisse.restful.handler.RequestParametersException;
import name.martingeisse.restful.request.MalformedRequestPathException;
import name.martingeisse.restful.request.RequestCycle;

/**
 * The servlet that handles all requests.
 */
public class RestfulServlet extends HttpServlet {

	/**
	 * the applicationRequestHandler
	 */
	private ApplicationRequestHandler applicationRequestHandler = new ApplicationRequestHandler();
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// prepare the request cycle object
		RequestCycle requestCycle;
		try {
			requestCycle = new RequestCycle(request, response);
		} catch (MalformedRequestPathException e) {
			ServletUtil.emitResourceNotFoundResponse(request, response);
			return;
		}
		
		// invoke the application's main handler
		try {
			applicationRequestHandler.handle(requestCycle, requestCycle.getRequestPath());
		} catch (RequestHandlingFinishedException e) {
		} catch (RequestParametersException e) {
			ServletUtil.emitParameterErrorResponse(response, e.getMessage());
		} catch (Exception e) {
			ServletUtil.emitInternalServerErrorResponse(response);
			e.printStackTrace();
			return;
		}
		
	}
	
}
