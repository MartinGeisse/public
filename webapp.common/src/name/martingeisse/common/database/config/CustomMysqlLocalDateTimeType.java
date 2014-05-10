/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import java.sql.Types;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;

/**
 * Custom type implementation for Joda {@link LocalDateTime}.
 * See {@link CustomMysqlQuerydslConfiguration} for more information.
 */
public class CustomMysqlLocalDateTimeType extends AbstractCustomJodaType<LocalDateTime> {

	/**
	 * Constructor.
	 */
	public CustomMysqlLocalDateTimeType() {
		this(Types.TIMESTAMP);
	}

	/**
	 * Constructor.
	 * @param type the SQL type
	 */
	public CustomMysqlLocalDateTimeType(final int type) {
		super(type, null, "yyyy-MM-dd HH:mm:ss.S", LocalDateTime.class);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.config.AbstractCustomJodaType#print(org.joda.time.format.DateTimeFormatter, java.lang.Object)
	 */
	@Override
	protected String print(final DateTimeFormatter formatter, final LocalDateTime value) {
		return formatter.print(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.config.AbstractCustomJodaType#parse(org.joda.time.format.DateTimeFormatter, java.lang.String)
	 */
	@Override
	protected LocalDateTime parse(final DateTimeFormatter formatter, final String value) {
		return formatter.parseLocalDateTime(value);
	}

}
