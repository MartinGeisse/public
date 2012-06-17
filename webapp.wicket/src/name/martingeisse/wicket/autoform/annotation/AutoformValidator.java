/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.wicket.validation.IValidator;

/**
 * This annotation specifies one or more validators to be added
 * to form components.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformValidator {

	/**
	 * @return the validator classes
	 */
	public Class<? extends IValidator<?>>[] value();
	
}
