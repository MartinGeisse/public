/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.converter;

import java.util.Locale;

import name.martingeisse.wicket.application.MyWicketSession;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Converts Joda {@link DateTime} objects.
 */
public class DateTimeConverter implements IConverter<DateTime> {

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String, java.util.Locale)
	 */
	@Override
	public DateTime convertToObject(String value, Locale locale) {
		try {
			DateTimeFormatter formatter = getFormatter(locale);
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
			DateTimeFormatter formatter = getFormatter(locale);
			return formatter.print(value);
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}
	
	/**
	 * Obtains the formatter to use for the specified locale and current Wicket session.
	 */
	private static DateTimeFormatter getFormatter(Locale locale) {
		return DateTimeFormat.mediumDateTime().withLocale(locale).withZone(getTimeZone());
	}
	
	/**
	 * Obtains the time zone to use for conversion. This is taken from the current
	 * Wicket session. There is no fallback to avoid hiding errors.
	 */
	private static DateTimeZone getTimeZone() {
		MyWicketSession session = MyWicketSession.get();
		if (session == null) {
			throw new IllegalStateException("no session -- cannot determine time zone for conversion");
		}
		DateTimeZone timeZone = session.getTimeZone();
		if (timeZone == null) {
			throw new IllegalStateException("session time zone is null");
		}
		return timeZone;
	}
	
}
