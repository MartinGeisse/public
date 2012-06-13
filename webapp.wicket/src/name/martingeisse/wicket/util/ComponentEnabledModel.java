/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * Simple boolean model for the enabled-ness of a component.
 */
public class ComponentEnabledModel implements IModel<Boolean> {

	/**
	 * the component
	 */
	private Component component;
	
	/**
	 * Constructor.
	 */
	public ComponentEnabledModel() {
	}
	
	/**
	 * Constructor.
	 * @param component the component
	 */
	public ComponentEnabledModel(Component component) {
		this.component = component;
	}

	/**
	 * Getter method for the component.
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * Setter method for the component.
	 * @param component the component to set
	 */
	public void setComponent(Component component) {
		this.component = component;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#getObject()
	 */
	@Override
	public Boolean getObject() {
		return component.isEnabled();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
	 */
	@Override
	public void setObject(Boolean object) {
		component.setEnabled(object);
	}

}
