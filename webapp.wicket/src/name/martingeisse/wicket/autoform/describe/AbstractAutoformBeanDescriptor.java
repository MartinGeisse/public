/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.lang.annotation.Annotation;
import java.util.List;

import name.martingeisse.common.terms.DisplayName;

/**
 * This class implements basic methods defined in {@link IAutoformBeanDescriptor}
 * as well as behavior automatically derived from annotations.
 * 
 * @param <T> the internal bean type
 */
public abstract class AbstractAutoformBeanDescriptor<T> implements IAutoformBeanDescriptor {

	/**
	 * the bean
	 */
	private final T bean;

	/**
	 * the propertyDescriptors
	 */
	private final List<IAutoformPropertyDescriptor> propertyDescriptors;

	/**
	 * Constructor.
	 * @param bean the bean being shown
	 * @param propertyDescriptors the list of property descriptors to show in the autoform
	 */
	public AbstractAutoformBeanDescriptor(T bean, List<IAutoformPropertyDescriptor> propertyDescriptors) {
		this.bean = bean;
		this.propertyDescriptors = propertyDescriptors;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriptor#getBean()
	 */
	@Override
	public T getBean() {
		return bean;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriptor#getPropertyDescriptors()
	 */
	@Override
	public List<IAutoformPropertyDescriptor> getPropertyDescriptors() {
		return propertyDescriptors;
	}

	/**
	 * Returns an annotation for the bean. This is used to find further
	 * information about the bean.
	 * 
	 * @param <A> the static type of the annotation
	 * @param annotationClass the annotation class
	 * @return the annotation, or null if not found
	 */
	public abstract <A extends Annotation> A getAnnotation(Class<A> annotationClass);
	
	/**
	 * Returns all annotations for the bean. This is used to find further
	 * information about the bean.
	 * @return the annotations
	 */
	public abstract Annotation[] getAnnotations();

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriptor#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		DisplayName annotation = getAnnotation(DisplayName.class);
		return (annotation == null) ? bean.getClass().getSimpleName() : annotation.value();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriptor#getPropertyDescriptor(java.lang.String)
	 */
	@Override
	public IAutoformPropertyDescriptor getPropertyDescriptor(String propertyName) {
		if (propertyName == null) {
			throw new IllegalArgumentException("propertyName is null");
		}
		for (IAutoformPropertyDescriptor descriptor : getPropertyDescriptors()) {
			if (propertyName.equals(descriptor.getName())) {
				return descriptor;
			}
		}
		return null;
	}
	
}
