/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.cakephp;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import name.martingeisse.common.database.QueryUtil;
import name.martingeisse.common.javascript.JavascriptAssembler;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Path;

/**
 * Utility methods for JSON generation using a {@link JavascriptAssembler}.
 */
public final class CakephpJsonUtil {

	/**
	 * Prevent instantiation.
	 */
	private CakephpJsonUtil() {
	}
	
	/**
	 * Assembles a JSON object from the current row of the specified result set.
	 * 
	 * If modelName is null, then this method simply assembles the fields of the
	 * current row as a JSON object. Otherwise, the same object is wrapped
	 * in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 * @param resultSet the result set to read from
	 */
	public static void generateModelEntry(JavascriptAssembler assembler, String modelName, ResultSet resultSet) throws SQLException {
		
		// handle model name wrapping
		if (modelName != null) {
			assembler.beginObject();
			assembler.prepareObjectProperty(modelName);
			generateModelEntry(assembler, null, resultSet);
			assembler.endObject();
			return;
		}
		
		// assemble data fields
		ResultSetMetaData metaData = resultSet.getMetaData();
		int width = metaData.getColumnCount();
		assembler.beginObject();
		for (int i=0; i<width; i++) {
			assembler.prepareObjectProperty(metaData.getColumnLabel(i + 1));
			assembler.appendPrimitive(resultSet.getObject(i + 1));
		}
		assembler.endObject();
		
	}

	/**
	 * Assembles a JSON object from the first for returned by the specified query. Throws a
	 * {@link NoSuchElementException} if the query does not return any results.
	 * 
	 * If modelName is null, then this method simply assembles the fields of the
	 * row as a JSON object. Otherwise, the same object is wrapped
	 * in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 * @param query the query
	 * @param parentPath the parent path for field paths
	 * @param fieldNames the field names
	 * @throws SQLException on SQL errors
	 */
	public static void generateModelEntry(JavascriptAssembler assembler, String modelName, SQLQuery query, Path<?> parentPath, String... fieldNames) throws SQLException {
		ResultSet resultSet = QueryUtil.getFieldsResultSet(query, parentPath, fieldNames);
		if (!resultSet.next()) {
			throw new NoSuchElementException();
		}
		generateModelEntry(assembler, modelName, resultSet);
		resultSet.close();
	}
	
	/**
	 * Assembles all rows from the specified resultSet, not including the current
	 * row (if any), using {{@link #generateModelEntry(ResultSet, String)}
	 * and wrapped in a JSON array.
	 * 
	 * This method is intended primarily for use with a fresh {@link ResultSet}
	 * from which no rows have been fetched yet.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just return the inner object.
	 * @param resultSet the result set to read from
	 */
	public static void generateModelList(JavascriptAssembler assembler, String modelName, ResultSet resultSet) throws SQLException {
		assembler.beginList();
		while (resultSet.next()) {
			assembler.prepareListElement();
			generateModelEntry(assembler, modelName, resultSet);
		}
		assembler.endList();
	}

	/**
	 * Assembles a JSON object from all rows returned by the specified query.
	 * 
	 * If modelName is null, then this method simply assembles the fields of each
	 * row as a JSON object. Otherwise, the same object is wrapped
	 * in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 * @param query the query
	 * @param parentPath the parent path for field paths
	 * @param fieldNames the field names
	 * @throws SQLException on SQL errors
	 */
	public static void generateModelList(JavascriptAssembler assembler, String modelName, SQLQuery query, Path<?> parentPath, String... fieldNames) throws SQLException {
		ResultSet resultSet = QueryUtil.getFieldsResultSet(query, parentPath, fieldNames);
		generateModelList(assembler, modelName, resultSet);
		resultSet.close();
	}
	
}
