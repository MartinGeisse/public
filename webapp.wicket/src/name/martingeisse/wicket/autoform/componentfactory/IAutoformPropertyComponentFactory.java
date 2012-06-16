/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.componentfactory;

import java.io.Serializable;

import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription;

import org.apache.wicket.Component;

/**
 * Implementations are able to create Wicket components for autoform properties.
 * Components are attached to an empty DIV tag and are usually Wicket panels,
 * although any component that accepts such a tag will do.
 * 
 * The full {@link IAutoformPropertyDescription} is passed to this factory
 * to allow customization based on annotations.
 */
public interface IAutoformPropertyComponentFactory extends Serializable {

	/**
	 * Creates a property component for the specified property.
	 * @param id the wicket id
	 * @param propertyDescription the property description of the property
	 * @return the component
	 */
	public Component createPropertyComponent(String id, IAutoformPropertyDescription propertyDescription);
	
}
