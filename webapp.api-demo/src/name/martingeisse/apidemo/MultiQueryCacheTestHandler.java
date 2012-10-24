/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;
import name.martingeisse.apidemo.phorum.PhorumSettings;
import name.martingeisse.apidemo.phorum.QPhorumSettings;
import name.martingeisse.common.cache.querydsl.RowLoader;
import name.martingeisse.common.util.Wrapper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Apache commons cache test.
 */
public class MultiQueryCacheTestHandler implements IRequestHandler {

	/**
	 * the settingsLoader
	 */
	private static final CacheLoader<String, Wrapper<PhorumSettings>> settingsLoader = new RowLoader<String, PhorumSettings>(QPhorumSettings.phorumSettings, QPhorumSettings.phorumSettings.name);
	
	/**
	 * the settingsCache2
	 */
	private static final LoadingCache<String, Wrapper<PhorumSettings>> settingsCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).build(settingsLoader);
	
	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(final RequestCycle requestCycle, final RequestPathChain path) throws Exception {
		final String headSegment = path.getHead();
		final String keyText = (headSegment == null ? "default" : headSegment);
		final List<String> keys = Arrays.asList(keyText.split("\\,"));
		final Map<String, Wrapper<PhorumSettings>> values = settingsCache.getAll(keys);
		requestCycle.preparePlainTextResponse();
		requestCycle.getWriter().println("keys: " + keyText);
		requestCycle.getWriter().println();
		for (Wrapper<PhorumSettings> valueWrapper : values.values()) {
			PhorumSettings value = valueWrapper.getValue();
			if (value == null) {
				requestCycle.getWriter().println("null");
			} else {
				requestCycle.getWriter().println("(" + value.getName() + ", " + value.getData() + ", " + value.getType() + ", " + value.getGroupId() + ")");
			}
		}
	}

}
