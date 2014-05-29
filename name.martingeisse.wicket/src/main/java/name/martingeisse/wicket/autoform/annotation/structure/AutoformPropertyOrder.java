/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.structure;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation can be used to specify the order of the displayed properties
 * for an autoform bean.
 * 
 * Without this annotation, all properties with visible getter methods (except
 * those with {@link AutoformIgnoreProperty}) will be used in an unspecified order.
 * 
 * When this annotation is present, it must list exactly those properties that
 * are not annotated with {@link AutoformIgnoreProperty}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformPropertyOrder {

	/**
	 * @return the property names
	 */
	public String[] value();
	
}
