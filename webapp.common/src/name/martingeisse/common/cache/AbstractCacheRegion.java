/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache;

import java.io.Serializable;
import java.util.NoSuchElementException;

import name.martingeisse.common.util.IllegalReturnValueException;
import name.martingeisse.common.util.ParameterUtil;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

/**
 * Base implementation for {@link ICacheRegion} based on JCS.
 * 
 * @param <K> the type of cache keys
 * @param <V> the type of cached values
 */
public abstract class AbstractCacheRegion<K extends Serializable, V> implements ICacheRegion<K, V> {

	/**
	 * the cache
	 */
	private final JCS cache;

	/**
	 * Constructor.
	 * @param regionName the name of the region represented by this object
	 */
	public AbstractCacheRegion(final String regionName) {
		ParameterUtil.ensureNotNull(regionName, "regionName");
		try {
			this.cache = JCS.getInstance(regionName);
		} catch (final CacheException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.ICacheRegion#isCached(java.lang.String)
	 */
	@Override
	public boolean isCached(final K key) {
		ParameterUtil.ensureNotNull(key, "key");
		return (cache.get(key) != null);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.ICacheRegion#getCachedValue(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public V getCachedValue(final K key) {
		ParameterUtil.ensureNotNull(key, "key");
		Object value = cache.get(key);
		return (value == CachedNull.INSTANCE ? null : (V)value);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.ICacheRegion#getInternalValue(java.io.Serializable)
	 */
	@Override
	public Object getInternalValue(K key) {
		ParameterUtil.ensureNotNull(key, "key");
		return cache.get(key);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.ICacheRegion#setCachedValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setCachedValue(final K key, final V value) {
		ParameterUtil.ensureNotNull(key, "key");
		if (value == CachedNull.INSTANCE) {
			throw new IllegalArgumentException("pass null (not CachedNull.INSTANCE) to store a null value in the cache");
		}
		try {
			cache.put(key, value == null ? CachedNull.INSTANCE : value);
		} catch (final CacheException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.ICacheRegion#get(java.io.Serializable)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public V get(K key) throws UnsupportedOperationException, NoSuchElementException {
		ParameterUtil.ensureNotNull(key, "key");
		Object internalValue = cache.get(key);
		if (internalValue == CachedNull.INSTANCE) {
			return null;
		} else if (internalValue != null) {
			return (V)internalValue;
		} else {
			V value = fetch(key);
			if (value == CachedNull.INSTANCE) {
				throw new IllegalReturnValueException("cache fetch() implementations should return null (not CachedNull.INSTANCE) to store a null value in the cache");
			}
			setCachedValue(key, value);
			return value;
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.ICacheRegion#remove(java.lang.String)
	 */
	@Override
	public void remove(final K key) {
		ParameterUtil.ensureNotNull(key, "key");
		try {
			cache.remove(key);
		} catch (final CacheException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.ICacheRegion#clear()
	 */
	@Override
	public void clear() {
		try {
			cache.clear();
		} catch (final CacheException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method actually fetches a missing value for the {@link #get(Serializable)} method.
	 * 
	 * @param key the key (never null)
	 * @return the value (never null)
	 * @throws UnsupportedOperationException if the value is missing and this
	 * implementation does not know how to fetch values
	 * @throws NoSuchElementException if the value is missing and this implementation
	 * tried to fetch it, but there is no value that matches the key
	 */
	protected abstract V fetch(K key) throws UnsupportedOperationException, NoSuchElementException;
	
}
