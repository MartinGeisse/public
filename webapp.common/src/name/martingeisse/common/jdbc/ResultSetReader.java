/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * This class is used to consume data from a {@link ResultSet}. It currently
 * only supports re-ordering: The fieldOrder specifies which fields, and in
 * which order, are taken from the {@link ResultSet}. Fields not in the
 * fieldOrder are ignored. If the fieldOrder contains a field name that does
 * not appear in the {@link ResultSet}, and exception is thrown.
 */
public class ResultSetReader {

	/**
	 * the resultSet
	 */
	private final ResultSet resultSet;
	
	/**
	 * the fieldOrder
	 */
	private final String[] fieldOrder;
	
	/**
	 * the fieldIndexToColumnIndexMapping
	 */
	private final int[] fieldIndexToColumnIndexMapping;

	/**
	 * Constructor.
	 * @param resultSet the result set to read from
	 * @param fieldOrder the field order
	 * @throws SQLException on SQL errors
	 */
	public ResultSetReader(final ResultSet resultSet, final String... fieldOrder) throws SQLException {
		
		// store arguments
		this.resultSet = resultSet;
		this.fieldOrder = fieldOrder;
		
		// create the index mapping
		ResultSetMetaData meta = resultSet.getMetaData();
		fieldIndexToColumnIndexMapping = new int[fieldOrder.length];
		fieldLoop: for (int fieldIndex = 0; fieldIndex < fieldOrder.length; fieldIndex++) {
			String fieldName = fieldOrder[fieldIndex];
			for (int columnIndex=1; columnIndex<=meta.getColumnCount(); columnIndex++) {
				if (meta.getColumnName(columnIndex).equals(fieldName)) {
					fieldIndexToColumnIndexMapping[fieldIndex] = columnIndex;
					continue fieldLoop;
				}
			}
			throw new IllegalArgumentException("column not found in result set: " + fieldName);
		}
		
	}
	
	/**
	 * Getter method for the resultSet.
	 * @return the resultSet
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

	/**
	 * Getter method for the fieldOrder.
	 * @return the fieldOrder
	 */
	public String[] getFieldOrder() {
		return fieldOrder;
	}

	/**
	 * Returns the SQL type for the field with the specified index. The index is the
	 * same index used in the fieldOrder and is internally mapped to the correct table
	 * column from the result set.
	 * @param fieldIndex the field index
	 * @return the SQL type
	 * @throws SQLException on SQL errors
	 */
	public int getSqlFieldType(int fieldIndex) throws SQLException {
		return resultSet.getMetaData().getColumnType(fieldIndexToColumnIndexMapping[fieldIndex]);
	}
	
	/**
	 * Fetches the next row.
	 * @return the row fields, or null if there is no next row
	 * @throws SQLException on SQL errors
	 */
	public Object[] fetchRow() throws SQLException {
		if (!resultSet.next()) {
			return null;
		}
		Object[] row = new Object[fieldOrder.length];
		for (int i=0; i<row.length; i++) {
			row[i] = resultSet.getObject(fieldIndexToColumnIndexMapping[i]);
		}
		return row;
	}
	
}
