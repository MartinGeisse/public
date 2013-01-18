/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.sql.Types;

import name.martingeisse.test.PreparedStatementDouble;
import name.martingeisse.test.ResultSetDouble;

/**
 *
 */
public abstract class AbstractCustomJodaTypeTest {

	/**
	 *
	 */
	static class MyResultSet extends ResultSetDouble {
		
		/**
		 * the value
		 */
		String value;
		
		/**
		 * Constructor.
		 */
		MyResultSet(String value) {
			this.value = value;
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.common.util.test.ResultSetDouble#getString(int)
		 */
		@Override
		public String getString(int columnIndex) throws SQLException {
			assertEquals(42, columnIndex);
			return value;
		}
		
	}
	
	/**
	 *
	 */
	static class MyPreparedStatement extends PreparedStatementDouble {

		/**
		 * the type
		 */
		int type;
		
		/**
		 * the value
		 */
		String value;
		
		/* (non-Javadoc)
		 * @see name.martingeisse.common.util.test.PreparedStatementDouble#setNull(int, int)
		 */
		@Override
		public void setNull(int parameterIndex, int sqlType) throws SQLException {
			assertEquals(42, parameterIndex);
			this.type = sqlType;
			this.value = null;
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.common.util.test.PreparedStatementDouble#setString(int, java.lang.String)
		 */
		@Override
		public void setString(int parameterIndex, String x) throws SQLException {
			assertEquals(42, parameterIndex);
			this.type = Types.OTHER;
			this.value = x;
		}
		
	}
	
}
