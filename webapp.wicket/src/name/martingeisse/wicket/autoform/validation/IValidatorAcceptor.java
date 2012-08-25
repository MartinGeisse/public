/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.validation;

import name.martingeisse.wicket.autoform.annotation.components.AutoformComponent;
import name.martingeisse.wicket.autoform.annotation.validation.AutoformValidator;

import org.apache.wicket.validation.IValidator;

/**
 * This interface must be implemented by user-specified components
 * (using {@link AutoformComponent}) which have {@link AutoformValidator}
 * or other validation annotations. It is used to pass the validators
 * to the component. For user-specified components without validation
 * annotations this interface is optional and its method not called.
 */
public interface IValidatorAcceptor {

	/**
	 * Accepts the specified validators. The effect of calling this method
	 * more than once for the same acceptor is unspecified.
	 * @param validators the validators to accept
	 * @param validationErrorAcceptor the acceptor for validation errors produced by this validator acceptor
	 */
	public void acceptValidators(IValidator<?>[] validators, IValidationErrorAcceptor validationErrorAcceptor);
	
}
