/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.util.List;

import name.martingeisse.common.terms.DisplayName;

/**
 * This class contains the information about a whole autoform bean.
 */
public final class DefaultAutoformBeanDescription implements IAutoformBeanDescription {

	/**
	 * the bean
	 */
	private final Object bean;

	/**
	 * the propertyDescriptions
	 */
	private final List<IAutoformPropertyDescription> propertyDescriptions;

	/**
	 * Constructor.
	 * @param bean the bean being shown
	 * @param propertyDescriptions the list of property descriptions to show in the autoform
	 */
	public DefaultAutoformBeanDescription(Object bean, List<IAutoformPropertyDescription> propertyDescriptions) {
		this.bean = bean;
		this.propertyDescriptions = propertyDescriptions;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformBeanDescription#getBean()
	 */
	@Override
	public Object getBean() {
		return bean;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformBeanDescription#getPropertyDescriptions()
	 */
	@Override
	public List<IAutoformPropertyDescription> getPropertyDescriptions() {
		return propertyDescriptions;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformBeanDescription#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		DisplayName annotation = bean.getClass().getAnnotation(DisplayName.class);
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
