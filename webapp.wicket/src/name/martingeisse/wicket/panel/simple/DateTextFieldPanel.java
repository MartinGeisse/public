/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;


import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

/**
 * A specialized {@link TextFieldPanel} that adds a date picker.
 * @param <T> the model type
 */
public class DateTextFieldPanel<T> extends TextFieldPanel<T> implements IHeaderContributor {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public DateTextFieldPanel(final String id, final IModel<T> model) {
		super(id, model);
		getTextField().setOutputMarkupId(true);
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
