/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;

/**
 * A simple panel that wraps a {@link CheckBox}.
 */
public class CheckBoxPanel extends AbstractSimpleFormComponentPanel<Boolean, CheckBox> {

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 */
	public CheckBoxPanel(String id) {
		super(id);
		add(new CheckBox(FORM_COMPONENT_ID));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param model the model used by the check box
	 */
	public CheckBoxPanel(String id, IModel<Boolean> model) {
		super(id);
		add(new CheckBox(FORM_COMPONENT_ID, model));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param checkBox the check box to use. Must use the value of {#FORM_COMPONENT_ID} as its Wicket id.
	 */
	public CheckBoxPanel(String id, CheckBox checkBox) {
		super(id);
		add(checkBox);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.append("class", "CheckBoxPanel", " ");
	}
	
}
