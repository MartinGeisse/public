/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.io.Serializable;

/**
 * Implementations of this interface are able to provide information for a bean
 * that is relevant to autoform generation. Information is typically gathered from
 * bean properties and annotations.
 * 
 * Naming convention: This interface is called "describer", referring to the fact
 * that it actively creates a description from a bean to decsribe. The "description"
 * is a passive object that contains the results from this describer.
 * 
 * {@link DefaultAutoformBeanDescriber} is the default implementation and
 * simply lists Java Bean properties. Other implementations would be created
 * for objects whose properties do not conform to the Java beans conventions.
 * 
 * Note that other customization of autoforms, such as custom-tailored
 * components, validations or triggered effects should be considered orthogonal
 * to bean describers and should be expressed through annotations (for the default
 * bean describer) or equivalent means (for other bean describers).
 */
public interface IAutoformBeanDescriber extends Serializable {

	/**
	 * Describes the specified bean.
	 * @param bean the bean to describe
	 * @return information about the bean for use in the autoform
	 */
	public IAutoformBeanDescription describe(Object bean);
	
}
