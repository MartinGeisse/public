/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.util.List;

import name.martingeisse.common.terms.DisplayName;

/**
 * This class contains the information about a whole autoform bean.
 */
public final class DefaultAutoformBeanDescriptor implements IAutoformBeanDescriptor {

	/**
	 * the bean
	 */
	private final Object bean;

	/**
	 * the propertyDescriptors
	 */
	private final List<IAutoformPropertyDescriptor> propertyDescriptors;

	/**
	 * Constructor.
	 * @param bean the bean being shown
	 * @param propertyDescriptors the list of property descriptors to show in the autoform
	 */
	public DefaultAutoformBeanDescriptor(Object bean, List<IAutoformPropertyDescriptor> propertyDescriptors) {
		this.bean = bean;
		this.propertyDescriptors = propertyDescriptors;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformBeanDescriptor#getBean()
	 */
	@Override
	public Object getBean() {
		return bean;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformBeanDescriptor#getPropertyDescriptors()
	 */
	@Override
	public List<IAutoformPropertyDescriptor> getPropertyDescriptors() {
		return propertyDescriptors;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformBeanDescriptor#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		DisplayName annotation = bean.getClass().getAnnotation(DisplayName.class);
		return (annotation == null) ? bean.getClass().getSimpleName() : annotation.value();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformBeanDescriptor#getPropertyDescriptor(java.lang.String)
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
