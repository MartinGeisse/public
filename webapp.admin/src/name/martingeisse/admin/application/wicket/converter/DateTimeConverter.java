/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Converts Joda {@link DateTime} objects.
 */
public class DateTimeConverter implements IConverter<DateTime> {

	/**
	 * the formatter
	 */
	private static final DateTimeFormatter formatter = DateTimeFormat.mediumDateTime().withLocale(Locale.GERMAN);
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String, java.util.Locale)
	 */
	@Override
	public DateTime convertToObject(String value, Locale locale) {
		try {
			return formatter.parseDateTime(value);
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object, java.util.Locale)
	 */
	@Override
	public String convertToString(DateTime value, Locale locale) {
		try {
			return formatter.print(value);
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

}
