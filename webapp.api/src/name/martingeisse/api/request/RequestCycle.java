/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import name.martingeisse.api.servlet.ServletUtil;
import name.martingeisse.api.util.IParameterSet;

/**
 * Stores information about a request cycle, such as {@link HttpServletRequest},
 * {@link HttpServletResponse}, decoded request data, etc.
 */
public final class RequestCycle implements IParameterSet {

	/**
	 * the request
	 */
	private final HttpServletRequest request;

	/**
	 * the response
	 */
	private final HttpServletResponse response;

	/**
	 * the requestPath
	 */
	private final RequestPathChain requestPath;

	/**
	 * Constructor.
	 * @param request the request
	 * @param response the response
	 * @throws MalformedRequestPathException if the request path (taken from the request URI) is malformed
	 */
	public RequestCycle(final HttpServletRequest request, final HttpServletResponse response) throws MalformedRequestPathException {
		this.request = request;
		this.response = response;

		final String uri = request.getRequestURI();
		final String requestPathText = (uri.startsWith("/") ? uri.substring(1) : uri);
		this.requestPath = RequestPathChain.parse(requestPathText);
	}

	/**
	 * Getter method for the request.
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * Getter method for the response.
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * Getter method for the requestPath.
	 * @return the requestPath
	 */
	public RequestPathChain getRequestPath() {
		return requestPath;
	}

	/**
	 * @return the writer of the response
	 * @throws IOException on I/O errors
	 */
	public PrintWriter getWriter() throws IOException {
		return response.getWriter();
	}

	/**
	 * @return the output stream of the response
	 * @throws IOException on I/O errors
	 */
	public OutputStream getOutputStream() throws IOException {
		return response.getOutputStream();
	}
	
	/**
	 * Prepares a plain-text response (text/plain, utf-8). Leaves the status code of the response alone.
	 * @throws IOException on I/O errors
	 */
	public void preparePlainTextResponse() throws IOException {
		ServletUtil.preparePlainTextResponse(response);
	}

	/**
	 * Prepares a plain-text response (text/plain, utf-8), using the specified status code.
	 * @param statusCode the HTTP status code to use
	 * @throws IOException on I/O errors
	 */
	public void preparePlainTextResponse(int statusCode) throws IOException {
		ServletUtil.preparePlainTextResponse(response, statusCode);
	}
	
	/**
	 * Finishes a text-based response. This method can be used for any response the uses
	 * the {@link HttpServletResponse}'s {@link Writer}.
	 * @throws IOException on I/O errors
	 */
	public void finishTextResponse() throws IOException {
		ServletUtil.finishTextResponse(response);
	}
	
	/**
	 * Writes a response using the specified status code and content, with content type
	 * text/plain (utf-8).
	 * @param statusCode the HTTP status code
	 * @param message the message to write
	 * @throws IOException on I/O errors
	 */
	public void emitMessageResponse(int statusCode, String message) throws IOException {
		ServletUtil.emitMessageResponse(response, statusCode, message);
	}

	/**
	 * Writes a 404 response for the request.
	 * @throws IOException on I/O errors
	 */
	public void emitResourceNotFoundResponse() throws IOException {
		ServletUtil.emitResourceNotFoundResponse(request, response);
	}

	/**
	 * Writes a 500 response about an internal server error (typically an unexpected exception).
	 * @throws IOException on I/O errors
	 */
	public void emitInternalServerErrorResponse() throws IOException {
		ServletUtil.emitInternalServerErrorResponse(response);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.util.IParameterSet#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String name) {
		return request.getParameter(name);
	}

}
