/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.datarow;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents multiple rows of data that conform to the same meta-data.
 * 
 * Note that this class does not ensure that either meta-data or data
 * are present, and does not ensure that the data actually conforms
 * to the meta-data. It is up to the caller to ensure that.
 */
public class DataRows extends AbstractDataRowMetaHolder implements Serializable {

	/**
	 * the rows
	 */
	private List<Object[]> rows = new ArrayList<Object[]>();

	/**
	 * Constructor. Leaves the meta-data unset and the row list empty.
	 */
	public DataRows() {
	}

	/**
	 * Constructor. Sets the specified meta-data but leaves the row list empty.
	 * @param meta the meta-data to set
	 */
	public DataRows(DataRowMeta meta) {
		setMeta(meta);
	}

	/**
	 * Constructor. Sets the meta-data from the specified JDBC meta-data but
	 * leaves the row list empty.
	 * @param resultSetMetaData the JDBC meta-data to create the row meta-data from
	 * @throws SQLException on SQL errors
	 */
	public DataRows(ResultSetMetaData resultSetMetaData) throws SQLException {
		setMeta(new DataRowMeta(resultSetMetaData));
	}

	/**
	 * Constructor. Sets the meta-data from the specified JDBC result set and
	 * reads all rows from the result set as data using readMoreRows(resultSet).
	 * @param resultSet the JDBC result set to create the row meta-data from and
	 * to read rows from
	 * @param typeConverters the type converters that extract values from the result set.
	 * This array must have the same size as the result set rows.
	 * @throws SQLException on SQL errors
	 */
	public DataRows(ResultSet resultSet, IDataRowTypeConverter[] typeConverters) throws SQLException {
		argumentCheck(resultSet, typeConverters);
		setMeta(new DataRowMeta(resultSet.getMetaData()));
		readMoreRows(resultSet, typeConverters);
	}

	/**
	 * Constructor. Sets the meta-data from the specified JDBC result set and
	 * reads all rows from the result set as data using readMoreRows(resultSet, rowCount).
	 * @param resultSet the JDBC result set to create the row meta-data from and
	 * to read rows from
	 * @param rowCount the maximum number of rows to read
	 * @param typeConverters the type converters that extract values from the result set.
	 * This array must have the same size as the result set rows.
	 * @throws SQLException on SQL errors
	 */
	public DataRows(ResultSet resultSet, IDataRowTypeConverter[] typeConverters, int rowCount) throws SQLException {
		argumentCheck(resultSet, typeConverters);
		setMeta(new DataRowMeta(resultSet.getMetaData()));
		readMoreRows(resultSet, typeConverters, rowCount);
	}
	
	/**
	 * Getter method for the rows.
	 * @return the rows
	 */
	public final List<Object[]> getRows() {
		return rows;
	}

	/**
	 * Setter method for the rows.
	 * @param rows the rows to set
	 */
	public final void setRows(final List<Object[]> rows) {
		this.rows = rows;
	}

	/**
	 * Clears the row list.
	 */
	public final void clearRows() {
		getRows().clear();
	}
	
	/**
	 * Creates a new row list. This is useful in combination with readMoreRows()
	 * to leave the previous list alone.
	 */
	public final void recreateRowList() {
		setRows(new ArrayList<Object[]>());
	}
	
	/**
	 * This method assumes that the meta-data has been created from the
	 * specified result set or is equivalent to that. It does not re-create
	 * the meta-data, just fetch more rows.
	 * 
	 * This method fetches new rows into the current row list without creating
	 * a new list or clearing the list. Use clearRows() to clear the current row
	 * list first, or recreateRowList() to load them into a new list.
	 * 
	 * This method reads all rows available from the result set. See readMoreRows(resultSet, rowCount)
	 * to specify the number of rows to read.
	 * 
	 * @param resultSet the result set to read from
	 * @param typeConverters the type converters that extract values from the result set.
	 * This array must have the same size as the result set rows.
	 * @throws SQLException on SQL errors
	 */
	public final void readMoreRows(ResultSet resultSet, IDataRowTypeConverter[] typeConverters) throws SQLException {
		readMoreRows(resultSet, typeConverters, -1);
	}
	
	/**
	 * This method assumes that the meta-data has been created from the
	 * specified result set or is equivalent to that. It does not re-create
	 * the meta-data, just fetch more rows.
	 * 
	 * This method fetches new rows into the current row list without creating
	 * a new list or clearing the list. Use clearRows() to clear the current row
	 * list first, or recreateRowList() to load them into a new list.
	 * 
	 * This method allows to read either as many rows as possible from the
	 * result set, or limit the number of rows to a specified maximum. If rowCount
	 * is negative, then all rows are fetched from the result set. Otherwise it
	 * is used as a limit and the remaining rows are left in the result set.
	 * 
	 * @param resultSet the result set to read from
	 * @param rowCount the maximum number of rows to fetch
	 * @param typeConverters the type converters that extract values from the result set.
	 * This array must have the same size as the result set rows.
	 * @throws SQLException on SQL errors
	 */
	public final void readMoreRows(ResultSet resultSet, IDataRowTypeConverter[] typeConverters, int rowCount) throws SQLException {
		argumentCheck(resultSet, typeConverters);
		while (rowCount != 0 && resultSet.next()) {
			getRows().add(createDataForCurrentRow(resultSet, typeConverters));
			if (rowCount > 0) {
				rowCount--;
			}
		}
	}

}
