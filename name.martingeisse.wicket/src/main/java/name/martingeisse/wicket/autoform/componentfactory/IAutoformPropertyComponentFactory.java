/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.componentfactory;

import java.io.Serializable;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescriptor;
import name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor;
import org.apache.wicket.Component;
import org.apache.wicket.validation.IValidator;

/**
 * Implementations are able to create Wicket components for autoform properties.
 * Components are attached to an empty DIV tag and are usually Wicket panels,
 * although any component that accepts such a tag will do.
 * 
 * The full {@link IAutoformPropertyDescriptor} is passed to this factory
 * to allow customization based on annotations.
 */
public interface IAutoformPropertyComponentFactory extends Serializable {

	/**
	 * Creates a property component for the specified property.
	 * @param id the wicket id
	 * @param propertyDescriptor the property descriptor of the property
	 * @param validators the validators to use for the component
	 * @param validationErrorAcceptor the acceptor for validation errors produced by the component
	 * @return the component
	 */
	public Component createPropertyComponent(String id, IAutoformPropertyDescriptor propertyDescriptor, IValidator<?>[] validators, IValidationErrorAcceptor validationErrorAcceptor);
	
}
