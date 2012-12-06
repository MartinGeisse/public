/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache.querydsl;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.IDatabaseDescriptor;

import com.google.common.cache.CacheLoader;
import com.mysema.commons.lang.Pair;
import com.mysema.query.group.QPair;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.PredicateOperation;

/**
 * This class provides basic support for database / QueryDSL based cache loaders.
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
	
	/**
	 * Creates a (keyExpression == keyConstantValue) predicate. This allows subclasses to intercept
	 * the process and create custom predicates (currently needed to work around QueryDSL's missing
	 * support for {@link Pair} constants and {@link QPair} syntax errors).
	 * @param keyExpression the expression for the key column (never null)
	 * @param keyConstantValue the constant value to compare with
	 * @return the predicate
	 */
	protected PredicateOperation createKeyEqualsPredicate(Expression<?> keyExpression, K keyConstantValue) {
		return new PredicateOperation(Ops.EQ, keyExpression, Expressions.constant(keyConstantValue));
	}
	
	/**
	 * Creates a (keyExpression IN (keyConstantValues, ...)) predicate. This allows subclasses to intercept
	 * the process and create custom predicates (currently needed to work around QueryDSL's missing
	 * support for {@link Pair} constants and {@link QPair} syntax errors).
	 * @param keyExpression the expression for the key column (never null)
	 * @param keyConstantValues the constant values to search
	 * @return the predicate
	 */
	protected PredicateOperation createKeyInPredicate(Expression<?> keyExpression, Iterable<? extends K> keyConstantValues) {
		return new PredicateOperation(Ops.IN, keyExpression, Expressions.constant(keyConstantValues));
	}
	
}
