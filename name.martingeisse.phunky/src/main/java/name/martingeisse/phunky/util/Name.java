/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generic name annotation.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PACKAGE, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {

	/**
	 * @return the name of the annotated declaration
	 */
	public String value();
	
}
