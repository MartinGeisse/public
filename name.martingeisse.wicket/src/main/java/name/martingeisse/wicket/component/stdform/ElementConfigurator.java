/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.stdform;

import org.apache.wicket.Component;

/**
 * This class can be used to configure special features of a form
 * element after it has been created. Subclasses are provided for
 * specific form elements.
 */
public class ElementConfigurator {
	
	/**
	 * the rawElementComponent
	 */
	private final Component rawElementComponent;

	/**
	 * Constructor.
	 * @param rawElementComponent the component that represents the raw form element
	 */
	public ElementConfigurator(Component rawElementComponent) {
		this.rawElementComponent = rawElementComponent;
	}

	/**
	 * Getter method for the rawElementComponent.
	 * @return the rawElementComponent
	 */
	public Component getRawElementComponent() {
		return rawElementComponent;
	}
	
}
