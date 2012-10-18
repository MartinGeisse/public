/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import java.sql.Types;

import name.martingeisse.common.util.ParameterUtil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;

/**
 * Custom type implementation for Joda {@link DateTime}. This type requires
 * a default time zone to convert the partials from the database
 * to instants. See {@link CustomMysqlQuerydslConfiguration} for
 * more information.
 */
public class CustomMysqlDateTimeType extends AbstractCustomJodaType<DateTime> {

	/**
	 * Constructor.
	 * @param timeZone the default time zone, used to create instants from partial datetimes
	 */
	public CustomMysqlDateTimeType(final DateTimeZone timeZone) {
		this(Types.TIMESTAMP, timeZone);
	}

	/**
	 * Constructor.
	 * @param type the SQL type
	 * @param timeZone the default time zone, used to create instants from partial datetimes
	 */
	public CustomMysqlDateTimeType(final int type, final DateTimeZone timeZone) {
		super(type, ParameterUtil.ensureNotNull(timeZone, "timeZone"), "yyyy-MM-dd HH:mm:ss.S", DateTime.class);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.config.AbstractCustomJodaType#print(org.joda.time.format.DateTimeFormatter, java.lang.Object)
	 */
	@Override
	protected String print(final DateTimeFormatter formatter, final DateTime value) {
		return formatter.print(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.config.AbstractCustomJodaType#parse(org.joda.time.format.DateTimeFormatter, java.lang.String)
	 */
	@Override
	protected DateTime parse(final DateTimeFormatter formatter, final String value) {
		return formatter.parseDateTime(value);
	}

}
