/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo;

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;
import name.martingeisse.apidemo.phorum.PhorumSettings;
import name.martingeisse.apidemo.phorum.QPhorumSettings;
import name.martingeisse.common.cache.ICacheRegion;
import name.martingeisse.common.cache.querydsl.RowRegion;

/**
 * Apache commons cache test.
 */
public class QueryCacheTestHandler implements IRequestHandler {

	/**
	 * the settingsCache
	 */
	private static final ICacheRegion<String, PhorumSettings> settingsCache = new RowRegion<String, PhorumSettings>("table.settings", QPhorumSettings.phorumSettings, QPhorumSettings.phorumSettings.name);
	
	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(final RequestCycle requestCycle, final RequestPathChain path) throws Exception {
		final String headSegment = path.getHead();
		final String key = (headSegment == null ? "default" : headSegment);
		final PhorumSettings value = settingsCache.get(key);
		requestCycle.preparePlainTextResponse();
		if (value == null) {
			requestCycle.getWriter().println(key + " -> null");
		} else {
			requestCycle.getWriter().println(key + " -> (" + value.getName() + ", " + value.getData() + ", " + value.getType() + ", " + value.getGroupId() + ")");
		}
	}

}
