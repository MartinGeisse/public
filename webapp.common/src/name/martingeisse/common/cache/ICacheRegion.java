/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache;

import java.io.Serializable;
import java.util.List;

/**
 * This interface is intended for singleton objects that provide
 * access to cache regions. Each cache region associates keys
 * with cached values.
 * 
 * Null values are explicitly allowed and are cached just like
 * other values. The {@link #isCached(Serializable)} method allows
 * to distinguish a cached null value from a missing value.
 * Null keys are not allowed. Since cache implementations might
 * disallow null values, this class internally stores
 * {@link CachedNull#INSTANCE} to represent null values. This value
 * is not typically visible to application code, though.
 * 
 * Note that regions are identified by name, not by identity of
 * the instance of this interface. If two such instances use the
 * same name, they access the same region.
 * 
 * @param <K> the type of cache keys
 * @param <V> the type of cached values
 */
public interface ICacheRegion<K extends Serializable, V> {
	
	/**
	 * Checks whether a value with the specified key is currently cached.
	 * Returns true if a null value is cached.
	 * 
	 * @param key the key
	 * @return true if cached, false if not
	 */
	public boolean isCached(K key);
	
	/**
	 * Returns the value cached for the specified key, or null if no
	 * such value is currently cached. Use {@link #isCached(Serializable)}
	 * or {@link #getInternalValue(Serializable)} to distinguish a cached
	 * null value from a missing value.
	 * 
	 * @param key the key (must not be null)
	 * @return the cached value or null
	 */
	public V getCachedValue(K key);
	
	/**
	 * This method is equivalent to calling {@link #getCachedValue(Serializable)}
	 * for each of the specified keys. It returns a list containing the cached values.
	 * @param keys the keys (neither the iterable nor any key may be null)
	 * @return the values (null for each key whose value is missing or is
	 * actually null)
	 */
	public List<V> getCachedValues(Iterable<K> keys);

	/**
	 * This method is similar to {@link #getCachedValue(Serializable)},
	 * except how if treats null values: This method returns null if
	 * and only if no value is stored in the cache. If a null value
	 * is stored in the cache, this method returns {@link CachedNull#INSTANCE}.
	 * 
	 * @param key the key (must not be null)
	 * @return the cached value or {@link CachedNull#INSTANCE}
	 */
	public Object getInternalValue(K key);
	
	/**
	 * This method is equivalent to calling {@link #getInternalValue(Serializable)}
	 * for each of the specified keys. It returns a list containing the internal values.
	 * @param keys the keys (neither the iterable nor any key may be null)
	 * @return the values (null for each key whose value is missing;
	 * {@link CachedNull#INSTANCE} for each key whose value is actually null)
	 */
	public List<Object> getInternalValues(Iterable<K> keys);
	
	/**
	 * Sets the value cached for the specified key.
	 * 
	 * @param key the key (must not be null)
	 * @param value the value. Pass null to store a null value in the
	 * cache; passing {@link CachedNull#INSTANCE} is not allowed.
	 */
	public void setCachedValue(K key, V value);

	/**
	 * This method returns the value for the specified key, fetching it
	 * if not yet cached. This requires that the implementation knows
	 * how to fetch missing values. Cached values are returned directly,
	 * whether the implementation knows how to re-fetch them or not.
	 * 
	 * @param key the key (must not be null)
	 * @return the value. Returns null if and only if the cached value is
	 * actually null.
	 * @throws UnsupportedOperationException if the value is missing and this
	 * implementation does not know how to fetch values
	 */
	public V get(K key) throws UnsupportedOperationException;

	/**
	 * This method is equivalent to calling {@link #get(Serializable)}
	 * for each of the specified keys. It returns a list containing the
	 * values.
	 * 
	 * Note that, if more than one value is not currently cached, this
	 * method attempts to fetch multiple values at once. This makes
	 * typical implementations much more efficient than actually calling
	 * {@link #get(Iterable)} for each key.
	 * 
	 * @param keys the keys (neither the iterable nor any key may be null)
	 * @return the values (contains null if and only if the cached value is
	 * actually null).
	 * 
	 * @throws UnsupportedOperationException if any value is missing and this
	 * implementation does not know how to fetch values
	 */
	public List<V> get(Iterable<K> keys) throws UnsupportedOperationException;

	/**
	 * Removes the cached value for the specified key.
	 * 
	 * @param key the key (must not be null)
	 */
	public void remove(K key);

	/**
	 * Removes the cached values for the specified keys.
	 * 
	 * @param keys the keys (neither the iterable nor any key may be null)
	 */
	public void remove(Iterable<K> keys);
	
	/**
	 * Removes all cached values.
	 */
	public void clear();
	
}
