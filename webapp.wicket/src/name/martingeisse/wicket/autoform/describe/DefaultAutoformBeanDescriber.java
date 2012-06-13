/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.wicket.autoform.annotation.AutoformIgnoreProperty;
import name.martingeisse.wicket.autoform.annotation.AutoformProperties;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * This describer starts with a base property list: If an AutoformFields annotation exists
 * at the bean class, then the list from that annotation is used as the base property
 * list. Otherwise, reflection is used to obtain a list of all bean properties and this
 * is used as the base property list.
 * 
 * Properties that are annotated with AutoformIgnoreProperty are ignored without any
 * further handling.
 * 
 * Properties are then handed to the acceptProperty() method. The default implementation
 * filters out the "class" property. Subclasses can customize this behavior.
 * 
 * For all remaining properties, a property description is generated.
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
	public DefaultAutoformBeanDescription describe(Object bean) {
		Serializable serializableBean = enforceSerializableBean(bean);
		List<PropertyDescriptor> beanPropertyDescriptors = createBasePropertyList(bean);
		List<IAutoformPropertyDescription> propertyDescriptions = new ArrayList<IAutoformPropertyDescription>();
		for (PropertyDescriptor beanPropertyDescriptor : beanPropertyDescriptors) {
			if (beanPropertyDescriptor.getReadMethod().getAnnotation(AutoformIgnoreProperty.class) == null) {
				if (acceptProperty(beanPropertyDescriptor)) {
					propertyDescriptions.add(new DefaultAutoformPropertyDescription(serializableBean, beanPropertyDescriptor));
				}
			}
		}
		return new DefaultAutoformBeanDescription(bean, propertyDescriptions);
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
	 * Adds a property description to the specified bean description and places it either directly
	 * before or directly after the indicator property. It is an error if the indicator property
	 * does not occur in the bean description.
	 * 
	 * @param beanDescription the bean description to which the property shall be added
	 * @param indicatorPropertyName the indicator property that determines the index where the property is added
	 * @param propertyToAdd the property to add
	 * @param after whether the property shall be added after (true) or before (false) the indicator
	 */
	protected static void addProperty(DefaultAutoformBeanDescription beanDescription, String indicatorPropertyName, IAutoformPropertyDescription propertyToAdd, boolean after) {
		int index = 0;
		for (IAutoformPropertyDescription existingProperty : beanDescription.getPropertyDescriptions()) {
			if (existingProperty.getName().equals(indicatorPropertyName)) {
				beanDescription.getPropertyDescriptions().add(after ? (index + 1) : index, propertyToAdd);
				return;
			}
			index++;
		}
		throw new IllegalArgumentException("unknown indicator property: " + indicatorPropertyName);
	}

}
