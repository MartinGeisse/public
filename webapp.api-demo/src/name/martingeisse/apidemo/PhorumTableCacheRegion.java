/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo;

import java.io.Serializable;

import name.martingeisse.common.cache.AbstractQuerydslTableCacheRegion;
import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.JdbcEntityDatabaseConnection;

import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * Cache implementation for Phorum tables.
 * 
 * @param <K> the type of cache keys
 * @param <V> the type of cached values
 */
public class PhorumTableCacheRegion<K extends Serializable, V> extends AbstractQuerydslTableCacheRegion<K, V> {

	/**
	 * Constructor.
	 * @param regionName the name of the region represented by this object
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param additionalPredicates additional predicates (if any)
	 */
	public PhorumTableCacheRegion(String regionName, RelationalPath<V> path, Expression<?> keyExpression, Predicate... additionalPredicates) {
		super(regionName, path, keyExpression, additionalPredicates);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractQuerydslTableCacheRegion#createQuery()
	 */
	@Override
	protected SQLQuery createQuery() {
		JdbcEntityDatabaseConnection entityConnection = (JdbcEntityDatabaseConnection)EntityConnectionManager.getConnection();
		return new SQLQueryImpl(entityConnection.getJdbcConnection(), new MySQLTemplates());
	}
	
}
