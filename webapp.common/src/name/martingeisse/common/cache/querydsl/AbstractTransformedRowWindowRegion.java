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
 * This cache also supports fetching and caching value lists in a
 * block-granular way, instead of fetching and caching all rows for a
 * cache key at once, to improve performance. When a window is requested
 * by calling code, then all blocks that contribute to this window are
 * fetched (and first checked if they are cached), leaving the other
 * blocks alone.
 * 
 * Note, however, that since this may require fetching a non-contiguous
 * range of blocks, a request for a large window may result in multiple
 * queries to the database. The block size should be chosen to be
 * larger than the typical window size to avoid this problem.
 * 
 * @param <K> the type of cache keys
 * @param <R> the table row bean type
 * @param <V> the type of cached values
 */
