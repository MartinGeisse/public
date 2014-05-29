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
 * This annotation can be used to mark a property as read-only with respect
 * to autoform handling. Although properties can be marked read-only by
 * not providing a setter method, this annotation can be used if a setter
 * method is necessary for other purposes.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformReadOnly {
}
