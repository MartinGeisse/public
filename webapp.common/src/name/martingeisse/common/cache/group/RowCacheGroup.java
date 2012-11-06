/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.common.cache.querydsl.RowLoader;
import name.martingeisse.common.computation.mapping.IMapping;
import name.martingeisse.common.util.Wrapper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * Represents a group of caches using {@link RowLoader}s. These caches store the
 * same values using different keys. Whenever a value is stored in one of the caches,
 * it is also stored in the other caches. They are not guaranteed to *contain* the
 * same values since they may evict values independently.
 * 
 * @param <V> the cache value type
 */
public class RowCacheGroup<V> {

	/**
	 * the caches
	 */
	private final List<LoadingCache<?, Wrapper<V>>> caches = new ArrayList<LoadingCache<?, Wrapper<V>>>();

	/**
	 * the cacheBuilder
	 */
	private final CacheBuilder<?, ?> cacheBuilder;

	/**
	 * the path
	 */
	private final RelationalPath<V> path;

	/**
	 * the additionalPredicates
	 */
	private final Predicate[] additionalPredicates;
	
	/**
	 * the loaderToCache
	 */
	private final Map<RowLoader<?, V>, LoadingCache<?, Wrapper<V>>> loaderToCache = new HashMap<RowLoader<?, V>, LoadingCache<?, Wrapper<V>>>();
	
	/**
	 * the cacheToValueToKeyMapping
	 */
	private final Map<LoadingCache<?, Wrapper<V>>, IMapping<V, ?>> cacheToValueToKeyMapping = new HashMap<LoadingCache<?, Wrapper<V>>, IMapping<V,?>>();

	/**
	 * Constructor.
	 * @param cacheBuilder the cache builder to use
	 * @param path the table path
	 * @param additionalPredicates additional predicates (if any)
	 */
	public RowCacheGroup(CacheBuilder<?, ?> cacheBuilder, RelationalPath<V> path, Predicate... additionalPredicates) {
		this.cacheBuilder = cacheBuilder;
		this.path = path;
		this.additionalPredicates = additionalPredicates;
	}

	/**
	 * This method is a shortcut for distribute(wrapper, null).
	 * @param wrapper the wrapper to distribute
	 */
	public void distribute(Wrapper<V> wrapper) {
		distribute(wrapper, null);
	}

	/**
	 * Distributes the specified value to all caches. This adds the value to all
	 * caches except the origin, because the origin will store the value itself.
	 * Specify null for the origin to distribute the value to all caches.
	 * 
	 * @param wrapper the wrapper to distribute
	 * @param origin the origin of the value
	 */
	public void distribute(Wrapper<V> wrapper, LoadingCache<?, ?> origin) {
		distributeHelper(wrapper, origin, false);
	}

	/**
	 * 
	 */
	private void distributeHelper(Wrapper<V> wrapper, LoadingCache<?, ?> origin, boolean external) {
		for (LoadingCache<?, Wrapper<V>> cache : caches) {
			if (cache != origin) {
				IMapping<?, ?> valueToKeyMapping = cacheToValueToKeyMapping.get(cache);
				distributeHelper(wrapper, valueToKeyMapping, cache);
				prepareCachedValue(wrapper, external);
			}
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private void distributeHelper(Wrapper wrapper, IMapping valueToKeyMapping, LoadingCache cache) {
		Object key = valueToKeyMapping.map(wrapper.getValue());
		cache.put(key, wrapper);
	}

	/**
	 * Creates a new cache using the specified key expression to fetch missing values, and the
	 * valueToKeyMapping to generate keys for the cache.
	 * 
	 * @param keyExpression the key expression
	 * @param valueToKeyMapping the mapping that generates keys for the cache
	 * @return the cache
	 */
	public <K> LoadingCache<K, Wrapper<V>> addCache(Expression<?> keyExpression, IMapping<V, K> valueToKeyMapping) {

		// create the loader
		RowLoader<K, V> rowLoader = new RowLoader<K, V>(path, keyExpression, additionalPredicates) {
			@Override
			protected Wrapper<V> transformValue(K key, V row) {
				Wrapper<V> wrapper = super.transformValue(key, row);
				if (wrapper.getValue() != null) {
					distributeHelper(wrapper, loaderToCache.get(this), true);
				}
				return wrapper;
			}
		};

		// create the cache
		@SuppressWarnings("unchecked")
		CacheBuilder<K, Wrapper<V>> typedCacheBuilder = (CacheBuilder<K, Wrapper<V>>)cacheBuilder;
		LoadingCache<K, Wrapper<V>> cache = typedCacheBuilder.build(rowLoader);
		caches.add(cache);
		loaderToCache.put(rowLoader, cache);
		cacheToValueToKeyMapping.put(cache, valueToKeyMapping);
		return cache;

	}
	
	/**
	 * This method can be used to prepare cached values for use. The default implementation
	 * does nothing.
	 *  
	 * @param wrapper the cached wrapper (never null)
	 * @param external whether the value comes from an external source via one of the
	 * distribute() methods.
	 */
	protected void prepareCachedValue(Wrapper<V> wrapper, boolean external) {
	}

}
