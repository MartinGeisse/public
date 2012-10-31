/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.delegate;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestMethod;
import name.martingeisse.api.request.RequestPathChain;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * This handler delegates requests to another (remote) API.
 */
public abstract class AbstractDelegateApiHandler implements IRequestHandler {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(AbstractDelegateApiHandler.class);
	
	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		HttpServletRequest originalRequest = requestCycle.getRequest();
		HttpServletResponse originalResponse = requestCycle.getResponse();
		
		// log the request
		if (logger.isDebugEnabled()) {
			logger.debug("----- sending request to delegate API -----");
			logger.debug("incoming " + originalRequest.getMethod() + " request");
			logger.debug("path info: " + originalRequest.getPathInfo());
			logger.debug("query string: " + originalRequest.getQueryString());
			logger.debug("context path: " + originalRequest.getContextPath());
			logger.debug("servlet path: " + originalRequest.getServletPath());
		}

		// build the URL to send the delegate request to
		String url = buildUrl(requestCycle, path);

		// Create and configure an HttpClient. The single-cookie-header is recommended to work around
		// bugs in many CGI scripts; it forces the HttpClient to send all cookies in a single header.
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter("http.protocol.single-cookie-header", true);
		configureHttpClient(httpClient);

		// create the request
		HttpUriRequest delegateRequest = buildRequest(requestCycle, path, url);
		
		// get the current time for profiling of the delegate API
		long startTime = System.nanoTime();
		
		// perform the delegate API request
		HttpResponse delegateResponse = httpClient.execute(delegateRequest);
		
		// get the current time for profiling of the delegate API
		long endTime = System.nanoTime();
		long deltaTime = (endTime - startTime);
		logger.debug("delta time: " + ((endTime - startTime) / 1000000) + " ms");
		consumeTiming(requestCycle, path, url, delegateRequest, delegateResponse, deltaTime);

		// copy response headers from the delegate response to the original response
		copyResponseHeaders(requestCycle, path, url, delegateRequest, delegateResponse);

		// copy the response body to the original response and log it
		InputStream delegateResponseBodyStream = delegateResponse.getEntity().getContent();
		OutputStream originalResponseBodyStream = originalResponse.getOutputStream();
		ByteArrayOutputStream captureStream = new ByteArrayOutputStream();
		OutputStream teeOutputStream = new TeeOutputStream(originalResponseBodyStream, captureStream);
		IOUtils.copy(delegateResponseBodyStream, teeOutputStream);
		originalResponse.getOutputStream().flush();
		originalResponse.getOutputStream().close();
		String charset = EntityUtils.getContentCharSet(delegateResponse.getEntity());
		if (charset == null) {
			charset = "ISO-8859-1";
		}
		logger.debug("response body: " + captureStream.toString(charset));
		
	}

	/**
	 * Returns the request URL relative to the servlet, including the query string.
	 * @param requestCycle the request cycle
	 * @param path the request path
	 * @return the relative URL (starts with a slash character)
	 */
	protected final String getRelativeUrl(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		HttpServletRequest request = requestCycle.getRequest();
		String result = request.getPathInfo();
		if (request.getQueryString() != null) {
			result += "?" + request.getQueryString();
		}
		return result;
	}
	
	/**
	 * Builds the URL for the delegate request.
	 * @param requestCycle the request cycle
	 * @param path the request path
	 * @return the absolute delegate URL
	 */
	protected abstract String buildUrl(RequestCycle requestCycle, RequestPathChain path) throws Exception;
	
	/**
	 * Subclasses can override this method to apply custom configuration to the specified
	 * {@link HttpClient}. The default implementation does nothing.
	 * @param httpClient the client to configure
	 */
	protected void configureHttpClient(HttpClient httpClient) throws Exception {
	}

	/**
	 * This method builds the delegate request. The default implementation delegates to
	 * buildGetRequest for GET requests, buildPostRequest for POST requests. It throws
	 * an exception for other requests.
	 * @param requestCycle the original request cycle
	 * @param path the original request path
	 * @param url the URL of the request to build
	 * @return the request
	 */
	protected HttpUriRequest buildRequest(RequestCycle requestCycle, RequestPathChain path, String url) throws Exception {
		if (requestCycle.getRequestMethod() == RequestMethod.GET) {
			return buildGetRequest(requestCycle, path, url);
		} else if (requestCycle.getRequestMethod() == RequestMethod.POST) {
			return buildPostRequest(requestCycle, path, url);
		} else {
			throw new RuntimeException("request method not supported: " + requestCycle.getRequestMethod());
		}
	}

	/**
	 * Builds a delegate request in case the original request was a GET request.
	 * The default implementation builds an {@link HttpGet}.
	 * @param requestCycle the original request cycle
	 * @param path the original request path
	 * @param url the URL of the request to build
	 * @return the request
	 */
	protected HttpUriRequest buildGetRequest(RequestCycle requestCycle, RequestPathChain path, String url) throws Exception {
		return new HttpGet(url);
	}

	/**
	 * Builds a delegate request in case the original request was a POST request.
	 * The default implementation builds an {@link HttpPost} using the body from
	 * the original request.
	 * @param requestCycle the original request cycle
	 * @param path the original request path
	 * @param url the URL of the request to build
	 * @return the request
	 */
	protected HttpUriRequest buildPostRequest(RequestCycle requestCycle, RequestPathChain path, String url) throws Exception {
		HttpServletRequest originalRequest = requestCycle.getRequest();
		String requestBody = IOUtils.toString(originalRequest.getInputStream());
		HttpPost subRequest = new HttpPost(url);
		subRequest.setEntity(new StringEntity(requestBody, originalRequest.getContentType(), "utf-8"));
		logger.debug("request body:\n" + requestBody);
		return subRequest;
	}
	
	/**
	 * Consumes timing information for the delegate request.
	 * @param requestCycle the original request cycle
	 * @param path the original request path
	 * @param delegateUrl the URL of the delegate request
	 * @param delegateRequest the delegate request
	 * @param delegateResponse the delegate response
	 * @param deltaTime the number of nanoseconds the delegate request took to execute
	 */
	protected void consumeTiming(RequestCycle requestCycle, RequestPathChain path, String delegateUrl, HttpUriRequest delegateRequest, HttpResponse delegateResponse, long deltaTime) throws Exception {
	}

	/**
	 * Copies headers from the delegate response to the original response.
	 * The default implementation copies the status code, "Content-Type",
	 * character encoding, "Date" and "Vary".
	 * 
	 * @param requestCycle the original request cycle
	 * @param path the original request path
	 * @param delegateUrl the URL of the delegate request
	 * @param delegateRequest the delegate request
	 * @param delegateResponse the delegate response
	 */
	protected void copyResponseHeaders(RequestCycle requestCycle, RequestPathChain path, String delegateUrl, HttpUriRequest delegateRequest, HttpResponse delegateResponse) throws Exception {
		HttpServletResponse originalResponse = requestCycle.getResponse();
		originalResponse.setStatus(delegateResponse.getStatusLine().getStatusCode());
		originalResponse.setCharacterEncoding("utf-8");
		originalResponse.setContentType(delegateResponse.getEntity().getContentType().getValue());
		for (Header header : delegateResponse.getAllHeaders()) {
			String name = header.getName();
			if (name.equals("Date") || name.equals("Set-Cookie") || name.equals("Vary")) {
				originalResponse.setHeader(header.getName(), header.getValue());
			}
		}
	}

}
