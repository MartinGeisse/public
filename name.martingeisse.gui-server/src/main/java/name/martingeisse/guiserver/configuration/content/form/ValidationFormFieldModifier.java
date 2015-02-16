/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.form;

import org.apache.wicket.validation.IValidator;

/**
 * Wraps an {@link IValidator} as a {@link FormFieldModifier}.
 */
public final class ValidationFormFieldModifier implements FormFieldModifier {

	/**
	 * the validator
	 */
	private final IValidator<?> validator;

	/**
	 * Constructor.
	 * @param validator the validator
	 */
	public ValidationFormFieldModifier(IValidator<?> validator) {
		this.validator = validator;
	}

	/**
	 * Getter method for the validator.
	 * @return the validator
	 */
	public IValidator<?> getValidator() {
		return validator;
	}
	
}
