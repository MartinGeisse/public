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
 * other values. Null keys are not allowed. Since cache
 * implementations might disallow null values, this class
 * internally stores {@link CachedNull#INSTANCE} to represent
 * null values. This value is not typically visible to application
 * code, though.
 * 
 * Note that regions are identified by name, not by identity of
 * the instance of this interface. If two such instances use the
 * same name, they access the same region.
 * 
 * Instances are often immutable singleton objects that are shared
 * by all threads, but this is not a requirement.
 * 
 * @param <K> the type of cache keys
 * @param <V> the type of cached values
 */
public interface ICacheRegion<K extends Serializable, V> {

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
	
}
