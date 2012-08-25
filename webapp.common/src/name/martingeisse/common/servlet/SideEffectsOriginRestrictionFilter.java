/**
 * Copyright (c) 2011 Martin Geisse
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
 * Prevents POST, PUT, DELETE requests from other domains to protect
 * against CSRF attacks.
 * 
 * TODO: log CSRF requests! This may lead us to the attacker.
 */
public class SideEffectsOriginRestrictionFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		// just in case we get a non-HTTP request/response, just pass it on
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			filterChain.doFilter(request, response);
			return;
		}

		// cast request / response
		final HttpServletRequest httpRequest = (HttpServletRequest)request;
		final HttpServletResponse httpResponse = (HttpServletResponse)response;

		// if the method indicates side effects, then the referrer must be our domain. Note that this filter
		// prevents CSRF, not other forged requests, so we can rely on the "Host" and "Referer" fields.
		String method = httpRequest.getMethod();
		if (method.equalsIgnoreCase("post") || method.equalsIgnoreCase("put") || method.equalsIgnoreCase("delete")) {
			
			// an empty referrer may come from a downloaded HTML file -- protect against these
			String referrer = httpRequest.getHeader("Referer");
			if (referrer == null) {
				httpResponse.setStatus(400);
				return;
			}

			// an empty host header field means we cannot check the referrer
			String host = httpRequest.getHeader("Host");
			if (host == null) {
				httpResponse.setStatus(400);
				return;
			}
			
			// the referrer must be our domain
			if (!host.equals(extractRefererDomain(referrer))) {
				httpResponse.setStatus(400);
				return;
			}
			
		}

		// with that response wrapper, go on handling the request
		filterChain.doFilter(request, response);
		
	}
	
	/**
	 * Removes the URL scheme.
	 */
	private String extractRefererDomain(String url) {
		int index1 = url.indexOf("://");
		url = (index1 == -1 ? url : url.substring(index1 + 3));
		int index2 = url.indexOf('/');
		url = (index2 == -1 ? url : url.substring(0, index2));
		return url;
	}
	
}
