/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.application;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.http.WebResponse;

/**
 * Intercepts requests for the specified "from" URL and produces a 301
 * MOVED PERMANENTLY to the "to" URL.
 */
public class PermanentRedirectMapper implements IRequestMapper {

	/**
	 * the from
	 */
	private final String from;

	/**
	 * the to
	 */
	private final String to;

	/**
	 * Constructor.
	 * @param from the relative URL from the root, e.g. "from/this/page"
	 * @param to the absolute URL to redirect to, e.g. "/toThis"
	 */
	public PermanentRedirectMapper(final String from, final String to) {
		this.from = from;
		this.to = to;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestMapper#mapRequest(org.apache.wicket.request.Request)
	 */
	@Override
	public IRequestHandler mapRequest(Request request) {
		if (request.getUrl().getPath().equals(from)) {
			return new MyRequestHandler();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestMapper#getCompatibilityScore(org.apache.wicket.request.Request)
	 */
	@Override
	public int getCompatibilityScore(Request request) {
		return Integer.MAX_VALUE;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestMapper#mapHandler(org.apache.wicket.request.IRequestHandler)
	 */
	@Override
	public Url mapHandler(IRequestHandler requestHandler) {
		return null;
	}

	/**
	 * Custom implementation for {@link IRequestHandler} that outputs the redirect.
	 */
	private class MyRequestHandler implements IRequestHandler {

		/* (non-Javadoc)
		 * @see org.apache.wicket.request.IRequestHandler#respond(org.apache.wicket.request.IRequestCycle)
		 */
		@Override
		public void respond(IRequestCycle requestCycle) {
	        WebResponse response = (WebResponse) requestCycle.getResponse();
	        HttpServletResponse servletResponse = (HttpServletResponse)response.getContainerResponse();
	        servletResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
	        servletResponse.setHeader("Location", to);
	        servletResponse.setHeader("Connection", "close");
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.request.IRequestHandler#detach(org.apache.wicket.request.IRequestCycle)
		 */
		@Override
		public void detach(IRequestCycle requestCycle) {
		}

	}
	
}
