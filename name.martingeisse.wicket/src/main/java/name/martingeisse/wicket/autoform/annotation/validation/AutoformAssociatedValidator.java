/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.wicket.validation.IValidator;

/**
 * This is a meta-annotation that can be used to define parameterized
 * validations for autoforms: Whenever an autoform property (i.e.
 * the getter method for the property) has an annotation that in turn
 * has {@link AutoformAssociatedValidator}, a validator is created for
 * that property by taking the validator class from this annotation
 * and passing the annotated annotation to the constructor. The annotated
 * annotation may then contain parameters that control validation.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformAssociatedValidator {

	/**
	 * @return the validator class
	 */
	public Class<? extends IValidator<?>> value();
	
}
