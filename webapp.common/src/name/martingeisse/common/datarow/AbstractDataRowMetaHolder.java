/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.datarow;

import java.sql.ResultSet;
import java.sql.SQLException;

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
	 * Reads the current row data from the specified result set
	 * and creates a data array from it. This does not advance the
	 * result set.
	 * @param resultSet the result set to read from
	 * @return the row values
	 */
	protected static Object[] createDataForCurrentRow(ResultSet resultSet) throws SQLException {
		Object[] data = new Object[resultSet.getMetaData().getColumnCount()];
		for (int i=0; i<data.length; i++) {
			data[i] = resultSet.getObject(i + 1);
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
