/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A simple {@link Panel} that wraps a {@link PasswordTextField}.
 */
public class PasswordTextFieldPanel extends Panel implements IFormComponentPanel<String> {

	/**
	 * the passwordTextField
	 */
	private final PasswordTextField passwordTextField;

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 */
	public PasswordTextFieldPanel(String id) {
		this(id, new PasswordTextField(getPasswordTextFieldId()));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param model the model used by the password text field
	 */
	public PasswordTextFieldPanel(String id, IModel<String> model) {
		this(id, new PasswordTextField(getPasswordTextFieldId(), model));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param passwordTextField the password text field to use. Must use the wicket:id returned by getPasswordTextFieldId().
	 */
	public PasswordTextFieldPanel(String id, PasswordTextField passwordTextField) {
		super(id);
		this.passwordTextField = passwordTextField;
		add(passwordTextField);
		passwordTextField.setRequired(false);
	}

	/**
	 * @return the wicket:id that the wrapped password text field must use.
	 */
	public static String getPasswordTextFieldId() {
		return "wrapped";
	}
	
	/**
	 * Getter method for the passwordTextField.
	 * @return the passwordTextField
	 */
	public final PasswordTextField getPasswordTextField() {
		return passwordTextField;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#getPanel()
	 */
	@Override
	public Panel getPanel() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#getFormComponent()
	 */
	@Override
	public FormComponent<String> getFormComponent() {
		return passwordTextField;
	}
	
}
