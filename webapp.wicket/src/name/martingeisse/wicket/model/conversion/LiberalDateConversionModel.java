/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import name.martingeisse.common.util.DateTimeUtil;

import org.apache.wicket.model.IModel;

/**
 * An {@link AbstractLiberalConversionModel} implementation for type {@link GregorianCalendar}
 * using german date format.
 */
public class LiberalDateConversionModel extends AbstractLiberalConversionModel<GregorianCalendar> {

	/**
	 * Constructor.
	 */
	public LiberalDateConversionModel() {
	}
	
	/**
	 * Constructor.
	 * @param wrappedModel the wrapped model
	 */
	public LiberalDateConversionModel(IModel<GregorianCalendar> wrappedModel) {
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
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
			Date date = format.parse(value);
			return DateTimeUtil.create(date);
		} catch (ParseException e) {
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.");
			Date date = format.parse(value);
			GregorianCalendar result = DateTimeUtil.create(date);
			GregorianCalendar now = new GregorianCalendar();
			result.set(Calendar.YEAR, now.get(Calendar.YEAR));
			return result;
		} catch (ParseException e) {
			throw new IllegalArgumentException("invalid date specification: " + value);
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
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			return format.format(value.getTime());
		}
	}

}
