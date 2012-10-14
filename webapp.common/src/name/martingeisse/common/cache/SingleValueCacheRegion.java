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
	 * Checks whether a value is currently cached.
	 * @return true if cached, false if not
	 */
	public boolean isCached() {
		return region.isCached(key);
	}
	
	/**
	 * Returns the cached value, or null if no value is currently cached.
	 * Use {@link #isCached()} or {@link #getInternalValue()} to distinguish
	 * a cached null value from a missing value.
	 * 
	 * @return the cached value or null
	 */
	public V getCachedValue() {
		return region.getCachedValue(key);
	}
	
	/**
	 * This method is similar to {@link #getCachedValue()}, except how if
	 * treats null values: This method returns null if and only if no value
	 * is stored in the cache. If a null value is stored in the cache, this
	 * method returns {@link CachedNull#INSTANCE}.
	 * 
	 * @return the cached value or {@link CachedNull#INSTANCE}
	 */
	public Object getInternalValue() {
		return region.getInternalValue(key);
	}
	
	/**
	 * Sets the cached value.
	 * @param value the value. Pass null to store a null value in the
	 * cache; passing {@link CachedNull#INSTANCE} is not allowed.
	 */
	public void setCachedValue(V value) {
		region.setCachedValue(key, value);
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

	/**
	 * Removes the cached value.
	 */
	public void remove() {
		region.remove(key);
	}
	
}
