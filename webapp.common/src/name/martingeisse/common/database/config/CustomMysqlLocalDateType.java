/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import java.sql.Types;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

/**
 * Custom type implementation for Joda {@link LocalDate}.
 * See {@link CustomMysqlQuerydslConfiguration} for more information.
 */
public class CustomMysqlLocalDateType extends AbstractCustomJodaType<LocalDate> {

	/**
	 * Constructor.
	 */
	public CustomMysqlLocalDateType() {
		this(Types.DATE);
	}

	/**
	 * Constructor.
	 * @param type the SQL type
	 */
	public CustomMysqlLocalDateType(final int type) {
		super(type, null, "yyyy-MM-dd", LocalDate.class);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.config.AbstractCustomJodaType#print(org.joda.time.format.DateTimeFormatter, java.lang.Object)
	 */
	@Override
	protected String print(final DateTimeFormatter formatter, final LocalDate value) {
		return formatter.print(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.config.AbstractCustomJodaType#parse(org.joda.time.format.DateTimeFormatter, java.lang.String)
	 */
	@Override
	protected LocalDate parse(final DateTimeFormatter formatter, final String value) {
		return formatter.parseLocalDate(value);
	}

}
