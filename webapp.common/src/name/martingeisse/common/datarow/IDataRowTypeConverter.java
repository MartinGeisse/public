/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.datarow;

import java.sql.ResultSet;
import java.sql.SQLException;
import name.martingeisse.common.database.IDatabaseDescriptor;

/**
 * Used for type conversion when creating a {@link DataRow} or
 * {@link DataRows} object from a JDBC {@link ResultSet}.
 */
public interface IDataRowTypeConverter {

	/**
	 * Reads a value of this type from the specified result set.
	 * 
	 * @param resultSet the result set
	 * @param index the index to read from
	 * @param databaseDescriptor the database descriptor from which the result set was read
	 * @return the value
	 * @throws SQLException on SQL errors
	 */
	public Object readFromResultSet(ResultSet resultSet, int index, IDatabaseDescriptor databaseDescriptor) throws SQLException;

}
