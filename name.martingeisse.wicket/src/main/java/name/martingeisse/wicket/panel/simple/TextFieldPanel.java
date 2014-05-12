/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A simple {@link Panel} that wraps a {@link TextField}.
 * @param <T> the model type
 */
public class TextFieldPanel<T> extends AbstractSimpleFormComponentPanel<T, TextField<T>> {

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 */
	public TextFieldPanel(String id) {
		super(id);
		add(new TextField<T>(FORM_COMPONENT_ID));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param model the model used by the text field
	 */
	public TextFieldPanel(String id, IModel<T> model) {
		super(id);
		add(new TextField<T>(FORM_COMPONENT_ID, model));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param textField the text field to use. Must use the value of {#FORM_COMPONENT_ID} as its Wicket id.
	 */
	public TextFieldPanel(String id, TextField<T> textField) {
		super(id);
		add(textField);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.append("class", "TextFieldPanel", " ");
	}
	
}
