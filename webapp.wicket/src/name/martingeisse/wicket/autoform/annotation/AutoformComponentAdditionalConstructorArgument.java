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
 * This annotation specifies an additional String-typed argument
 * that is passed to the component constructor. It can be used to
 * configure the behavior of the component in a simple way.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformComponentAdditionalConstructorArgument {

	/**
	 * @return the additional argument
	 */
	public String value();
	
}
