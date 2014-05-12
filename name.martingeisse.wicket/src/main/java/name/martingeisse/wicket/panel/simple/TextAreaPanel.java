/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import name.martingeisse.common.terms.IReadOnlyAware;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A simple {@link Panel} that wraps a {@link TextArea}.
 * @param <T> the model type
 */
public class TextAreaPanel<T> extends AbstractSimpleFormComponentPanel<T, TextArea<T>> implements IReadOnlyAware {

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 */
	public TextAreaPanel(String id) {
		super(id);
		add(new TextArea<T>(FORM_COMPONENT_ID));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param model the model used by the text area
	 */
	public TextAreaPanel(String id, IModel<T> model) {
		super(id);
		add(new TextArea<T>(FORM_COMPONENT_ID, model));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param textArea the text area to use. Must use the value of {#FORM_COMPONENT_ID} as its Wicket id.
	 */
	public TextAreaPanel(String id, TextArea<T> textArea) {
		super(id);
		add(textArea);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.terms.IReadOnlyAware#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return !getFormComponent().isEnabled();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.terms.IReadOnlyAware#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean readOnly) {
		getFormComponent().setEnabled(!readOnly);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.append("class", "TextAreaPanel", " ");
	}

}
