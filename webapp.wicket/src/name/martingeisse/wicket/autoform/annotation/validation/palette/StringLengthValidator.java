/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.validation.palette;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * Common implementation for {@link ExactStringLength}, {@link MinStringLength}
 * and {@link MaxStringLength} validation.
 */
public class StringLengthValidator extends Behavior implements IValidator<String>, IClusterable {

	/**
	 * the minLength
	 */
	private final int minLength;

	/**
	 * the maxLength
	 */
	private final int maxLength;

	/**
	 * Constructor.
	 * @param annotation the annotation
	 */
	public StringLengthValidator(final MinStringLength annotation) {
		minLength = checkNonNegative("minimum string length", annotation.value());
		maxLength = -1;
	}

	/**
	 * Constructor.
	 * @param annotation the annotation
	 */
	public StringLengthValidator(final MaxStringLength annotation) {
		minLength = -1;
		maxLength = checkNonNegative("maximum string length", annotation.value());
	}

	/**
	 * Constructor.
	 * @param annotation the annotation
	 */
	public StringLengthValidator(final ExactStringLength annotation) {
		minLength = maxLength = checkNonNegative("exact string length", annotation.value());
	}

	/**
	 * 
	 */
	private static int checkNonNegative(final String name, final int value) {
		if (value < 0) {
			throw new IllegalArgumentException(name + " must not be negative");
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	public void validate(IValidatable<String> validatable) {
		int length = validatable.getValue().length();
		if ((minLength >= 0 && length < minLength) || (maxLength >= 0 && length > maxLength)) {
			ValidationError error = new ValidationError(this, getVariation());
			error.setVariable("length", validatable.getValue().length());
			if (minLength >= 0 && maxLength >= 0 && minLength == maxLength) {
				error.setVariable("exact", minLength);
			} else {
				if (minLength >= 0) {
					error.setVariable("minimum", minLength);
				}
				if (maxLength >= 0) {
					error.setVariable("maximum", maxLength);
				}
			}
			validatable.error(error);
		}
	}

	/**
	 * Returns the variation for the error message.
	 */
	private String getVariation() {
		if (minLength >= 0) {
			if (maxLength >= 0) {
				if (minLength == maxLength) {
					return "exact";
				} else {
					return "range";
				}
			} else {
				return "minimum";
			}
		} else {
			if (maxLength >= 0) {
				return "maximum";
			} else {
				throw new IllegalStateException("validation error occured although neither a minimum nor a maximum string length are defined");
			}
		}
	}
	
}
