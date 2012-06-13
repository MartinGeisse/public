/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.io.Serializable;
import java.util.List;

import name.martingeisse.common.terms.DisplayName;

/**
 * This interface is used by autoform classes to get information about the
 * data bean being edited.
 */
public interface IAutoformBeanDescriptor extends Serializable {

	/**
	 * Getter method for the bean.
	 * @return the bean
	 */
	public Object getBean();

	/**
	 * Getter method for the propertyDescriptors.
	 * @return the propertyDescriptors
	 */
	public List<? extends IAutoformPropertyDescriptor> getPropertyDescriptors();

	/**
	 * @return the user-visible name of the bean, respecting any {@link DisplayName} annotation if present
	 */
	public String getDisplayName();

	/**
	 * Returns the property descriptor for the specified property.
	 * @param propertyName the name of the property
	 * @return the property descriptor
	 */
	public IAutoformPropertyDescriptor getPropertyDescriptor(String propertyName);

}
