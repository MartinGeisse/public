/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.process;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import name.martingeisse.webide.ipc.IpcEvent;

import org.json.simple.JSONValue;

/**
 * A servlet filter that consumes messages from companion
 * processes.
 */
public class CompanionProcessMessageFilter implements Filter {
	
	/**
	 * the URI_PREFIX
	 */
	private static final String URI_PREFIX = "/_companion_process/";
	
	/**
	 * Returns the base URL used to send messages to the IDE process.
	 * The actual URL is obtained by appending the event type.
	 * 
	 * @param companionId the companion ID of the subprocess
	 * @return the base URL
	 */
	static String getMessageBaseUrl(long companionId) {
		return "http://localhost:8080" + URI_PREFIX + companionId + '/';
	}
	
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			// TODO: this must not accept messages from untrusted sources!
			if (httpRequest.getRequestURI().startsWith(URI_PREFIX)) {
				String remainder = httpRequest.getRequestURI().substring(URI_PREFIX.length());
				int slashIndex = remainder.indexOf('/');
				if (slashIndex > 0) {
					long companionId;
					try {
						companionId = Long.parseLong(remainder.substring(0, slashIndex));
					} catch (NumberFormatException e) {
						companionId = -1;
					}
					CompanionProcess process = CompanionProcess.getRunningProcess(companionId);
					if (process != null) {
						String eventType = remainder.substring(slashIndex + 1);
						Object eventData = JSONValue.parse(request.getReader());
						process.onEventReceived(new IpcEvent(eventType, null, eventData));
					}
				}
			}
		}
		chain.doFilter(request, response);
	}
	
}
