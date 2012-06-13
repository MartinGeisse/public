/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.io.Serializable;
import java.util.List;

/**
 * This interface represents the information gathered by an {@link IAutoformBeanDescriber}
 * for a specific bean (or bean-equivalent object).
 */
public interface IAutoformBeanDescription extends Serializable {

	/**
	 * Getter method for the bean.
	 * @return the bean
	 */
	public Object getBean();

	/**
	 * Returns the user-visible name of the bean.
	 * @return the user-visible name of the bean
	 */
	public String getDisplayName();

	/**
	 * Getter method for the propertyDescriptions.
	 * @return the propertyDescriptions
	 */
	public List<? extends IAutoformPropertyDescription> getPropertyDescriptions();

	/**
	 * Returns the property description for the specified property.
	 * @param propertyName the name of the property
	 * @return the property description
	 */
	public IAutoformPropertyDescription getPropertyDescription(String propertyName);

}
