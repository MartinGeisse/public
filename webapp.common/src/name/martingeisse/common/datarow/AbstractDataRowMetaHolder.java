/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.datarow;

import java.sql.ResultSet;
import java.sql.SQLException;

import name.martingeisse.common.util.ParameterUtil;

/**
 * Common base class for {@link DataRow} and {@link DataRows}.
 */
public abstract class AbstractDataRowMetaHolder {

	/**
	 * the meta
	 */
	private DataRowMeta meta;

	/**
	 * Constructor.
	 */
	public AbstractDataRowMetaHolder() {
	}

	/**
	 * Getter method for the meta.
	 * @return the meta
	 */
	public DataRowMeta getMeta() {
		return meta;
	}

	/**
	 * Setter method for the meta.
	 * @param meta the meta to set
	 */
	public void setMeta(final DataRowMeta meta) {
		this.meta = meta;
	}
	
	/**
	 * Makes sure that both the result set and the type converters array are non-null, that the
	 * number of type converters equals the result set row width, and that the type converters
	 * array has no null entries.
	 * @param resultSet
	 * @param typeConverters
	 * @throws SQLException
	 */
	protected static final void argumentCheck(ResultSet resultSet, IDataRowTypeConverter[] typeConverters) throws SQLException {
		ParameterUtil.ensureNotNull(resultSet, "resultSet");
		ParameterUtil.ensureNotNull(typeConverters, "typeConverters");
		ParameterUtil.ensureNoNullElement(typeConverters, "typeConverters");
		if (resultSet.getMetaData().getColumnCount() != typeConverters.length) {
			throw new IllegalArgumentException("result set width is " + resultSet.getMetaData().getColumnCount() + " but number of type converters is " + typeConverters);
		}
	}

	/**
	 * Reads the current row data from the specified result set
	 * and creates a data array from it. This does not advance the
	 * result set.
	 * @param resultSet the result set to read from
	 * @param typeConverters the type converters that extract values from the result set.
	 * This array must have the same size as the result set rows.
	 * @return the row values
	 */
	protected static Object[] createDataForCurrentRow(ResultSet resultSet, IDataRowTypeConverter[] typeConverters) throws SQLException {
		argumentCheck(resultSet, typeConverters);
		Object[] data = new Object[resultSet.getMetaData().getColumnCount()];
		for (int i=0; i<data.length; i++) {
			data[i] = typeConverters[i].readFromResultSet(resultSet, i + 1);
		}
		return data;
	}

	/**
	 * Returns the value of the specified field.
	 * @param fieldName the name of the field whose value to return
	 * @return the field value
	 */
	protected final Object getFieldValue(final Object[] data, final String fieldName) {
		String[] fieldNames = getMeta().getNames();
		for (int i = 0; i < fieldNames.length; i++) {
			if (fieldNames[i].equals(fieldName)) {
				return data[i];
			}
		}
		throw new IllegalArgumentException("unknown data row field: " + fieldName);
	}

	/**
	 * Sets the value of the specified field.
	 * @param fieldName the name of the field whose value to set
	 * @param fieldValue the value to set
	 */
	protected final void setFieldValue(final Object[] data, final String fieldName, final Object fieldValue) {
		String[] fieldNames = getMeta().getNames();
		for (int i = 0; i < fieldNames.length; i++) {
			if (fieldNames[i].equals(fieldName)) {
				data[i] = fieldValue;
				return;
			}
		}
		throw new IllegalArgumentException("unknown data row field: " + fieldName);
	}
	
}
