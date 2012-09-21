/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database;

import java.sql.ResultSet;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.Projectable;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Path;

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
	
}
