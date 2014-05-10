/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import name.martingeisse.api.servlet.ServletUtil;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;

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
	 * the useSessions
	 */
	private static volatile boolean useSessions = true;
	
	/**
	 * Setter method for the useSessions.
	 * @param useSessions the useSessions to set
	 */
	public static void setUseSessions(boolean useSessions) {
		RequestCycle.useSessions = useSessions;
	}
	
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
	 * Returns either the raw POST body, or the value of the "body" POST parameter
	 * (if the Content-Type is "application/x-www-form-urlencoded"), as a {@link Reader}.
	 * 
	 * @return the reader
	 * @throws IOException on I/O errors
	 */
	public Reader getBodyAsReader() throws IOException {
		if (isFormRequest()) {
			return new StringReader(parameters.getString("body", true));
		} else {
			return new InputStreamReader(request.getInputStream(), Charset.forName("utf-8"));
		}
	}
	
	/**
	 * Returns either the raw POST body, or the value of the "body" POST parameter
	 * (if the Content-Type is "application/x-www-form-urlencoded"), as a {@link String}.
	 * 
	 * @return the string
	 * @throws IOException on I/O errors
	 */
	public String getBodyAsString() throws IOException {
		if (isFormRequest()) {
			return parameters.getString("body", true);
		} else {
			return IOUtils.toString(request.getInputStream(), Charset.forName("utf-8"));
		}
	}

	/**
	 * Returns either the raw POST body, or the value of the "body" POST parameter
	 * (if the Content-Type is "application/x-www-form-urlencoded"), as a {@link JsonAnalyzer}.
	 * 
	 * @return the JSON analyuer
	 * @throws IOException on I/O errors
	 */
	public JsonAnalyzer getBodyAsJsonAnalyzer() throws IOException {
		if (isFormRequest()) {
			return JsonAnalyzer.parse(parameters.getString("body", true));
		} else {
			return JsonAnalyzer.parse(new InputStreamReader(request.getInputStream(), Charset.forName("utf-8")));
		}
	}
	
	/**
	 * 
	 */
	private boolean isFormRequest() {
		return request.getContentType().startsWith("application/x-www-form-urlencoded");
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
	 * Renders an HTML page that shows a form to manually enter a request body. Only works
	 * in combination with the getBodyAs*() methods or similar that can accept a POST form
	 * parameter named "body" instead of a raw POST body.
	 * 
	 * @throws IOException on I/O errors
	 */
	public void emitBodyFormPage() throws IOException {
		
		// build the page
		StringBuilder builder = new StringBuilder();
		builder.append("<html><body><form method=\"post\" style=\"width: 800px; margin-left: auto; margin-right: auto; \">");
		builder.append("<textarea name=\"body\" style=\"display: block; width: 100%; height: 300px; margin-top: 30px; font-family: monospace; font-size: 12pt; \">{\n}</textarea>");
		builder.append("<input type=\"submit\" value=\"Submit\" style=\"display: block; width: 100%; height: 40px; margin-top: 20px; background-color: #ddd; border: 1px solid #aaa; \">");
		builder.append("</form></body></html>");
		
		// emit the response
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().print(builder);
		response.getWriter().flush();
		response.getWriter().close();
		
	}

	/**
	 * Sets the session object for the specified key.
	 * @param key the session key
	 * @param value the value to set
	 */
	public <T extends Serializable> void setSessionValue(SessionKey<T> key, T value) {
		if (useSessions) {
			request.getSession().setAttribute(key.getInternalKey(), value);
		}
	}

	/**
	 * Obtains the session object for the specified key
	 * @param key the session key
	 * @return the value for the specified key
	 */
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T getSessionValue(SessionKey<T> key) {
		if (useSessions) {
			return (T)request.getSession().getAttribute(key.getInternalKey());
		} else {
			return null;
		}
	}

}
