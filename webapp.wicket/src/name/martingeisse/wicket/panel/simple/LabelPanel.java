/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A simple {@link Panel} that wraps a {@link Label}.
 */
public class LabelPanel extends Panel implements IFormComponentPanel<String> {

	/**
	 * the label
	 */
	private final Label label;

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 */
	public LabelPanel(String id) {
		this(id, new Label(getLabelId()));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param model the model used by the label
	 */
	public LabelPanel(String id, IModel<?> model) {
		this(id, new Label(getLabelId(), model));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param label the label to use. Must use the wicket:id returned by getLabelId().
	 */
	public LabelPanel(String id, Label label) {
		super(id);
		this.label = label;
		add(label);
	}

	/**
	 * @return the wicket:id that the wrapped label must use.
	 */
	public static String getLabelId() {
		return "wrapped";
	}
	
	/**
	 * Getter method for the label.
	 * @return the label
	 */
	public final Label getLabel() {
		return label;
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
	
}
