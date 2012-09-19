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
 * Common implementation for {@link MinIntegerValue} and {@link MaxIntegerValue} validation.
 */
public class IntegerRangeValidator extends Behavior implements IValidator<Integer>, IClusterable {

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
	 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	public void validate(IValidatable<Integer> validatable) {
		int actualValue = validatable.getValue();
		if ((isMinimum && actualValue < value) || (!isMinimum && actualValue > value)) {
			ValidationError error = new ValidationError(this, isMinimum ? "minimum" : "maximum");
			error.setVariable("value", validatable.getValue());
			error.setVariable(isMinimum ? "minimum" : "maximum", value);
			validatable.error(error);
		}
	}

}
