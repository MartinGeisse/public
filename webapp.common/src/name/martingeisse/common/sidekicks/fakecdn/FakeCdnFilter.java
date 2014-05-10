/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sidekicks.fakecdn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This filter implements the fake CDN web interface.
 */
public final class FakeCdnFilter implements Filter {

	/**
	 * the URI_PREFIX
	 */
	public static final String URI_PREFIX = "/__cdn/";
	
	/**
	 * the cdn
	 */
	private final FakeCdn cdn = new FakeCdn();
	
	/**
	 * the dateTimeHeaderFormatter
	 */
	private final DateTimeFormatter dateTimeHeaderFormatter = DateTimeFormat.forPattern("E, dd MMM yyyy HH:mm:ss 'GMT'");

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
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
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String uri = httpRequest.getRequestURI();
		if (uri.startsWith(URI_PREFIX)) {
			String key = uri.substring(URI_PREFIX.length() - 1);
			doCdn(httpRequest, response, key);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	/**
	 * 
	 */
	private void doCdn(ServletRequest request, ServletResponse response, String key) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		FakeCdnRecord record = cdn.request(key);
		httpResponse.setStatus(record.getStatusCode());
		httpResponse.setContentType(record.getContentType());
		httpResponse.setHeader("Cache-Control", "" + (365 * 24 * 3600));
		httpResponse.setHeader("Date", dateTimeHeaderFormatter.print(new DateTime()));
		httpResponse.setHeader("Expires", dateTimeHeaderFormatter.print(new DateTime().plusDays(365)));
		httpResponse.setHeader("Last-Modified", dateTimeHeaderFormatter.print(new DateTime(2000, 1, 1, 0, 0)));
		IOUtils.copy(new ByteArrayInputStream(record.getData()), httpResponse.getOutputStream());
		httpResponse.getOutputStream().flush();
		httpResponse.getOutputStream().close();
	}
	
}
