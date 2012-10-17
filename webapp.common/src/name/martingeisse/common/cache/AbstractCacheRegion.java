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

	/**
	 * Checks whether a value with the specified key is currently cached.
	 * Returns true if a null value is cached.
	 * 
	 * @param key the key
	 * @return true if cached, false if not
	 */
	public boolean isCached(final K key) {
		ParameterUtil.ensureNotNull(key, "key");
		return (cache.get(key) != null);
	}

	/**
	 * Returns the value cached for the specified key, or null if no
	 * such value is currently cached. Use {@link #isCached(Serializable)}
	 * or {@link #getInternalValue(Serializable)} to distinguish a cached
	 * null value from a missing value.
	 * 
	 * @param key the key (must not be null)
	 * @return the cached value or null
	 */
	@SuppressWarnings("unchecked")
	public V getCachedValue(final K key) {
		ParameterUtil.ensureNotNull(key, "key");
		final Object value = cache.get(key);
		return (value == CachedNull.INSTANCE ? null : (V)value);
	}

	/**
	 * This method is equivalent to calling {@link #getCachedValue(Serializable)}
	 * for each of the specified keys. It returns a list containing the cached values.
	 * @param keys the keys (neither the iterable nor any key may be null)
	 * @return the values (null for each key whose value is missing or is
	 * actually null)
	 */
	public List<V> getCachedValues(final Iterable<K> keys) {
		ParameterUtil.ensureNotNull(keys, "keys");
		final List<V> values = new ArrayList<V>();
		for (final K key : keys) {
			values.add(getCachedValue(key));
		}
		return values;
	}

	/**
	 * This method is similar to {@link #getCachedValue(Serializable)},
	 * except how if treats null values: This method returns null if
	 * and only if no value is stored in the cache. If a null value
	 * is stored in the cache, this method returns {@link CachedNull#INSTANCE}.
	 * 
	 * @param key the key (must not be null)
	 * @return the cached value or {@link CachedNull#INSTANCE}
	 */
	public Object getInternalValue(final K key) {
		ParameterUtil.ensureNotNull(key, "key");
		return cache.get(key);
	}

	/**
	 * This method is equivalent to calling {@link #getInternalValue(Serializable)}
	 * for each of the specified keys. It returns a list containing the internal values.
	 * @param keys the keys (neither the iterable nor any key may be null)
	 * @return the values (null for each key whose value is missing;
	 * {@link CachedNull#INSTANCE} for each key whose value is actually null)
	 */
	public List<Object> getInternalValues(final Iterable<K> keys) {
		ParameterUtil.ensureNotNull(keys, "keys");
		final List<Object> values = new ArrayList<Object>();
		for (final K key : keys) {
			values.add(getInternalValue(key));
		}
		return values;
	}

	/**
	 * Sets the value cached for the specified key.
	 * 
	 * @param key the key (must not be null)
	 * @param value the value. Pass null to store a null value in the
	 * cache; passing {@link CachedNull#INSTANCE} is not allowed.
	 */
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
	public V get(final K key) throws UnsupportedOperationException, NoSuchElementException {
		ParameterUtil.ensureNotNull(key, "key");
		final Object internalValue = cache.get(key);
		if (internalValue == CachedNull.INSTANCE) {
			return null;
		} else if (internalValue != null) {
			return (V)internalValue;
		} else {
			final V value = fetch(key);
			if (value == CachedNull.INSTANCE) {
				throw new IllegalReturnValueException(
					"cache fetch() implementations should return null (not CachedNull.INSTANCE) to store a null value in the cache");
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
	public List<V> get(final Iterable<K> keys) throws UnsupportedOperationException {

		// scan the cache for the specified keys
		final List<Object> internalValues = new ArrayList<Object>();
		final List<K> missingKeys = new ArrayList<K>();
		for (final K key : keys) {
			ParameterUtil.ensureNotNull(key, "key");
			final Object internalValue = cache.get(key);
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
				throw new IllegalReturnValueException("fetchMultiple() returned " + fetchedValues.size() + " values for "
					+ missingKeys.size() + " keys");
			}
			{
				final Iterator<V> fetchedValueIterator = fetchedValues.iterator();
				for (final K key : missingKeys) {
					setCachedValue(key, fetchedValueIterator.next());
				}
			}
		}

		// merge cached and fetched values
		final List<V> result = new ArrayList<V>();
		final Iterator<V> fetchedValueIterator = fetchedValues.iterator();
		for (final Object internalValue : internalValues) {
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

	/**
	 * Removes the cached value for the specified key.
	 * 
	 * @param key the key (must not be null)
	 */
	public void remove(final K key) {
		ParameterUtil.ensureNotNull(key, "key");
		try {
			cache.remove(key);
		} catch (final CacheException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Removes the cached values for the specified keys.
	 * 
	 * @param keys the keys (neither the iterable nor any key may be null)
	 */
	public void remove(final Iterable<K> keys) {
		ParameterUtil.ensureNotNull(keys, "keys");
		for (final K key : keys) {
			remove(key);
		}
	}

	/**
	 * Removes all cached values.
	 */
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
	protected List<V> fetchMultiple(final List<K> keys) throws UnsupportedOperationException {
		ParameterUtil.ensureNotNull(keys, "keys");
		final List<V> values = new ArrayList<V>();
		for (final K key : keys) {
			values.add(fetch(key));
		}
		return values;
	}

}
