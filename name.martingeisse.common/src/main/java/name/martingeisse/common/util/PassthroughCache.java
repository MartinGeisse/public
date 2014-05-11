/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;
import org.apache.commons.lang3.NotImplementedException;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.UncheckedExecutionException;

/**
 * A {@link LoadingCache} implementation that does not actually cache
 * any values, but just passes requests to the {@link CacheLoader}.
 *
 * @param <K> the cache key type
 * @param <V> the cache value type
 */
public final class PassthroughCache<K, V> implements LoadingCache<K, V> {

	/**
	 * the loader
	 */
	private final CacheLoader<K, V> loader;

	/**
	 * Constructor.
	 * @param loader the cache loader
	 */
	public PassthroughCache(CacheLoader<K, V> loader) {
		this.loader = loader;
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.Cache#cleanUp()
	 */
	@Override
	public void cleanUp() {
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.Cache#get(java.lang.Object, java.util.concurrent.Callable)
	 */
	@Override
	public V get(K key, Callable<? extends V> producer) throws ExecutionException {
		try {
			return producer.call();
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.Cache#getAllPresent(java.lang.Iterable)
	 */
	@Override
	public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
		return ImmutableMap.<K, V>builder().build();
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.Cache#getIfPresent(java.lang.Object)
	 */
	@Override
	@Nullable
	public V getIfPresent(Object key) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.Cache#invalidate(java.lang.Object)
	 */
	@Override
	public void invalidate(Object key) {
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.Cache#invalidateAll()
	 */
	@Override
	public void invalidateAll() {
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.Cache#invalidateAll(java.lang.Iterable)
	 */
	@Override
	public void invalidateAll(Iterable<?> keys) {
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.Cache#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void put(K key, V value) {
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.Cache#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> pairs) {
	}
	
	/* (non-Javadoc)
	 * @see com.google.common.cache.Cache#size()
	 */
	@Override
	public long size() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.Cache#stats()
	 */
	@Override
	public CacheStats stats() {
		return new CacheStats(0, 0, 0, 0, 0, 0);
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.LoadingCache#apply(java.lang.Object)
	 */
	@Override
	public V apply(K key) {
		try {
			return loader.load(key);
		} catch (Exception e) {
			throw new UncheckedExecutionException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.LoadingCache#asMap()
	 */
	@Override
	public ConcurrentMap<K, V> asMap() {
		throw new NotImplementedException("");
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.LoadingCache#get(java.lang.Object)
	 */
	@Override
	public V get(K key) throws ExecutionException {
		try {
			return loader.load(key);
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.LoadingCache#getAll(java.lang.Iterable)
	 */
	@Override
	public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
		try {
			return ImmutableMap.copyOf(loader.loadAll(keys));
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.LoadingCache#getUnchecked(java.lang.Object)
	 */
	@Override
	public V getUnchecked(K key) {
		try {
			return loader.load(key);
		} catch (Exception e) {
			throw new UncheckedExecutionException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.google.common.cache.LoadingCache#refresh(java.lang.Object)
	 */
	@Override
	public void refresh(K key) {
	}

}
