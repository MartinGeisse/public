/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.serialize;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import name.martingeisse.common.javascript.JavascriptAssembler;

/**
 * This class turns JDBC {@link ResultSet} objects into Javascript objects.
 * It only serializes the current row of a {@link ResultSet} and never
 * advances the row cursor. It optionally allows subclasses to add
 * custom generated fields that are not actually present in the row being
 * serialized.
 */
public class ResultSetRowToJavascriptObjectSerializer extends AbstractJavascriptSerializer<ResultSet> {
	
	/**
	 * The shared instance of this class.
	 */
	public static final ResultSetRowToJavascriptObjectSerializer instance = new ResultSetRowToJavascriptObjectSerializer();
	
	/**
	 * Constructor.
	 */
	public ResultSetRowToJavascriptObjectSerializer() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.serialize.IJavascriptSerializer#serialize(java.lang.Object, name.martingeisse.common.javascript.JavascriptAssembler)
	 */
	@Override
	public void serialize(ResultSet resultSet, JavascriptAssembler assembler) {
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			int width = metaData.getColumnCount();
			assembler.beginObject();
			for (int i=0; i<width; i++) {
				assembler.prepareObjectProperty(metaData.getColumnLabel(i + 1));
				assembler.appendPrimitive(resultSet.getObject(i + 1));
			}
			serializeGeneratedFields(resultSet, assembler);
			assembler.endObject();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * This method is a stub that allows subclasses to add custom generated fields not
	 * actually present in the row being serialized. The default implementation does nothing.
	 * 
	 * @param resultSet the result set that contains the row being serialized
	 * @param assembler the assembler
	 */
	protected void serializeGeneratedFields(ResultSet resultSet, JavascriptAssembler assembler) {
	}
	
}
