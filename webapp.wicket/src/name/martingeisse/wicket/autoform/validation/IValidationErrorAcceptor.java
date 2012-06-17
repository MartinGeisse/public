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
 */
public interface IValidationErrorAcceptor {

	/**
	 * Accepts validation errors from the specified component. The effect
	 * of invoking this method more than once for a single acceptor is
	 * unspecified.
	 * @param component the component for which validation errors will be registered
	 */
	public void acceptValidationErrorsFrom(Component component);
	
}
