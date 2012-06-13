/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

/**
 * This class is invoked by annotating an autoform data bean with {@link AutoformComponentBuildPostProcessor}.
 * When an autoform is built and the data bean is annotated this way, then an instance of the
 * corresponding implementation of this interface is created right after the property components
 * have been created, and invoked on the bean descriptor. This allows to modify the components
 * for the specific case of the annotated bean. For example, it allows to install inter-property
 * behavior (such as Javascript-based component enablers).
 */
public interface IAutoformComponentBuildPostProcessor {

	/**
	 * Processes the specified bean descriptor. This method is invoked right after property
	 * components have been created and stored in the descriptor.
	 * @param beanDescriptor the bean descriptor
	 */
	public void process(IAutoformBeanDescriptor beanDescriptor);
	
}
