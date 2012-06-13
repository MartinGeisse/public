/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import name.martingeisse.common.util.DateTimeUtil;

import org.apache.wicket.model.IModel;

/**
 * An {@link AbstractLiberalConversionModel} implementation for type {@link GregorianCalendar}
 * using german time format. Accepts HH:mm:ss format as well as single-digit values and missing
 * seconds.
 */
public class LiberalTimeConversionModel extends AbstractLiberalConversionModel<GregorianCalendar> {

	/**
	 * Constructor.
	 */
	public LiberalTimeConversionModel() {
	}
	
	/**
	 * Constructor.
	 * @param wrappedModel the wrapped model
	 */
	public LiberalTimeConversionModel(IModel<GregorianCalendar> wrappedModel) {
		setWrappedModel(wrappedModel);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.model.AbstractLiberalConversionModel#convertFromString(java.lang.String)
	 */
	@Override
	protected GregorianCalendar convertFromString(String value) throws IllegalArgumentException {
		if (value == null) {
			return null;
		}
		Date date = parseDate("HH:mm:ss", value);
		date = (date == null) ? parseDate("HH:mm", value) : date;
		date = (date == null) ? parseDate("HH", value) : date;
		if (date != null) {
			return DateTimeUtil.create(date);
		}
		throw new IllegalArgumentException("invalid time specification: " + value);
	}
	
	/**
	 * @param format
	 * @return
	 */
	private Date parseDate(String format, String value) {
		try {
			return new SimpleDateFormat(format).parse(value);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.model.AbstractLiberalConversionModel#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(GregorianCalendar value) {
		if (value == null) {
			return null;
		} else {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			return format.format(value.getTime());
		}
	}

}
