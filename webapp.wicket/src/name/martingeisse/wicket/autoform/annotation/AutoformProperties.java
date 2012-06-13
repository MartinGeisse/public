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
 * This annotation can be used to specify the displayed properties and their order
 * for an autoform bean. Without this annotation, all properties with visible
 * getter methods (except those with {@link AutoformIgnoreProperty}) will be
 * used in an unspecified order.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformProperties {

	/**
	 * @return the property names
	 */
	public String[] value();
	
}
