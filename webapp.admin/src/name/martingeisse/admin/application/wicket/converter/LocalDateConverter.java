/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Converts Joda {@link LocalDate} objects.
 */
public class LocalDateConverter implements IConverter<LocalDate> {

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String, java.util.Locale)
	 */
	@Override
	public LocalDate convertToObject(String value, Locale locale) {
		try {
			DateTimeFormatter formatter = getFormatter(locale);
			return formatter.parseLocalDate(value);
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object, java.util.Locale)
	 */
	@Override
	public String convertToString(LocalDate value, Locale locale) {
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
		return DateTimeFormat.mediumDate().withLocale(locale);
	}
	
}
