/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.common.computation.mapping;

import java.util.HashMap;
import java.util.Map;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Expression;

/**
 * This mapping reads objects from a QueryDSL query, generates a key for
 * each object using either a sub-mapping or a subclass method, and stores
 * the objects in a map using that key. Each key should occur only once,
 * otherwise the last object for each key overwrites the previous ones.
 * 
 * @param <K> the key type
 * @param <V> the value (bean) type
 */
public class QueryToSingleMapMapping<K, V> implements IMapping<SQLQuery, Map<K, V>> {

	/**
	 * the beanExpression
	 */
	private Expression<V> beanExpression;

	/**
	 * the keyExtractor
	 */
	private IMapping<V, K> keyExtractor;

	/**
	 * Constructor.
	 * @param beanExpression the expression for the bean to fetch
	 */
	public QueryToSingleMapMapping(Expression<V> beanExpression) {
		this.beanExpression = beanExpression;
		this.keyExtractor = null;
	}

	/**
	 * Constructor.
	 * @param beanExpression the QueryDSL expression for the bean to fetch
	 * @param keyExtractor the mapping from fetched bean to the key for the map
	 */
	public QueryToSingleMapMapping(Expression<V> beanExpression, IMapping<V, K> keyExtractor) {
		this.beanExpression = beanExpression;
		this.keyExtractor = keyExtractor;
	}

	/**
	 * Extracts the key from the specified value. The default implementation uses
	 * the key extractor specified at construction.
	 * @param value the value
	 * @return the key
	 */
	protected K extractKey(V value) {
		if (keyExtractor == null) {
			throw new IllegalStateException("called default implementation of extractKey() without key extractor");
		}
		return keyExtractor.map(value);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.mapping.IMapping#map(java.lang.Object)
	 */
	@Override
	public Map<K, V> map(SQLQuery query) {
        CloseableIterator<V> it = query.iterate(beanExpression);
        Map<K, V> result = new HashMap<K, V>();
        while (it.hasNext()) {
        	V row = it.next();
        	result.put(extractKey(row), row);
        }
        it.close();
        return result;
	}

	/**
	 * Static version of this mapping.
	 * @param query the query to read beans from
	 * @param beanExpression the expression for the beans to read
	 * @param keyExtractor the mapping from bean to key
	 * @return the key-to-bean map
	 */
	public static <K, V> Map<K, V> map(SQLQuery query, Expression<V> beanExpression, IMapping<V, K> keyExtractor) {
		return new QueryToSingleMapMapping<K, V>(beanExpression, keyExtractor).map(query);
	}
	
}
