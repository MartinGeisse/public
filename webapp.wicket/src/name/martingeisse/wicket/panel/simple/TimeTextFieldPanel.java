/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import java.util.GregorianCalendar;

import name.martingeisse.wicket.model.conversion.LiberalTimeConversionModel;

import org.apache.wicket.model.IModel;

/**
 * A specialized {@link TextFieldPanel} using a {@link LiberalTimeConversionModel}.
 */
public class TimeTextFieldPanel extends TextFieldPanel<String> {

	/**
	 * the timeModel
	 */
	private final IModel<GregorianCalendar> timeModel;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param timeModel the model
	 */
	public TimeTextFieldPanel(final String id, final IModel<GregorianCalendar> timeModel) {
		super(id, new LiberalTimeConversionModel(timeModel));
		this.timeModel = timeModel;
	}

	/**
	 * Getter method for the timeModel.
	 * @return the timeModel
	 */
	public IModel<GregorianCalendar> getTimeModel() {
		return timeModel;
	}

}
