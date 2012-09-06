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
public final class DefaultAutoformBeanDescription extends AbstractAutoformBeanDescription<Serializable> {

	/**
	 * Constructor.
	 * @param bean the bean being shown
	 * @param propertyDescriptions the list of property descriptions to show in the autoform
	 */
	public DefaultAutoformBeanDescription(final Serializable bean, final List<IAutoformPropertyDescription> propertyDescriptions) {
		super(bean, propertyDescriptions);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescription#getAnnotation(java.lang.Class)
	 */
	@Override
	public <A extends Annotation> A getAnnotation(final Class<A> annotationClass) {
		return getBean().getClass().getAnnotation(annotationClass);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescription#getAnnotations()
	 */
	@Override
	public Annotation[] getAnnotations() {
		return getBean().getClass().getAnnotations();
	}

}
