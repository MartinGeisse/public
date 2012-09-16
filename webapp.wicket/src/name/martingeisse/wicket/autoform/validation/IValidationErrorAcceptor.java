/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.validation;

import org.apache.wicket.Component;

/**
 * This interface is implemented by the autoform internals to
 * accept the validation errors produced by autoform property
 * components. When creating a property component, this interface
 * must be invoked and the actual form component be passed for
 * which validation error feedback messages will be registered.
 * 
 * The effect of invoking both methods or invoking one method
 * more than once is unspecified.
 */
public interface IValidationErrorAcceptor {

	/**
	 * Accepts validation errors from the specified component.
	 * @param component the component for which validation errors will be registered
	 */
	public void acceptValidationErrorsFrom(Component component);

	/**
	 * Accepts validation errors from the specified components.
	 * @param components the component for which validation errors will be registered
	 */
	public void acceptValidationErrorsFromMultiple(Component... components);

}
