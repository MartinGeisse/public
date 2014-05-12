/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;

/**
 * This component acts as a no-op component with an implicit attribute modifier.
 */
public class AttributeModifierComponent extends WebComponent {

	/**
	 * the attributeName
	 */
	private String attributeName;

	/**
	 * Constructor.
	 * @param id the id of this component
	 * @param attributeName the name of the attribute to change
	 * @param attributeValueModel the model for the new value of the attribute
	 */
	public AttributeModifierComponent(final String id, final String attributeName, final IModel<?> attributeValueModel) {
		super(id, attributeValueModel);
		this.attributeName = attributeName;
	}

	/**
	 * Getter method for the attributeName.
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Setter method for the attributeName.
	 * @param attributeName the attributeName to set
	 */
	public void setAttributeName(final String attributeName) {
		this.attributeName = attributeName;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put(attributeName, getDefaultModelObjectAsString());
	}
	
}
