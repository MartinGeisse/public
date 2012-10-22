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

/**
 * Stores information about a request cycle, such as {@link HttpServletRequest},
 * {@link HttpServletResponse}, decoded request data, etc.
 * 
 * This class distinguishes "plain" requests -- the normal usage mode for
 * a service API -- and "user interface" requests.  The latter are useful
 * when the service API is called directly through a browser, and are
 * intended to "upgrade" the API result from a plain-text data structure
 * (such as JSON) to a full interactive HTML response. Such an upgraded
 * response is obviously useless to an API client. Therefore, this class
 * assumes "user interface" mode only if a normal browser is recognized
 * through the User-Agent HTTP header AND the user does not explicitly
 * request plain mode through the "__plain" request parameter.
 * 
 * The request cycle is able to store an exception. This is useful when
 * delegating to another handler for caught exceptions since request
 * parameters can only hold strings.
 */
public final class RequestCycle {

	/**
	 * the request
	 */
	private final HttpServletRequest request;

	/**
	 * the response
	 */
	private final HttpServletResponse response;

	/**
	 * the requestMethod
	 */
	private final RequestMethod requestMethod;

	/**
	 * the requestPath
	 */
	private final RequestPathChain requestPath;

	/**
	 * the parameters
	 */
	private final RequestParameters parameters;

	/**
	 * the plainRequest
	 */
	private final boolean plainRequest;
	
	/**
	 * the exception
	 */
	private Exception exception;

	/**
	 * Constructor.
	 * @param request the request
	 * @param response the response
	 * @throws MalformedRequestPathException if the request path (taken from the request URI) is malformed
	 */
	public RequestCycle(final HttpServletRequest request, final HttpServletResponse response) throws MalformedRequestPathException {
		this.request = request;
		this.response = response;

		this.requestMethod = (request.getMethod().equalsIgnoreCase("POST") ? RequestMethod.POST : RequestMethod.GET);
		final String uri = request.getRequestURI();
		final String requestPathText = (uri.startsWith("/") ? uri.substring(1) : uri);
		this.requestPath = RequestPathChain.parse(requestPathText);
		this.parameters = new RequestParameters(request);

		final Boolean plainParameter = parameters.getBoolean("__plain", false);
		if (plainParameter != null) {
			this.plainRequest = plainParameter;
		} else {
			final String userAgent = request.getHeader("User-Agent");
			this.plainRequest = (userAgent == null || !userAgent.contains("Mozilla"));
		}

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
	 * Getter method for the requestMethod.
	 * @return the requestMethod
	 */
	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	/**
	 * Getter method for the requestPath.
	 * @return the requestPath
	 */
	public RequestPathChain getRequestPath() {
		return requestPath;
	}

	/**
	 * Getter method for the parameters.
	 * @return the parameters
	 */
	public RequestParameters getParameters() {
		return parameters;
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
	 * Getter method for the plainRequest.
	 * @return the plainRequest
	 */
	public boolean isPlainRequest() {
		return plainRequest;
	}
	
	/**
	 * Getter method for the exception.
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}
	
	/**
	 * Setter method for the exception.
	 * @param exception the exception to set
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}

	/**
	 * Prepares a plain-text response (text/plain, utf-8). Leaves the status code of the response alone.
	 * @throws IOException on I/O errors
	 */
	public void preparePlainTextResponse() throws IOException {
		ServletUtil.preparePlainTextResponse(response);
	}

	/**
	 * Prepares an HTML response (text/html, utf-8). Leaves the status code of the response alone.
	 * @throws IOException on I/O errors
	 */
	public void prepareHtmlResponse() throws IOException {
		ServletUtil.prepareHtmlResponse(response);
	}

	/**
	 * Prepares either a plain-text response (text/plain, utf-8) or a HTML response (text/html, utf-8),
	 * depending on whether this request is in plain mode or user interface mode.
	 * Leaves the status code of the response alone.
	 * @throws IOException on I/O errors
	 */
	public void prepareUpgradablePlainTextResponse() throws IOException {
		if (plainRequest) {
			preparePlainTextResponse();
		} else {
			prepareHtmlResponse();
		}
	}

	/**
	 * Prepares a plain-text response (text/plain, utf-8), using the specified status code.
	 * @param statusCode the HTTP status code to use
	 * @throws IOException on I/O errors
	 */
	public void preparePlainTextResponse(final int statusCode) throws IOException {
		ServletUtil.preparePlainTextResponse(response, statusCode);
	}

	/**
	 * Prepares an HTML response (text/html, utf-8), using the specified status code.
	 * @param statusCode the HTTP status code to use
	 * @throws IOException on I/O errors
	 */
	public void prepareHtmlResponse(final int statusCode) throws IOException {
		ServletUtil.prepareHtmlResponse(response, statusCode);
	}

	/**
	 * Prepares either a plain-text response (text/plain, utf-8) or a HTML response (text/html, utf-8),
	 * depending on whether this request is in plain mode or user interface mode.
	 * Uses the specified status code.
	 * @param statusCode the HTTP status code to use
	 * @throws IOException on I/O errors
	 */
	public void prepareUpgradablePlainTextResponse(final int statusCode) throws IOException {
		if (plainRequest) {
			preparePlainTextResponse(statusCode);
		} else {
			prepareHtmlResponse(statusCode);
		}
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
	public void emitMessageResponse(final int statusCode, final String message) throws IOException {
		ServletUtil.emitMessageResponse(response, statusCode, message);
	}

	/**
	 * Prints a line about a handler decorator to the response. Since this method is only useful
	 * for upgraded HTML responses, this method does nothing in plain mode.
	 * @param text the text line to print
	 * @throws IOException on I/O errors
	 */
	public void printDecoratorInfoLine(String text) throws IOException {
		if (!plainRequest) {
			response.getWriter().println("<div class=\"decorated\">" + text + "</div>");
		}
	}
	
}
