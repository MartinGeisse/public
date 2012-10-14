/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import name.martingeisse.common.util.IllegalReturnValueException;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.ReturnValueUtil;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

/**
 * Base implementation for {@link ICacheRegion} based on JCS.
 * 
 * This class provides a default implementation for the fetchMultiple()
 * method that actually invokes the single-value fetch() method for each key.
 * Subclasses are encouraged to provide for efficient implementations.
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
	 * @see name.martingeisse.common.cache.ICacheRegion#getCachedValues(java.lang.Iterable)
	 */
	@Override
	public List<V> getCachedValues(Iterable<K> keys) {
		ParameterUtil.ensureNotNull(keys, "keys");
		List<V> values = new ArrayList<V>();
		for (K key : keys) {
			values.add(getCachedValue(key));
		}
		return values;
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
	 * @see name.martingeisse.common.cache.ICacheRegion#getInternalValues(java.lang.Iterable)
	 */
	@Override
	public List<Object> getInternalValues(Iterable<K> keys) {
		ParameterUtil.ensureNotNull(keys, "keys");
		List<Object> values = new ArrayList<Object>();
		for (K key : keys) {
			values.add(getInternalValue(key));
		}
		return values;
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
	 * @see name.martingeisse.common.cache.ICacheRegion#get(java.lang.Iterable)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<V> get(Iterable<K> keys) throws UnsupportedOperationException {

		// scan the cache for the specified keys
		List<Object> internalValues = new ArrayList<Object>();
		List<K> missingKeys = new ArrayList<K>();
		for (K key : keys) {
			ParameterUtil.ensureNotNull(key, "key");
			Object internalValue = cache.get(key);
			internalValues.add(internalValue);
			if (internalValue == null) {
				missingKeys.add(key);
			}
		}
		
		// fetch missing values and store them in the cache
		List<V> fetchedValues;
		if (missingKeys.isEmpty()) {
			fetchedValues = new ArrayList<V>();
		} else {
			fetchedValues = ReturnValueUtil.nullNotAllowed(fetchMultiple(missingKeys), "fetchMultiple()");
			if (fetchedValues.size() != missingKeys.size()) {
				throw new IllegalReturnValueException("fetchMultiple() returned " + fetchedValues.size() + " values for " + missingKeys.size() + " keys");
			}
			{
				Iterator<V> fetchedValueIterator = fetchedValues.iterator();
				for (K key : missingKeys) {
					setCachedValue(key, fetchedValueIterator.next());
				}
			}
		}
		
		// merge cached and fetched values
		List<V> result = new ArrayList<V>();
		Iterator<V> fetchedValueIterator = fetchedValues.iterator();
		for (Object internalValue : internalValues) {
			if (internalValue == null) {
				result.add(fetchedValueIterator.next());
			} else if (internalValue == CachedNull.INSTANCE) {
				result.add(null);
			} else {
				result.add((V)internalValue);
			}
		}
		return result;
		
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
	 * @see name.martingeisse.common.cache.ICacheRegion#remove(java.lang.Iterable)
	 */
	@Override
	public void remove(Iterable<K> keys) {
		ParameterUtil.ensureNotNull(keys, "keys");
		for (K key : keys) {
			remove(key);
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
	 * @return the value
	 * @throws UnsupportedOperationException if the value is missing and this
	 * implementation does not know how to fetch values
	 */
	protected abstract V fetch(K key) throws UnsupportedOperationException;

	/**
	 * This method actually fetches missing values for the {@link #get(Iterable)} method.
	 * 
	 * This default implementation invokes fetch() for each key. Subclasses are encouraged to
	 * provide for efficient implementations.
	 * 
	 * @param keys the keys (neither the iterable nor any key may be null)
	 * @return the values
	 * @throws UnsupportedOperationException if the value is missing and this
	 * implementation does not know how to fetch values
	 */
	protected List<V> fetchMultiple(List<K> keys) throws UnsupportedOperationException {
		ParameterUtil.ensureNotNull(keys, "keys");
		List<V> values = new ArrayList<V>();
		for (K key : keys) {
			values.add(fetch(key));
		}
		return values;
	}
	
}
