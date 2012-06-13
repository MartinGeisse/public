/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.io.Serializable;

import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription;

import org.apache.wicket.Component;

/**
 * Implementations are able to create Wicket components for autoform properties.
 * Each component must be a Wicket Panel (or subclass) instance since property
 * components must bring their own markup.
 */
public interface IAutoformPropertyComponentFactory extends Serializable {

	/**
	 * Creates a property component for the specified property.
	 * @param id the wicket id
	 * @param propertyDescriptor the property descriptor of the property
	 * @return the component
	 */
	public Component createPropertyComponent(String id, IAutoformPropertyDescription propertyDescriptor);
	
}
