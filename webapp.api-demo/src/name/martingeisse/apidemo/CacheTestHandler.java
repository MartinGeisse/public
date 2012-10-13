/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo;

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;

import org.apache.jcs.JCS;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;

/**
 * Apache commons cache test.
 */
public class CacheTestHandler implements IRequestHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(final RequestCycle requestCycle, final RequestPathChain path) throws Exception {
		final String headSegment = path.getHead();
		final String key = (headSegment == null ? "default" : headSegment);
		final JCS cache = JCS.getInstance("myregion");

		String value = (String)cache.get(key);
		if (value == null) {
			value = DateTimeFormat.fullDateTime().print(new Instant());
			cache.put(key, value);
		}
		
		requestCycle.preparePlainTextResponse();
		requestCycle.getWriter().println(key + ": " + value);
	}

}
