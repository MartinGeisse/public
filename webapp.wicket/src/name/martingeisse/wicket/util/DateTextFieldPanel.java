/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import java.util.GregorianCalendar;

import name.martingeisse.wicket.model.conversion.LiberalDateConversionModel;
import name.martingeisse.wicket.panel.simple.TextFieldPanel;

import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

/**
 * A specialized {@link TextFieldPanel} using a {@link LiberalDateConversionModel}.
 */
public class DateTextFieldPanel extends TextFieldPanel<String> implements IHeaderContributor {

	/**
	 * the dateModel
	 */
	private final IModel<GregorianCalendar> dateModel;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param dateModel the model
	 */
	public DateTextFieldPanel(final String id, final IModel<GregorianCalendar> dateModel) {
		super(id, new LiberalDateConversionModel(dateModel));
		this.dateModel = dateModel;
		getTextField().setOutputMarkupId(true);
	}

	/**
	 * Getter method for the dateModel.
	 * @return the dateModel
	 */
	public IModel<GregorianCalendar> getDateModel() {
		return dateModel;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.IHeaderContributor#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		String markupId = getTextField().getMarkupId();
		response.renderOnDomReadyJavaScript("$('#" + markupId + "').datepicker({ dateFormat: 'd.m.yy' });");
	}

}
