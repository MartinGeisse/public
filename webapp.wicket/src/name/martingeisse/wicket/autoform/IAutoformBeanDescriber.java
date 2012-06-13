/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.io.Serializable;

/**
 * Implementations of this interface are able to provide property information
 * for a bean being shown in an autoform. A default implementation exists
 * ({@link DefaultAutoformBeanDescriber}) that simply lists Java Bean properties.
 */
public interface IAutoformBeanDescriber extends Serializable {

	/**
	 * @param bean the bean to describe
	 * @return information about the bean for use in the autoform
	 */
	public IAutoformBeanDescriptor describe(Object bean);
	
}
