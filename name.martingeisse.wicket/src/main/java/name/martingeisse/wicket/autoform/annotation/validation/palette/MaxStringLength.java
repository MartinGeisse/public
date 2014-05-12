/**
 * Copyright (c) 2010 Martin Geisse
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
 * Validation for string-typed properties: The property value
 * must have at most the specified length.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@AutoformAssociatedValidator(StringLengthValidator.class)
public @interface MaxStringLength {

	/**
	 * @return the maximum string length.
	 */
	public int value();
	
}
