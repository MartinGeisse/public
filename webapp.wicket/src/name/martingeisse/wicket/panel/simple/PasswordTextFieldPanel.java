/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A simple {@link Panel} that wraps a {@link PasswordTextField}.
 */
public class PasswordTextFieldPanel extends AbstractSimpleFormComponentPanel<String, PasswordTextField> {

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 */
	public PasswordTextFieldPanel(String id) {
		super(id);
		add(new PasswordTextField(FORM_COMPONENT_ID));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param model the model used by the password text field
	 */
	public PasswordTextFieldPanel(String id, IModel<String> model) {
		super(id);
		add(new PasswordTextField(FORM_COMPONENT_ID, model));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param passwordTextField the password text field to use. Must use the value of {#FORM_COMPONENT_ID} as its Wicket id.
	 */
	public PasswordTextFieldPanel(String id, PasswordTextField passwordTextField) {
		super(id);
		add(passwordTextField);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.append("class", "PasswordTextFieldPanel", " ");
	}
	
}
