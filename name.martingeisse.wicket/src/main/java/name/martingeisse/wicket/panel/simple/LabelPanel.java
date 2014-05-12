/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A simple panel that wraps a {@link Label}.
 * 
 * The wrapped label always uses the Wicket id "wrapped"
 * (also available as class constant LABEL_ID).
 */
public class LabelPanel extends Panel implements IFormComponentPanel<String> {

	/**
	 * The Wicket id of the wrapped label.
	 */
	public static final String LABEL_ID = "wrapped";

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 */
	public LabelPanel(String id) {
		this(id, new Label(LABEL_ID));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param model the model used by the label
	 */
	public LabelPanel(String id, IModel<?> model) {
		this(id, new Label(LABEL_ID, model));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param label the label to use. Must use the value of {#LABEL_ID} as its Wicket id.
	 */
	public LabelPanel(String id, Label label) {
		super(id);
		add(label);
	}

	/**
	 * Getter method for the label.
	 * @return the label
	 */
	public final Label getLabel() {
		return (Label)get(LABEL_ID);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#getRootComponent()
	 */
	@Override
	public Component getRootComponent() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#getFormComponent()
	 */
	@Override
	public FormComponent<String> getFormComponent() {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#connectValidationErrorAcceptor(name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor)
	 */
	@Override
	public void connectValidationErrorAcceptor(IValidationErrorAcceptor validationErrorAcceptor) {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.append("class", "LabelPanel", " ");
	}
	
}
