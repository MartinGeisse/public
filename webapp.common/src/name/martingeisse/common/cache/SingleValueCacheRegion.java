/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache;

import java.io.Serializable;

/**
 * This class is a simple wrapper around an {@link ICacheRegion}
 * that provides key-less access methods and uses a single key
 * specified at construction in all methods. This is useful for
 * cache regions that only store a single value.
 * 
 * @param <K> the type of cache keys
 * @param <V> the type of cached values
 */
public final class SingleValueCacheRegion<K extends Serializable, V> {

	/**
	 * the region
	 */
	private final ICacheRegion<K, V> region;
	
	/**
	 * the key
	 */
	private final K key;
	
	/**
	 * Constructor.
	 * @param region the cache region
	 * @param key the key to use in all methods (must not be null)
	 */
	public SingleValueCacheRegion(final ICacheRegion<K, V> region, final K key) {
		this.region = region;
		this.key = key;
	}
	
	/**
	 * This method returns the value, fetching it if not yet cached.
	 * This requires that the implementation knows how to fetch missing
	 * values. Cached values are returned directly, whether the
	 * implementation knows how to re-fetch them or not.
	 * 
	 * @return the value. Returns null if and only if the cached value is
	 * actually null.
	 * @throws UnsupportedOperationException if the value is missing and this
	 * implementation does not know how to fetch values
	 */
	public V get() throws UnsupportedOperationException {
		return region.get(key);
	}
	
}
