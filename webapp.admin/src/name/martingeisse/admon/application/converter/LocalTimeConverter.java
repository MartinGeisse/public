/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admon.application.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Converts Joda {@link LocalTime} objects.
 */
public class LocalTimeConverter implements IConverter<LocalTime> {

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String, java.util.Locale)
	 */
	@Override
	public LocalTime convertToObject(String value, Locale locale) {
		try {
			DateTimeFormatter formatter = getFormatter(locale);
			return formatter.parseLocalTime(value);
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object, java.util.Locale)
	 */
	@Override
	public String convertToString(LocalTime value, Locale locale) {
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
		return DateTimeFormat.mediumTime().withLocale(locale);
	}
	
}
