/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.common.computation.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Expression;

/**
 * This mapping reads objects from a QueryDSL query, generates a key for
 * each object using either a sub-mapping or a subclass method, and stores
 * the objects in a map of lists using that key. That is, for each key,
 * the value stored in the map is a list of objects with that key.
 */
public class QueryToListMapMapping<K, V> implements IMapping<SQLQuery, Map<K, List<V>>> {

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
	 */
	public QueryToListMapMapping(Expression<V> beanExpression) {
		this.beanExpression = beanExpression;
		this.keyExtractor = null;
	}

	/**
	 * Constructor.
	 * @param beanExpression the QueryDSL expression for the bean to fetch
	 * @param keyExtractor the mapping from fetched bean to the key for the map
	 */
	public QueryToListMapMapping(Expression<V> beanExpression, IMapping<V, K> keyExtractor) {
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
	public Map<K, List<V>> map(SQLQuery query) {
        CloseableIterator<V> it = query.iterate(beanExpression);
        Map<K, List<V>> result = new HashMap<K, List<V>>();
        while (it.hasNext()) {
        	V row = it.next();
        	K key = extractKey(row);
        	List<V> list = result.get(key);
        	if (list == null) {
        		list = new ArrayList<V>();
        		result.put(key, list);
        	}
        	list.add(row);
        }
        it.close();
        return result;
	}

	/**
	 * Static version of this mapping.
	 * @param query the query to read beans from
	 * @param beanExpression the expression for the beans to read
	 * @param keyExtractor the mapping from bean to key
	 * @return the map of lists
	 */
	public static <K, V> Map<K, List<V>> map(SQLQuery query, Expression<V> beanExpression, IMapping<V, K> keyExtractor) {
		return new QueryToListMapMapping<K, V>(beanExpression, keyExtractor).map(query);
	}
	
}
