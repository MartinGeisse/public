/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.io.Serializable;

/**
 * This interface provides a method to resolve the message key of a
 * validation error to the message template.
 */
public interface IValidationErrorMessageResolver extends Serializable {

	/**
	 * Resolves the specified validation error message.
	 * @param messageKey the message key
	 * @return the message template
	 */
	public String resolveValidationErrorMessage(String messageKey);
	
}
