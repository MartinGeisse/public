/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.cakephp;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import name.martingeisse.common.javascript.JavascriptAssembler;

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
	 * @param resultSet the result set to read from
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 */
	public static void generateModelEntry(JavascriptAssembler assembler, ResultSet resultSet, String modelName) throws SQLException {
		
		// handle model name wrapping
		if (modelName != null) {
			assembler.beginObject();
			assembler.prepareObjectProperty(modelName);
			generateModelEntry(assembler, resultSet, null);
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
	 * Assembles all rows from the specified resultSet, not including the current
	 * row (if any), using {{@link #generateModelEntry(ResultSet, String)}
	 * and wrapped in a JSON array.
	 * 
	 * This method is intended primarily for use with a fresh {@link ResultSet}
	 * from which no rows have been fetched yet.
	 * 
	 * @param assembler the Javascript assembler
	 * @param resultSet the result set to read from
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just return the inner object.
	 */
	public static void generateModelList(JavascriptAssembler assembler, ResultSet resultSet, String modelName) throws SQLException {
		assembler.beginList();
		while (resultSet.next()) {
			assembler.prepareListElement();
			generateModelEntry(assembler, resultSet, modelName);
		}
		assembler.endList();
	}
	
}
