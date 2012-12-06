/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import name.martingeisse.api.servlet.ServletUtil;

/**
 * Stores information about a request cycle, such as {@link HttpServletRequest},
 * {@link HttpServletResponse}, decoded request data, etc.
 * 
 * The request cycle is able to store arbitrary attributes in addition to
 * the request parameters. The difference is that attributes are always
 * set programmatically (never by the client), but can store values of
 * arbitrary type, not just strings.
 */
public final class RequestCycle {

	/**
	 * the EXCEPTION_REQUEST_ATTRIBUTE_KEY
	 */
	public static final RequestAttributeKey<Throwable> EXCEPTION_REQUEST_ATTRIBUTE_KEY = new RequestAttributeKey<Throwable>(Throwable.class);
	
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
	 * the attributes
	 */
	private final Map<RequestAttributeKey<?>, Object> attributes;

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
		this.attributes = new HashMap<RequestAttributeKey<?>, Object>();

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
	 * Returns the value of a request attribute.
	 * @param key the attribute key
	 * @return the attribute value
	 */
	public <T> T getAttribute(RequestAttributeKey<T> key) {
		return key.cast(attributes.get(key));
	}

	/**
	 * Sets the value of a request attribute.
	 * @param key the attribute key
	 * @param value the attribute value
	 */
	public <T> void setAttribute(RequestAttributeKey<T> key, T value) {
		attributes.put(key, value);
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
	public void preparePlainTextResponse(final int statusCode) throws IOException {
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
	public void emitMessageResponse(final int statusCode, final String message) throws IOException {
		ServletUtil.emitMessageResponse(response, statusCode, message);
	}

	/**
	 * Sets the session object for the specified key.
	 * @param key the session key
	 * @param value the value to set
	 */
	public <T extends Serializable> void setSessionValue(SessionKey<T> key, T value) {
		request.getSession().setAttribute(key.getInternalKey(), value);
	}

	/**
	 * Obtains the session object for the specified key
	 * @param key the session key
	 * @return the value for the specified key
	 */
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T getSessionValue(SessionKey<T> key) {
		return (T)request.getSession().getAttribute(key.getInternalKey());
	}

}
