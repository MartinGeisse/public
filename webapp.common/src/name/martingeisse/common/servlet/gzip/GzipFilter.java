/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.servlet.gzip;

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
 * GZIP-compressing and caching servlet filter.
 */
public class GzipFilter implements Filter {

	/**
	 * the cache
	 */
	private CannedResponseCache cache;
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		this.cache = new CannedResponseCache();
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
		if (!handleWithGzip(request, response, filterChain)) {
			filterChain.doFilter(request, response);
		}
	}
	      
	private boolean handleWithGzip(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		
		// HTTP only
		if (!(request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
			return false;
		}
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		/* Dismiss uncompressible file types. GZIPing them isn't disallowed, but would not
		 * decrease file size significantly (or might actually increase size).
		 */
		String lowercaseUri = ((HttpServletRequest)request).getRequestURI().toLowerCase();
		if (lowercaseUri.endsWith(".jpg") || lowercaseUri.endsWith(".png") || lowercaseUri.endsWith(".gif")) {
			return false;
		}

		/* If we got here, then the requested page or resource is compressible. That means it now depends on
		 * the client's capabilities -- expressed in the "Accept-Encoding" header -- whether we can serve it
		 * compressed. To make proxy servers properly cache both the compressed and uncompressed version, we
		 * must use the "Vary: Accept-Encoding" header.
		 * 
		 * Unfortunately this may(*) cause Internet Explorer to not cache the response at all if it is
		 * not compressed. I still prefer that behavior to the alternative though, which would be to send
		 * "Vary: AE" only for compressed responses. Doing so could and does cause proxy server software to
		 * take the uncompressed version as the only one in existence, and never return the compressed version
		 * again.
		 * 
		 * (*) Information on the internet is unclear about this.
		 */
		httpResponse.addHeader("Vary", "Accept-Encoding");

		// check if the client supports GZIP
		String acceptEncodingHeader = httpRequest.getHeader("Accept-Encoding");
		if (acceptEncodingHeader == null || acceptEncodingHeader.indexOf("gzip") == -1) {
			return false;
		}
		
		// maybe we find something in the cache
		CannedResponseCacheKey cacheKey = new CannedResponseCacheKey(getRelativeUrl(httpRequest));
		CannedResponse cacheValue = cache.get(cacheKey);
		if (cacheValue != null) {
			cacheValue.respond(httpRequest, httpResponse);
			return true;
		}

		// wrap the response to enable GZIP
		GzipResponseWrapper responseWrapper = new GzipResponseWrapper(httpResponse);
		filterChain.doFilter(request, responseWrapper);
		responseWrapper.finishResponse();
		
		// possibly create a canned response and store it in the cache
		if (responseWrapper.isCacheable()) {
			cacheValue = responseWrapper.createCannedResponse();
			cache.put(cacheKey, cacheValue);
		}
		
		return true;

	}

	/**
	 * @param request the request
	 * @return the relative request URL
	 */
	private String getRelativeUrl(HttpServletRequest request) {
		String result = request.getRequestURI();
		String query = request.getQueryString();
		if (query == null) {
			return result;
		} else {
			return result + '?' + query;
		}
	}
	
}
