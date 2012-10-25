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
import name.martingeisse.common.cache.querydsl.RowLoader;
import name.martingeisse.common.util.Wrapper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Apache commons cache test.
 */
public class MultiKeyCacheTestHandler implements IRequestHandler {

	/**
	 * the settingsLoader
	 */
	private static final CacheLoader<String, Wrapper<PhorumSettings>> unprefixedSettingsLoader = new RowLoader<String, PhorumSettings>(QPhorumSettings.phorumSettings, QPhorumSettings.phorumSettings.name) {
		@Override
		protected Wrapper<PhorumSettings> transformValue(String key, PhorumSettings row) {
			Wrapper<PhorumSettings> result = super.transformValue(key, row);
			if (result.getValue() != null) {
				prefixedSettingsCache.put("+" + key, result);
			}
			return result;
		}
	};
	
	/**
	 * the settingsCache2
	 */
	private static final LoadingCache<String, Wrapper<PhorumSettings>> unprefixedSettingsCache = CacheBuilder.newBuilder().build(unprefixedSettingsLoader);

	/**
	 * the settingsLoader
	 */
	private static final CacheLoader<String, Wrapper<PhorumSettings>> prefixedSettingsLoader = new RowLoader<String, PhorumSettings>(QPhorumSettings.phorumSettings, QPhorumSettings.phorumSettings.name.prepend("+")) {
		@Override
		protected Wrapper<PhorumSettings> transformValue(String key, PhorumSettings row) {
			Wrapper<PhorumSettings> result = super.transformValue(key, row);
			if (result.getValue() != null) {
				unprefixedSettingsCache.put(key.substring(1), result);
			}
			return result;
		}
	};
	
	/**
	 * the settingsCache2
	 */
	private static final LoadingCache<String, Wrapper<PhorumSettings>> prefixedSettingsCache = CacheBuilder.newBuilder().build(prefixedSettingsLoader);
	
	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(final RequestCycle requestCycle, final RequestPathChain path) throws Exception {
		
		final String headSegment = path.getHead();
		final String key = (headSegment == null ? "default" : headSegment);
		final boolean prefixed = (requestCycle.getParameters().getBooleanOrDefault("prefixed", false));
		
		final LoadingCache<String, Wrapper<PhorumSettings>> cache = (prefixed ? prefixedSettingsCache : unprefixedSettingsCache);
		final PhorumSettings value = cache.get(key).getValue();
		requestCycle.preparePlainTextResponse();
		if (value == null) {
			requestCycle.getWriter().println(key + " -> null");
		} else {
			requestCycle.getWriter().println(key + " -> (" + value.getName() + ", " + value.getData() + ", " + value.getType() + ", " + value.getGroupId() + ")");
		}
	}

}
