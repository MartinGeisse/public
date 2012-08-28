/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.validation.palette;

import java.util.Map;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Common implementation for {@link ExactStringLength}, {@link MinStringLength}
 * and {@link MaxStringLength} validation.
 */
public class StringLengthValidator extends AbstractValidator<String> {

	/**
	 * the minLength
	 */
	private final int minLength;

	/**
	 * the maxLength
	 */
	private int maxLength;

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
	 * @see org.apache.wicket.validation.validator.AbstractValidator#onValidate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	protected void onValidate(IValidatable<String> validatable) {
		int length = validatable.getValue().length();
		if ((minLength >= 0 && length < minLength) || (maxLength >= 0 && length > maxLength)) {
			error(validatable);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.validator.AbstractValidator#resourceKey()
	 */
	@Override
	protected String resourceKey() {
		if (minLength >= 0) {
			if (maxLength >= 0) {
				if (minLength == maxLength) {
					return "StringValidator.exact";
				} else {
					return "StringValidator.range";
				}
			} else {
				return "StringValidator.minimum";
			}
		} else {
			if (maxLength >= 0) {
				return "StringValidator.maximum";
			} else {
				throw new IllegalStateException("validation error occured although neither a minimum nor a maximum string length are defined");
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.validator.AbstractValidator#variablesMap(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	protected Map<String, Object> variablesMap(IValidatable<String> validatable) {
		final Map<String, Object> map = super.variablesMap(validatable);
		map.put("length", validatable.getValue().length());
		if (minLength >= 0 && maxLength >= 0 && minLength == maxLength) {
			map.put("exact", minLength);
		} else {
			if (minLength >= 0) {
				map.put("minimum", minLength);
			}
			if (maxLength >= 0) {
				map.put("maximum", maxLength);
			}
		}
		return map;
	}

}
