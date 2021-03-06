/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Converts Joda {@link LocalDateTime} objects.
 */
public class LocalDateTimeConverter implements IConverter<LocalDateTime> {

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String, java.util.Locale)
	 */
	@Override
	public LocalDateTime convertToObject(String value, Locale locale) {
		try {
			DateTimeFormatter formatter = getFormatter(locale);
			return formatter.parseLocalDateTime(value);
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object, java.util.Locale)
	 */
	@Override
	public String convertToString(LocalDateTime value, Locale locale) {
		try {
			DateTimeFormatter formatter = getFormatter(locale);
			return formatter.print(value);
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

	/**
	 * Obtains the formatter to use for the specified locale.
	 */
	private static DateTimeFormatter getFormatter(Locale locale) {
		return DateTimeFormat.mediumDateTime().withLocale(locale);
	}
	
}
