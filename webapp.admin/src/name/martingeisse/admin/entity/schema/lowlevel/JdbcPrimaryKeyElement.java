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
 * An element of the primary key of a table.
 */
public final class JdbcPrimaryKeyElement {

	/**
	 * the selector
	 */
	private final JdbcColumnSelector selector;

	/**
	 * the sequenceNumber
	 */
	private final int sequenceNumber;

	/**
	 * Constructor that takes primary key element information from the current row of the
	 * specified result set. The result set should use the format specified for
	 * {@link DatabaseMetaData#getPrimaryKeys(String, String, String)}.
	 * 
	 * @param resultSet the result set
	 * @throws SQLException on SQL errors
	 */
	public JdbcPrimaryKeyElement(final ResultSet resultSet) throws SQLException {
		this.selector = new JdbcColumnSelector(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
		this.sequenceNumber = resultSet.getInt(5);
	}

	/**
	 * Getter method for the selector.
	 * @return the selector
	 */
	public JdbcColumnSelector getSelector() {
		return selector;
	}

	/**
	 * Getter method for the sequenceNumber.
	 * @return the sequenceNumber
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}

}
