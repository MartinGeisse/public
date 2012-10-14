/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache;

import java.io.Serializable;

import name.martingeisse.common.util.ParameterUtil;

/**
 * Implementation of {@link ICacheRegion} for caches that cannot fetch
 * values themselves.
 * 
 * @param <K> the type of cache keys
 * @param <V> the type of cached values
 */
public final class ManualCacheRegion<K extends Serializable, V> extends AbstractCacheRegion<K, V> {

	/**
	 * Constructor.
	 * @param regionName the name of the region represented by this object
	 */
	public ManualCacheRegion(final String regionName) {
		super(regionName);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractCacheRegion#fetch(java.io.Serializable)
	 */
	@Override
	public V fetch(final K key) throws UnsupportedOperationException {
		ParameterUtil.ensureNotNull(key, "key");
		throw new UnsupportedOperationException("ManualCacheRegion cannot automatically fetch missing value for key: " + key);
	}

}
