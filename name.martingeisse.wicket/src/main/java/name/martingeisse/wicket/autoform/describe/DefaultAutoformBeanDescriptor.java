/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * This class contains the information about a whole autoform bean.
 */
public final class DefaultAutoformBeanDescriptor extends AbstractAutoformBeanDescriptor<Serializable> {

	/**
	 * Constructor.
	 * @param bean the bean being shown
	 * @param propertyDescriptors the list of property descriptors to show in the autoform
	 */
	public DefaultAutoformBeanDescriptor(final Serializable bean, final List<IAutoformPropertyDescriptor> propertyDescriptors) {
		super(bean, propertyDescriptors);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriptor#getAnnotation(java.lang.Class)
	 */
	@Override
	public <A extends Annotation> A getAnnotation(final Class<A> annotationClass) {
		return getBean().getClass().getAnnotation(annotationClass);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriptor#getAnnotations()
	 */
	@Override
	public Annotation[] getAnnotations() {
		return getBean().getClass().getAnnotations();
	}

}
