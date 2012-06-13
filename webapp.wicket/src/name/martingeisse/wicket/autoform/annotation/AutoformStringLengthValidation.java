/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation causes the autoform system to validate the
 * length of string fields. It cannot be attached to other fields.
 * This annotation has no effect on string fields whose value is null.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformStringLengthValidation {

	/**
	 * @return the maximum string length.
	 */
	public int value();
	
}
