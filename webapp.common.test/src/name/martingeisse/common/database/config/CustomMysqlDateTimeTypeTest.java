/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import java.sql.Types;

import org.junit.Test;

/**
 * Test class for {@link CustomMysqlDateTimeType}.
 */
public class CustomMysqlDateTimeTypeTest {

	/**
	 * @throws Exception on errors
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullTimeZoneException() throws Exception {
		new CustomMysqlDateTimeType(null);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullTimeZoneExceptionWithSqlType() throws Exception {
		new CustomMysqlDateTimeType(Types.TIMESTAMP, null);
	}
	
}
