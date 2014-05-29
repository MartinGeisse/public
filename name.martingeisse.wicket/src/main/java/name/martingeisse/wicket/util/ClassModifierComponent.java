/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;

/**
 * A simple component that sets the "class" attribute of the
 * component tag to the stringified value of the component model.
 * Currently only works if the component tag is empty.
 */
public class ClassModifierComponent extends WebComponent {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public ClassModifierComponent(String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public ClassModifierComponent(String id, IModel<?> model) {
		super(id, model);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("class", getDefaultModelObjectAsString());
	}
	
}
