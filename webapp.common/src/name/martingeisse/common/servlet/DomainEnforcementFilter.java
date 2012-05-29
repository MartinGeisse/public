/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter accepts only requests to a specific domain -- configured via a filter parameter --
 * and redirects requests with any other server name to the correct one with a
 * 301 MOVED PERMANENTLY response.
 */
public class DomainEnforcementFilter implements Filter {

	/**
	 * the domain
	 */
	private String domain;
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		domain = "vshg01.mni.fh-giessen.de";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {

		// handle requests to the correct domain
		final String serverName = request.getServerName();
		if (serverName != null && (serverName.equalsIgnoreCase(domain) || serverName.equalsIgnoreCase("localhost"))) {
			filterChain.doFilter(request, response);
			return;
		} 
		
		// just in case we get a non-HTTP request/response, just pass it on
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			filterChain.doFilter(request, response);
			return;
		}

		// cast request / response
		final HttpServletRequest httpRequest = (HttpServletRequest)request;
		final HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		// send a 301 MOVED PERMANENTLY
		httpResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		httpResponse.setHeader("Location", getModifiedUrl(httpRequest));
		httpResponse.setHeader("Connection", "close");

	}
	
	/**
	 * @param request
	 * @return
	 */
	private String getModifiedUrl(HttpServletRequest request) {
		String baseUrl = request.getScheme() + "://" + domain + request.getRequestURI();
		String queryString = request.getQueryString();
		return (queryString == null) ? baseUrl : (baseUrl + '?' + queryString);
	}

}
