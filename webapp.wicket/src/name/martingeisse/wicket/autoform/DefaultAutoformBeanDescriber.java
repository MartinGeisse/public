/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * This describer starts with a base property list. If an AutoformFields annotation exists
 * at the bean class, then the list from that annotation is used. Otherwise, reflection
 * is used to obtain a list of all bean properties.
 * 
 * Properties that are annotated with AutoformIgnoreProperty are ignored without any
 * further handling.
 * 
 * Properties are then handed to the acceptProperty() method. The default implementation
 * filters out the "class" property. Subclasses can customize this behavior.
 * 
 * For all remaining properties, a property descriptor is generated.
 */
public class DefaultAutoformBeanDescriber implements IAutoformBeanDescriber {

	/**
	 * the default instance of this class
	 */
	public static final DefaultAutoformBeanDescriber instance = new DefaultAutoformBeanDescriber();
	
	/**
	 * Prevent further instantiation.
	 */
	protected DefaultAutoformBeanDescriber() {
	}
	
	/**
	 * @param bean the bean
	 * @return the bean, cast to {@link Serializable} type
	 */
	protected final Serializable enforceSerializableBean(Object bean) {
		try {
			return (Serializable)bean;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("bean is not serialiable");
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformBeanDescriber#describe(java.lang.Object)
	 */
	@Override
	public DefaultAutoformBeanDescriptor describe(Object bean) {
		Serializable serializableBean = enforceSerializableBean(bean);
		List<PropertyDescriptor> beanPropertyDescriptors = createBasePropertyList(bean);
		List<IAutoformPropertyDescriptor> propertyDescriptors = new ArrayList<IAutoformPropertyDescriptor>();
		for (PropertyDescriptor beanPropertyDescriptor : beanPropertyDescriptors) {
			if (beanPropertyDescriptor.getReadMethod().getAnnotation(AutoformIgnoreProperty.class) == null) {
				if (acceptProperty(beanPropertyDescriptor)) {
					propertyDescriptors.add(new DefaultAutoformPropertyDescriptor(serializableBean, beanPropertyDescriptor));
				}
			}
		}
		return new DefaultAutoformBeanDescriptor(bean, propertyDescriptors);
	}

	/**
	 * @return
	 */
	private List<PropertyDescriptor> createBasePropertyList(Object bean) {
		List<PropertyDescriptor> result = new ArrayList<PropertyDescriptor>();
		AutoformProperties annotation = bean.getClass().getAnnotation(AutoformProperties.class);
		if (annotation == null) {
			for (PropertyDescriptor beanPropertyDescriptor : PropertyUtils.getPropertyDescriptors(bean)) {
				result.add(beanPropertyDescriptor);
			}
		} else {
			for (String propertyName : annotation.value()) {
				try {
					result.add(PropertyUtils.getPropertyDescriptor(bean, propertyName));
				} catch (Exception e) {
					throw new RuntimeException("bean class " + bean.getClass() + ": cannot get property descriptor named " + propertyName + ", " + e);
				}
			}
		}
		return result;
	}
	
	/**
	 * Subclasses can override this method to control which properties are listed. The default implementation
	 * accepts all properties except "class".
	 * @param beanPropertyDescriptor the descriptor for the property to check
	 * @return true to include the property in the autoform, false to exclude
	 */
	protected boolean acceptProperty(PropertyDescriptor beanPropertyDescriptor) {
		return !"class".equals(beanPropertyDescriptor.getName());
	}
	
	/**
	 * Adds a property descriptor to the specified bean descriptor and places it either directly
	 * before or directly after the indicator property. It is an error if the indicator property
	 * does not occur in the bean descriptor.
	 * 
	 * @param beanDescriptor the bean descriptor to which the property shall be added
	 * @param indicatorPropertyName the indicator property that determines the index where the property is added
	 * @param propertyToAdd the property to add
	 * @param after whether the property shall be added after (true) or before (false) the indicator
	 */
	protected static void addProperty(DefaultAutoformBeanDescriptor beanDescriptor, String indicatorPropertyName, IAutoformPropertyDescriptor propertyToAdd, boolean after) {
		int index = 0;
		for (IAutoformPropertyDescriptor existingProperty : beanDescriptor.getPropertyDescriptors()) {
			if (existingProperty.getName().equals(indicatorPropertyName)) {
				beanDescriptor.getPropertyDescriptors().add(after ? (index + 1) : index, propertyToAdd);
				return;
			}
			index++;
		}
		throw new IllegalArgumentException("unknown indicator property: " + indicatorPropertyName);
	}

}
