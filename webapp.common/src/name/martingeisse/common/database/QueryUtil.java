/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.Pair;
import com.mysema.query.Projectable;
import com.mysema.query.group.QPair;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

/**
 * Utility methods to deal with QueryDSL queries.
 */
public class QueryUtil {

	/**
	 * Prevent instantiation.
	 */
	private QueryUtil() {
	}
	
	/**
	 * Convenience method to fetch an array of fields from a query and return them as a {@link CloseableIterator}
	 * of object arrays. This method allows to specify simple field names instead of {@link Path}s.
	 * 
	 * @param projectable the query
	 * @param parentPath the parent path to which fields are relative
	 * @param fieldNames the field names
	 * @return the query result iterator
	 */
	public static CloseableIterator<Object[]> iterateFields(Projectable projectable, Path<?> parentPath, String... fieldNames) {
		return projectable.iterate(convertFieldNamesToFieldExpressions(parentPath, fieldNames));
	}

	/**
	 * Convenience method to fetch an array of fields from a query and return them as a {@link ResultSet}.
	 * This method allows to specify simple field names instead of {@link Path}s.
	 * 
	 * @param query the query
	 * @param parentPath the parent path to which fields are relative
	 * @param fieldNames the field names
	 * @return the result set
	 */
	public static ResultSet getFieldsResultSet(SQLQuery query, Path<?> parentPath, String... fieldNames) {
		return query.getResults(convertFieldNamesToFieldExpressions(parentPath, fieldNames));
	}

	/**
	 * Takes an array of field names and a parent path, and creates an array of combined field paths.
	 * @param parentPath the parent path
	 * @param fieldNames the field names
	 * @return the field paths
	 */
	private static Path<?>[] convertFieldNamesToFieldExpressions(Path<?> parentPath, String... fieldNames) {
		int width = fieldNames.length;
		Path<?>[] fieldPaths = new Path<?>[width];
		for (int i=0; i<width; i++) {
			fieldPaths[i] = Expressions.path(Object.class, parentPath, fieldNames[i]);
		}
		return fieldPaths;
	}
	
	/**
	 * Like {@link Expressions#constant(Object)}, but unwraps {@link Pair} objects.
	 * @param value the constant value
	 * @return the constant expression
	 */
	public static <T> Expression<T> constant(T value) {
		if (value instanceof Pair) {
			Pair<?, ?> pair = (Pair<?, ?>)value;
			Expression<?> first = Expressions.constant(pair.getFirst());
			Expression<?> second = Expressions.constant(pair.getSecond());
			@SuppressWarnings("unchecked")
			Expression<T> result = (Expression<T>)QPair.create(first, second);
			return result;
		} else {
			return Expressions.constant(value);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// query factories
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a new query for the specified tables.
	 * @param qpaths the table paths
	 * @return the query
	 */
	public static SQLQuery from(Expression<?>... qpaths) {
		return EntityConnectionManager.getConnection().createQuery().from(qpaths);
	}
	
	/**
	 * Creates a new query builder for the specified path and database.
	 * @param qpaths the table paths
	 * @param database the database to use
	 * @return the query
	 */
	public static <T> SQLQuery from(IDatabaseDescriptor database, Expression<?>... qpaths) {
		return EntityConnectionManager.getConnection(database).createQuery().from(qpaths);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// shortcut methods for common queries
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * Fetches all rows from a table.
	 * 
	 * This method uses the default database connection.
	 * 
	 * @param qpath the QueryDSL relational path for the table, e.g. QMyTable.myTable
	 * @return the rows
	 */
	public static <ROW> List<ROW> fetchAll(RelationalPathBase<ROW> qpath) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(qpath).list(qpath);
	}
	
	/**
	 * Fetches a single field of all rows from a table.
	 * 
	 * This method uses the default database connection.
	 * 
	 * @param qpath the QueryDSL relational path for the table, e.g. QMyTable.myTable
	 * @param fieldPath the path of the field to return
	 * @return the fields
	 */
	public static <ROW, FIELD> List<FIELD> fetchAll(RelationalPathBase<ROW> qpath, Path<FIELD> fieldPath) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(qpath).list(fieldPath);
	}
	
	/**
	 * Fetches all rows from a table and puts them in a map (key-to-single-row),
	 * using the specified key field.
	 * 
	 * This method uses the default database connection.
	 * 
	 * @param qpath the QueryDSL relational path for the table, e.g. QMyTable.myTable
	 * @param keyFieldPath the path of the field to use as the map key
	 * @return the map
	 */
	public static <ROW, FIELD> Map<FIELD, ROW> mapAll(RelationalPathBase<ROW> qpath, Path<FIELD> keyFieldPath) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(qpath).map(keyFieldPath, qpath);
	}
	
	/**
	 * Fetches the first row from a table that satisfies a specific predicate.
	 * This method implements the common case: SELECT * FROM [row] WHERE [predicates] LIMIT 1
	 * 
	 * This method uses the default database connection.
	 * 
	 * @param qpath the QueryDSL relational path for the table, e.g. QMyTable.myTable
	 * @param predicates the predicates
	 * @return the row, or null if not found
	 */
	public static <ROW> ROW fetchSingle(RelationalPathBase<ROW> qpath, Predicate... predicates) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(qpath).where(predicates).singleResult(qpath);
	}
	
	/**
	 * Fetches a single field of the first row from a table that satisfies a specific predicate.
	 * This method implements the common case: SELECT [field] FROM [row] WHERE [predicates] LIMIT 1
	 * 
	 * This method uses the default database connection.
	 * 
	 * @param qpath the QueryDSL relational path for the table, e.g. QMyTable.myTable
	 * @param fieldPath the path of the field to return
	 * @param predicates the predicates
	 * @return the field, or null if not found
	 */
	public static <ROW, FIELD> FIELD fetchSingle(RelationalPathBase<ROW> qpath, Path<FIELD> fieldPath, Predicate... predicates) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(qpath).where(predicates).singleResult(fieldPath);
	}
	
	/**
	 * Fetches all rows from a table that satisfy a specific predicate.
	 * This method implements the common case: SELECT * FROM [row] WHERE [predicates]
	 * 
	 * This method uses the default database connection.
	 * 
	 * @param qpath the QueryDSL relational path for the table, e.g. QMyTable.myTable
	 * @param predicates the predicates
	 * @return the rows
	 */
	public static <ROW> List<ROW> fetchMultiple(RelationalPathBase<ROW> qpath, Predicate... predicates) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(qpath).where(predicates).list(qpath);
	}
	
	/**
	 * Fetches a single field of all rows from a table that satisfy a specific predicate.
	 * This method implements the common case: SELECT * FROM [row] WHERE [predicates]
	 * 
	 * This method uses the default database connection.
	 * 
	 * @param qpath the QueryDSL relational path for the table, e.g. QMyTable.myTable
	 * @param fieldPath the path of the field to return
	 * @param predicates the predicates
	 * @return the fields
	 */
	public static <ROW, FIELD> List<FIELD> fetchMultiple(RelationalPathBase<ROW> qpath, Path<FIELD> fieldPath, Predicate... predicates) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(qpath).where(predicates).list(fieldPath);
	}
	
}
