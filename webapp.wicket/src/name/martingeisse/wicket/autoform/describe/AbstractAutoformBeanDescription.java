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
 * This class implements basic methods defined in {@link IAutoformBeanDescription}
 * as well as behavior automatically derived from annotations.
 * 
 * @param <T> the internal bean type
 */
public abstract class AbstractAutoformBeanDescription<T> implements IAutoformBeanDescription {

	/**
	 * the bean
	 */
	private final T bean;

	/**
	 * the propertyDescriptions
	 */
	private final List<IAutoformPropertyDescription> propertyDescriptions;

	/**
	 * Constructor.
	 * @param bean the bean being shown
	 * @param propertyDescriptions the list of property descriptions to show in the autoform
	 */
	public AbstractAutoformBeanDescription(T bean, List<IAutoformPropertyDescription> propertyDescriptions) {
		this.bean = bean;
		this.propertyDescriptions = propertyDescriptions;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformBeanDescription#getBean()
	 */
	@Override
	public T getBean() {
		return bean;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformBeanDescription#getPropertyDescriptions()
	 */
	@Override
	public List<IAutoformPropertyDescription> getPropertyDescriptions() {
		return propertyDescriptions;
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
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformBeanDescription#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		DisplayName annotation = getAnnotation(DisplayName.class);
		return (annotation == null) ? bean.getClass().getSimpleName() : annotation.value();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformBeanDescription#getPropertyDescription(java.lang.String)
	 */
	@Override
	public IAutoformPropertyDescription getPropertyDescription(String propertyName) {
		if (propertyName == null) {
			throw new IllegalArgumentException("propertyName is null");
		}
		for (IAutoformPropertyDescription description : getPropertyDescriptions()) {
			if (propertyName.equals(description.getName())) {
				return description;
			}
		}
		return null;
	}
	
}
