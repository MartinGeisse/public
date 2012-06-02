/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Helper methods to deal with JDBC.
 */
public class JdbcUtil {

	/**
	 * Fetches the number of rows in a table.
	 * @param statement the JDBC statement object to use
	 * @param tableName the name of the table whose size shall be returned
	 * @return the number of rows in the table
	 * @throws SQLException on SQL errors
	 */
	public static int fetchTableSize(Statement statement, String tableName) throws SQLException {
		final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM \"" + tableName + "\"");
		if (!resultSet.next()) {
			throw new RuntimeException("unexpected result set layout for COUNT(*) (no row)");
		}
		if (resultSet.getMetaData().getColumnCount() != 1) {
			throw new RuntimeException("unexpected result set layout for COUNT(*) (number of columns is " + resultSet.getMetaData().getColumnCount() + ")");
		}
		int count = resultSet.getInt(1);
		if (resultSet.next()) {
			throw new RuntimeException("unexpected result set layout for COUNT(*) (more than one row)");
		}
		resultSet.close();
		return count;
	}
	
}
