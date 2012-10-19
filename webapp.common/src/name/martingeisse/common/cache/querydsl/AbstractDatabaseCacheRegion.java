/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache.querydsl;

import java.io.Serializable;

import com.mysema.query.sql.SQLQuery;

import name.martingeisse.common.cache.AbstractCacheRegion;
import name.martingeisse.common.cache.ICacheRegion;
import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.IDatabaseDescriptor;

/**
 * This class provides basic support for database / QueryDSL
 * based cache regions.
 * 
 * @param <K> the type of cache keys
 * @param <V> the type of cached values
 */
public abstract class AbstractDatabaseCacheRegion<K extends Serializable, V> extends AbstractCacheRegion<K, V> implements Cloneable {

	/**
	 * the databaseDescriptor
	 */
	private IDatabaseDescriptor databaseDescriptor;
	
	/**
	 * Constructor.
	 * @param regionName the cache region name
	 */
	public AbstractDatabaseCacheRegion(String regionName) {
		super(regionName);
		this.databaseDescriptor = null;
	}

	/**
	 * Constructor.
	 * @param regionName the cache region name
	 * @param databaseDescriptor the database to use
	 */
	public AbstractDatabaseCacheRegion(String regionName, IDatabaseDescriptor databaseDescriptor) {
		super(regionName);
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
	public ICacheRegion<K, V> withDatabase(IDatabaseDescriptor databaseDescriptor) {
		try {
			@SuppressWarnings("unchecked")
			AbstractDatabaseCacheRegion<K, V> clone = (AbstractDatabaseCacheRegion<K, V>)clone();
			clone.databaseDescriptor = databaseDescriptor;
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
