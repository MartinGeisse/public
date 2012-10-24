/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache.querydsl;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.IDatabaseDescriptor;

import com.google.common.cache.CacheLoader;
import com.mysema.query.sql.SQLQuery;

/**
 * This class provides basic support for database / QueryDSL based cache loaders.
 * 
 * TODO remove cloneable, apply builder pattern
 * 
 * @param <K> the type of cache keys
 * @param <V> the type of cached values
 */
public abstract class AbstractDatabaseCacheLoader<K, V> extends CacheLoader<K, V> implements Cloneable {

	/**
	 * the databaseDescriptor
	 */
	private IDatabaseDescriptor databaseDescriptor;
	
	/**
	 * Constructor.
	 */
	public AbstractDatabaseCacheLoader() {
		this.databaseDescriptor = null;
	}

	/**
	 * Constructor.
	 * @param databaseDescriptor the database to use
	 */
	public AbstractDatabaseCacheLoader(IDatabaseDescriptor databaseDescriptor) {
		this.databaseDescriptor = databaseDescriptor;
	}
	
	/**
	 * Getter method for the databaseDescriptor.
	 * @return the databaseDescriptor
	 */
	public IDatabaseDescriptor getDatabaseDescriptor() {
		return databaseDescriptor;
	}

	/**
	 * Creates a new {@link SQLQuery} to access the underlying database.
	 * @return the query
	 */
	protected SQLQuery createQuery() {
		if (databaseDescriptor == null) {
			return EntityConnectionManager.getConnection().createQuery();
		} else {
			return EntityConnectionManager.getConnection(databaseDescriptor).createQuery();
		}
	}
	
	/**
	 * Creates a clone of this region, but with the specified database descriptor.
	 * @param databaseDescriptor the new database descriptor to use for the clone
	 * @return the new region
	 */
	public AbstractDatabaseCacheLoader<K, V> withDatabase(IDatabaseDescriptor databaseDescriptor) {
		try {
			@SuppressWarnings("unchecked")
			AbstractDatabaseCacheLoader<K, V> clone = (AbstractDatabaseCacheLoader<K, V>)clone();
			clone.databaseDescriptor = databaseDescriptor;
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
