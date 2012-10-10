/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that a getter method was not
 * generated for a database column. This should be used in
 * place of {@link GeneratedFromColumn} (since it would be
 * an error if that property was just missing).
 * 
 * Note that {@link NonColumnGetter} is assumed implicitly
 * for the getClass() method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonColumnGetter {
}
