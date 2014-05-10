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
 * This filter works against "jsessionid" session identifiers. It does two things:
 * - prevent insertion of such an identifier into URLs
 * - session invalidation for requests to URLs that contain such an identifier.
 * 
 * See http://randomcoder.org/articles/jsessionid-considered-harmful for detailed information.
 * Session identifiers in URLs cause multiple problems:
 * - missing calls to encodeUrl() cause session loss
 * - search engines choke on such URLs
 * - security: An attacker can send a victim an URL with jsessionid and have the victim's session
 *   hijacked from the beginning
 */
public class AntiJsessionidUrlFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
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

		// just in case we get a non-HTTP request/response, just pass it on
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			filterChain.doFilter(request, response);
			return;
		}

		// cast request / response
		final HttpServletRequest httpRequest = (HttpServletRequest)request;
		final HttpServletResponse httpResponse = (HttpServletResponse)response;

		// block requests with a jsessionid -- they should not appear since we disable them in Jetty
		if (httpRequest.isRequestedSessionIdFromURL()) {
			httpResponse.setStatus(400);
			return;
		}

		// with that response wrapper, go on handling the request
		filterChain.doFilter(request, response);

	}

}
