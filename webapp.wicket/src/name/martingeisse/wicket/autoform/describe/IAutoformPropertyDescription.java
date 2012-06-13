/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import name.martingeisse.common.terms.DisplayName;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * This interface represents the information gathered by an {@link IAutoformBeanDescriber}
 * for a single property of a specific bean (or bean-equivalent object).
 */
public interface IAutoformPropertyDescription extends Serializable {

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName();

	/**
	 * @return the user-visible name of the property, respecting any {@link DisplayName} annotation if present
	 */
	public String getDisplayName();

	/**
	 * Getter method for the type.
	 * @return the type
	 */
	public Class<?> getType();

	/**
	 * Getter method for the model.
	 * @return the model
	 */
	public IModel<?> getModel();

	/**
	 * Getter method for the readOnly.
	 * @return the readOnly
	 */
	public boolean isReadOnly();

	/**
	 * If the bean declares that a specific component shall be used for this property,
	 * then this method returns the component class to use. Otherwise, this method
	 * returns null.
	 * @return the component class or null
	 */
	public Class<? extends Component> getComponentClassOverride();
	
	/**
	 * Returns an annotation for this property. This is used to find further
	 * information about the property, especially details about the components to use.
	 * 
	 * @param <T> the static type of the annotation
	 * @param annotationClass the annotation class
	 * @return the annotation, or null if not found
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass);

}
