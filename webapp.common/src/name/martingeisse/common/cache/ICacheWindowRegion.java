/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache;

import java.io.Serializable;
import java.util.List;

/**
 * This interface represents a cache region whose values are lists,
 * and provides limit/offset based "window" access to those lists.
 * Implementations may provide optimized access to such windows.
 * 
 * Using this cache via the normal {@link ICacheRegion} interface
 * always fetches the whole value list for a cache key instead of
 * a sub-window.
 * 
 * @param <K> the cache key type
 * @param <E> the element type for cache value lists
 */
public interface ICacheWindowRegion<K extends Serializable, E> extends ICacheRegion<K, List<E>> {

	/**
	 * This method returns a sublist of the value list for the specified
	 * key, fetching as much as necessary of the value list if not yet cached.
	 * This requires that the implementation knows how to fetch missing values.
	 * Cached values are returned directly, whether the implementation knows
	 * how to re-fetch them or not.
	 * 
	 * @param key the key (must not be null)
	 * @param start the (0-based) start of the sublist to return
	 * @param count the maximum length of the sublist to return. The returned sublist
	 * will be shorter if the end of the value list for the specified key is reached.
	 * @return the value sublist.
	 * @throws UnsupportedOperationException if the value is missing and this
	 * implementation does not know how to fetch values
	 */
	public List<E> get(K key, int start, int count) throws UnsupportedOperationException;

}
