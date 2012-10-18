/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Types;

import org.joda.time.LocalDateTime;
import org.junit.Test;

/**
 * Test class for {@link CustomMysqlLocalDateTimeType}.
 */
public class CustomMysqlLocalDateTimeTypeTest extends AbstractCustomJodaTypeTest {

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testConstructors() throws Exception {
		new CustomMysqlLocalDateTimeType();
		new CustomMysqlLocalDateTimeType(Types.TIMESTAMP);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testGetSqlType() throws Exception {
		CustomMysqlLocalDateTimeType type = new CustomMysqlLocalDateTimeType();
		assertEquals(1, type.getSQLTypes().length);
		assertEquals(Types.TIMESTAMP, type.getSQLTypes()[0]);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testGetValueNull() throws Exception {
		MyResultSet resultSet = new MyResultSet(null);
		CustomMysqlLocalDateTimeType type = new CustomMysqlLocalDateTimeType();
		assertNull(type.getValue(resultSet, 42));
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testGetValue() throws Exception {
		MyResultSet resultSet = new MyResultSet("2012-10-02 11:22:33.0");
		CustomMysqlLocalDateTimeType type = new CustomMysqlLocalDateTimeType();
		LocalDateTime result = type.getValue(resultSet, 42);
		assertEquals(2012, result.getYear());
		assertEquals(10, result.getMonthOfYear());
		assertEquals(2, result.getDayOfMonth());
		assertEquals(11, result.getHourOfDay());
		assertEquals(22, result.getMinuteOfHour());
		assertEquals(33, result.getSecondOfMinute());
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testSetValueNull() throws Exception {
		MyPreparedStatement statement = new MyPreparedStatement();
		statement.value = "xxx";
		CustomMysqlLocalDateTimeType type = new CustomMysqlLocalDateTimeType();
		type.setValue(statement, 42, null);
		assertNull(statement.value);
		assertEquals(Types.TIMESTAMP, statement.type);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testSetValueWithSameTimeZone() throws Exception {
		MyPreparedStatement statement = new MyPreparedStatement();
		statement.value = "xxx";
		CustomMysqlLocalDateTimeType type = new CustomMysqlLocalDateTimeType();
		LocalDateTime dateTime = new LocalDateTime(2011, 7, 12, 17, 3, 55);
		type.setValue(statement, 42, dateTime);
		assertEquals("2011-07-12 17:03:55.0", statement.value);
	}
	
}
