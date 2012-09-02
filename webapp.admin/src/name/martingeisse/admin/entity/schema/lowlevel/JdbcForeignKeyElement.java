/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.lowlevel;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Stores information about an element of a JDBC foreign key.
 */
public final class JdbcForeignKeyElement {

	/**
	 * the parentColumn
	 */
	private final JdbcColumnSelector parentColumn;

	/**
	 * the foreignColumn
	 */
	private final JdbcColumnSelector foreignColumn;

	/**
	 * the elementSequenceNumber
	 */
	private final int elementSequenceNumber;

	/**
	 * the foreignKeyName
	 */
	private final String foreignKeyName;

	/**
	 * the primaryKeyName
	 */
	private final String primaryKeyName;

	/**
	 * Constructor that takes foreign key information from the current row of the
	 * specified result set. The result set should use the format specified for
	 * {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)}.
	 * 
	 * @param resultSet the result set
	 * @throws SQLException on SQL errors
	 */
	public JdbcForeignKeyElement(final ResultSet resultSet) throws SQLException {
		this.parentColumn = new JdbcColumnSelector(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
		this.foreignColumn = new JdbcColumnSelector(resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8));
		this.elementSequenceNumber = resultSet.getInt(9);
		this.foreignKeyName = resultSet.getString(12);
		this.primaryKeyName = resultSet.getString(13);
	}

	/**
	 * Getter method for the parentColumn.
	 * @return the parentColumn
	 */
	public JdbcColumnSelector getParentColumn() {
		return parentColumn;
	}
	
	/**
	 * Getter method for the foreignColumn.
	 * @return the foreignColumn
	 */
	public JdbcColumnSelector getForeignColumn() {
		return foreignColumn;
	}

	/**
	 * Getter method for the elementSequenceNumber.
	 * @return the elementSequenceNumber
	 */
	public int getElementSequenceNumber() {
		return elementSequenceNumber;
	}

	/**
	 * Getter method for the foreignKeyName.
	 * @return the foreignKeyName
	 */
	public String getForeignKeyName() {
		return foreignKeyName;
	}

	/**
	 * Getter method for the primaryKeyName.
	 * @return the primaryKeyName
	 */
	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

}
