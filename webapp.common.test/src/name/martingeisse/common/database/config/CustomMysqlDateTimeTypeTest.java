/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Types;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Test class for {@link CustomMysqlDateTimeType}.
 */
public class CustomMysqlDateTimeTypeTest extends AbstractCustomJodaTypeTest {

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
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testGetSqlType() throws Exception {
		CustomMysqlDateTimeType type = new CustomMysqlDateTimeType(DateTimeZone.UTC);
		assertEquals(1, type.getSQLTypes().length);
		assertEquals(Types.TIMESTAMP, type.getSQLTypes()[0]);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testGetValueNull() throws Exception {
		MyResultSet resultSet = new MyResultSet(null);
		CustomMysqlDateTimeType type = new CustomMysqlDateTimeType(DateTimeZone.forID("Europe/Moscow"));
		assertNull(type.getValue(resultSet, 42));
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testGetValue() throws Exception {
		MyResultSet resultSet = new MyResultSet("2012-10-02 11:22:33.0");
		CustomMysqlDateTimeType type = new CustomMysqlDateTimeType(DateTimeZone.forID("Europe/Moscow"));
		DateTime result = type.getValue(resultSet, 42);
		assertEquals(2012, result.getYear());
		assertEquals(10, result.getMonthOfYear());
		assertEquals(2, result.getDayOfMonth());
		assertEquals(11, result.getHourOfDay());
		assertEquals(22, result.getMinuteOfHour());
		assertEquals(33, result.getSecondOfMinute());
		assertEquals("Europe/Moscow", result.getZone().getID());
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testSetValueNull() throws Exception {
		MyPreparedStatement statement = new MyPreparedStatement();
		statement.value = "xxx";
		CustomMysqlDateTimeType type = new CustomMysqlDateTimeType(DateTimeZone.forID("Europe/Moscow"));
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
		CustomMysqlDateTimeType type = new CustomMysqlDateTimeType(DateTimeZone.forID("Europe/Moscow"));
		DateTime dateTime = new DateTime(2011, 7, 12, 17, 3, 55, DateTimeZone.forID("Europe/Moscow"));
		type.setValue(statement, 42, dateTime);
		assertEquals("2011-07-12 17:03:55.0", statement.value);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testSetValueWithDifferentTimeZone() throws Exception {
		MyPreparedStatement statement = new MyPreparedStatement();
		statement.value = "xxx";
		CustomMysqlDateTimeType type = new CustomMysqlDateTimeType(DateTimeZone.forID("Europe/Moscow"));
		DateTime dateTime = new DateTime(2011, 7, 12, 17, 3, 55, DateTimeZone.forID("Europe/Berlin"));
		type.setValue(statement, 42, dateTime);
		assertEquals("2011-07-12 19:03:55.0", statement.value);
	}
	
}
