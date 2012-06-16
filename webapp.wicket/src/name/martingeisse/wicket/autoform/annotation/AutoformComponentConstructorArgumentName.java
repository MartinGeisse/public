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
 * This annotation allows to pass an additional argument to the constructor
 * of the component for an autoform property. The argument must be
 * an annotation instance and must be attached to the getter method just
 * like other autoform property annotations. In addition, this annotation
 * must be attached to the getter too, and must specify the name of
 * the annotation to pass to the constructor.
 * 
 * Example: A bean has a property called customerName, using the
 * getter method getCustomerName(). Autoform annotations for this
 * property are attached to the getter method. To pass an additional
 * constructor argument of annotation type \@MyAnnotation, you must
 * add that annotation to the getter method, as well as
 * \@AutoformComponentConstructorArgumentName("MyAnnotation").
 * 
 * The component class must obviously be tailored for use with autoforms
 * for this feature to make sense. However, this allows to customize
 * components without also customizing the component factory (which
 * would otherwise be needed to interpret component customization
 * annotations), so this is primarily useful in combination with
 * {@link AutoformComponent}.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformComponentConstructorArgumentName {

	/**
	 * @return the name of the annotation type whose instance to pass
	 * to the component constructor.
	 */
	public String value();
	
}
