/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.validation.palette;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import name.martingeisse.wicket.autoform.annotation.validation.AutoformAssociatedValidator;

/**
 * Validation for integer-typed properties: The property value
 * must be at most the specified value.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@AutoformAssociatedValidator(IntegerRangeValidator.class)
public @interface MaxIntegerValue {

	/**
	 * @return the maximum value.
	 */
	public int value();
	
}
