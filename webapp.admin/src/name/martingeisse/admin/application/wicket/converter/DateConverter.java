/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.DateMidnight;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Converts Joda {@link DateMidnight} objects.
 */
public class DateConverter implements IConverter<DateMidnight> {

	/**
	 * the formatter
	 */
	private static final DateTimeFormatter formatter = DateTimeFormat.mediumDate().withLocale(Locale.GERMAN);
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String, java.util.Locale)
	 */
	@Override
	public DateMidnight convertToObject(String value, Locale locale) {
		try {
			return formatter.parseDateTime(value).toDateMidnight();
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object, java.util.Locale)
	 */
	@Override
	public String convertToString(DateMidnight value, Locale locale) {
		try {
			return formatter.print(value);
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

}
