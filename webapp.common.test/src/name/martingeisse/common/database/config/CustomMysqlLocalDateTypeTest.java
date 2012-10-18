/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Types;

import org.joda.time.LocalDate;
import org.junit.Test;

/**
 * Test class for {@link CustomMysqlLocalDateType}.
 */
public class CustomMysqlLocalDateTypeTest extends AbstractCustomJodaTypeTest {

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testConstructors() throws Exception {
		new CustomMysqlLocalDateType();
		new CustomMysqlLocalDateType(Types.DATE);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testGetSqlType() throws Exception {
		CustomMysqlLocalDateType type = new CustomMysqlLocalDateType();
		assertEquals(1, type.getSQLTypes().length);
		assertEquals(Types.DATE, type.getSQLTypes()[0]);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testGetValueNull() throws Exception {
		MyResultSet resultSet = new MyResultSet(null);
		CustomMysqlLocalDateType type = new CustomMysqlLocalDateType();
		assertNull(type.getValue(resultSet, 42));
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testGetValue() throws Exception {
		MyResultSet resultSet = new MyResultSet("2012-10-02");
		CustomMysqlLocalDateType type = new CustomMysqlLocalDateType();
		LocalDate result = type.getValue(resultSet, 42);
		assertEquals(2012, result.getYear());
		assertEquals(10, result.getMonthOfYear());
		assertEquals(2, result.getDayOfMonth());
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testSetValueNull() throws Exception {
		MyPreparedStatement statement = new MyPreparedStatement();
		statement.value = "xxx";
		CustomMysqlLocalDateType type = new CustomMysqlLocalDateType();
		type.setValue(statement, 42, null);
		assertNull(statement.value);
		assertEquals(Types.DATE, statement.type);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testSetValueWithSameTimeZone() throws Exception {
		MyPreparedStatement statement = new MyPreparedStatement();
		statement.value = "xxx";
		CustomMysqlLocalDateType type = new CustomMysqlLocalDateType();
		LocalDate dateTime = new LocalDate(2011, 7, 12);
		type.setValue(statement, 42, dateTime);
		assertEquals("2011-07-12", statement.value);
	}
	
}
