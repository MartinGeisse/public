/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A simple {@link Panel} that wraps a {@link CheckBox}.
 */
public class CheckBoxPanel extends Panel implements IFormComponentPanel<Boolean> {

	/**
	 * the checkBox
	 */
	private final CheckBox checkBox;

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 */
	public CheckBoxPanel(String id) {
		this(id, new CheckBox(getCheckBoxId()));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param model the model used by the check box
	 */
	public CheckBoxPanel(String id, IModel<Boolean> model) {
		this(id, new CheckBox(getCheckBoxId(), model));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param checkBox the check box to use. Must use the wicket:id returned by getCheckBoxId().
	 */
	public CheckBoxPanel(String id, CheckBox checkBox) {
		super(id);
		this.checkBox = checkBox;
		add(checkBox);
	}

	/**
	 * @return the wicket:id that the wrapped check box must use.
	 */
	public static String getCheckBoxId() {
		return "wrapped";
	}

	/**
	 * Getter method for the checkBox.
	 * @return the checkBox
	 */
	public CheckBox getCheckBox() {
		return checkBox;
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
	public FormComponent<Boolean> getFormComponent() {
		return checkBox;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#connectValidationErrorAcceptor(name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor)
	 */
	@Override
	public void connectValidationErrorAcceptor(IValidationErrorAcceptor validationErrorAcceptor) {
		validationErrorAcceptor.acceptValidationErrorsFrom(checkBox);
	}
	
}
