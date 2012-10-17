/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache.querydsl;


/*
 * Cache implementation for a row window, transformed cache.
 * Each cache value is a list of values, generated from the list of
 * rows for that key. This cache supports limit/offset based windows
 * into the cache value lists.
 * 
 * This cache also supports fetching cache value lists in a block-granular
 * way, instead of fetching and caching all rows for a cache key at once,
 * to improve performance.
 * 
 * @param <K> the type of cache keys
 * @param <R> the table row bean type
 * @param <V> the type of cached values
 */
