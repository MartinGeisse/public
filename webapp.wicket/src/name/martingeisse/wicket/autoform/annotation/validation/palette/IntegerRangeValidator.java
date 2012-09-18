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
 * Common implementation for {@link MinIntegerValue} and {@link MaxIntegerValue} validation.
 */
public class IntegerRangeValidator extends AbstractValidator<Integer> {

	/**
	 * the value
	 */
	private final int value;

	/**
	 * the isMinimum
	 */
	private final boolean isMinimum;

	/**
	 * Constructor.
	 * @param annotation the annotation
	 */
	public IntegerRangeValidator(final MinIntegerValue annotation) {
		this(annotation.value(), true);
	}

	/**
	 * Constructor.
	 * @param annotation the annotation
	 */
	public IntegerRangeValidator(final MaxIntegerValue annotation) {
		this(annotation.value(), false);
	}

	/**
	 * Constructor.
	 * @param value the value to compare with
	 * @param isMinimum true if the value is the minimum, false if it is the maximum
	 */
	public IntegerRangeValidator(final int value, final boolean isMinimum) {
		this.value = value;
		this.isMinimum = isMinimum;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.validator.AbstractValidator#onValidate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	protected void onValidate(IValidatable<Integer> validatable) {
		int actualValue = validatable.getValue();
		if ((isMinimum && actualValue < value) || (!isMinimum && actualValue > value)) {
			error(validatable);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.validator.AbstractValidator#resourceKey()
	 */
	@Override
	protected String resourceKey() {
		return (isMinimum ? "IntegerValidator.minimum" : "IntegerValidator.maximum");
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.validator.AbstractValidator#variablesMap(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	protected Map<String, Object> variablesMap(IValidatable<Integer> validatable) {
		final Map<String, Object> map = super.variablesMap(validatable);
		map.put("value", validatable.getValue());
		if (isMinimum) {
			map.put("minimum", value);
		} else {
			map.put("maximum", value);
		}
		return map;
	}

}
