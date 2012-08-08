/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.datarow;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Represents a row of data as well as meta-data to which it conforms.
 * 
 * Note that this class does not ensure that either meta-data or data
 * are present, and does not ensure that the data actually conforms
 * to the meta-data. It is up to the caller to ensure that.
 */
public class DataRow extends AbstractDataRowMetaHolder {

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
		setMeta(meta);
	}

	/**
	 * Constructor. Sets the meta-data from the specified JDBC meta-data but
	 * leaves the row data unset.
	 * @param resultSetMetaData the JDBC meta-data to create the row meta-data from
	 * @throws SQLException on SQL errors
	 */
	public DataRow(ResultSetMetaData resultSetMetaData) throws SQLException {
		setMeta(new DataRowMeta(resultSetMetaData));
	}

	/**
	 * Constructor that creates an instance from the first row of the
	 * specified result set.
	 * @param resultSet the JDBC result set
	 * @throws SQLException on SQL errors
	 */
	public DataRow(ResultSet resultSet) throws SQLException {
		if (!resultSet.next()) {
			throw new IllegalArgumentException("the result set has no rows");
		}
		setMeta(new DataRowMeta(resultSet.getMetaData()));
		data = createDataForCurrentRow(resultSet);
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

}
