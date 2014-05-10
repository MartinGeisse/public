/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.datarow;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import name.martingeisse.common.database.IDatabaseDescriptor;

/**
 * Default implementation of {@link IDataRow} based on an array.
 */
public class DataRow extends AbstractDataRowMetaHolder implements IDataRow {

	/**
	 * the data
	 */
	private Object[] data;

	/**
	 * Constructor. Leaves the meta-data and row data unset.
	 */
	public DataRow() {
	}

	/**
	 * Constructor. Sets the specified meta-data but leaves the row data unset.
	 * @param meta the meta-data to set
	 */
	public DataRow(DataRowMeta meta) {
		setDataRowMeta(meta);
	}

	/**
	 * Constructor. Sets the meta-data from the specified JDBC meta-data but
	 * leaves the row data unset.
	 * @param resultSetMetaData the JDBC meta-data to create the row meta-data from
	 * @throws SQLException on SQL errors
	 */
	public DataRow(ResultSetMetaData resultSetMetaData) throws SQLException {
		if (resultSetMetaData == null) {
			throw new IllegalArgumentException("resultSetMetaData argument is null");
		}
		setDataRowMeta(new DataRowMeta(resultSetMetaData));
	}

	/**
	 * Constructor that creates an instance from the current row of the
	 * specified result set. This method does not advance the result set.
	 * @param resultSet the JDBC result set
	 * @param typeConverters the type converters that extract values from the result set.
	 * This array must have the same size as the result set rows.
	 * @param databaseDescriptor the database descriptor from which the result set was read
	 * @throws SQLException on SQL errors
	 */
	public DataRow(ResultSet resultSet, IDataRowTypeConverter[] typeConverters, IDatabaseDescriptor databaseDescriptor) throws SQLException {
		argumentCheck(resultSet, typeConverters);
		setDataRowMeta(new DataRowMeta(resultSet.getMetaData()));
		data = createDataForCurrentRow(resultSet, typeConverters, databaseDescriptor);
	}

	/**
	 * Getter method for the data.
	 * @return the data
	 */
	public Object[] getData() {
		return data;
	}

	/**
	 * Setter method for the data.
	 * @param data the data to set
	 */
	public void setData(final Object[] data) {
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRow#getDataRowFields()
	 */
	@Override
	public Object[] getDataRowFields() {
		return data;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRow#getDataRowFieldValue(java.lang.String)
	 */
	@Override
	public Object getDataRowFieldValue(String fieldName) {
		return getFieldValue(getData(), fieldName);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRow#setDataRowFieldValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setDataRowFieldValue(String fieldName, Object fieldValue) {
		setFieldValue(getData(), fieldName, fieldValue);
	}

}
