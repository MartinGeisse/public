/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * An instance of this class is generated per run of {@link DefaultAutoformBeanDescriber}.
 */
class DefaultAutoformBeanDescriberHelper extends AbstractAutoformBeanDescriberHelper<Class<? extends Serializable>, PropertyDescriptor, DefaultAutoformBeanDescriber> {

	/**
	 * Constructor.
	 * @param describer the bean describer, used to call overridable methods
	 * @param beanClass the bean class to describe
	 */
	DefaultAutoformBeanDescriberHelper(final DefaultAutoformBeanDescriber describer, final Class<? extends Serializable> beanClass) {
		super(describer, beanClass);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriberHelper#getBeanAnnotation(java.lang.Object, java.lang.Class)
	 */
	@Override
	protected <A extends Annotation> A getBeanAnnotation(final Class<? extends Serializable> beanDescriptor, final Class<A> annotationClass) {
		return beanDescriptor.getAnnotation(annotationClass);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriberHelper#getPropertyDescriptors(java.lang.Object)
	 */
	@Override
	protected PropertyDescriptor[] getPropertyDescriptors(final Class<? extends Serializable> beanDescriptor) {
		return PropertyUtils.getPropertyDescriptors(beanDescriptor);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriberHelper#getPropertyName(java.lang.Object)
	 */
	@Override
	protected String getPropertyName(final PropertyDescriptor propertyDescriptor) {
		return propertyDescriptor.getName();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriberHelper#getPropertyAnnotation(java.lang.Object, java.lang.Class)
	 */
	@Override
	protected <A extends Annotation> A getPropertyAnnotation(final PropertyDescriptor propertyDescriptor, final Class<A> annotationClass) {
		return propertyDescriptor.getReadMethod().getAnnotation(annotationClass);
	}

}
