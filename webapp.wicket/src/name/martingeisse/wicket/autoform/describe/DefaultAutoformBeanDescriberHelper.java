/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.wicket.autoform.annotation.structure.AutoformIgnoreProperty;
import name.martingeisse.wicket.autoform.annotation.structure.AutoformPropertyOrder;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * An instance of this class is generated per run of {@link DefaultAutoformBeanDescriber}.
 */
class DefaultAutoformBeanDescriberHelper {

	/**
	 * the describer
	 */
	private final DefaultAutoformBeanDescriber describer;

	/**
	 * the beanClass
	 */
	private final Class<? extends Serializable> beanClass;

	/**
	 * the declaredNameOrder
	 */
	private List<String> declaredNameOrder;

	/**
	 * the actualPropertiesByName
	 */
	private Map<String, PropertyDescriptor> allActualPropertiesByName;

	/**
	 * the sortedRelevantProperties
	 */
	private List<PropertyDescriptor> sortedRelevantProperties;

	/**
	 * the sortedVisibleProperties
	 */
	private List<PropertyDescriptor> sortedVisibleProperties;

	/**
	 * Constructor.
	 * @param describer the bean describer, used to call overridable methods
	 * @param beanClass the bean class to describe
	 */
	DefaultAutoformBeanDescriberHelper(final DefaultAutoformBeanDescriber describer, final Class<? extends Serializable> beanClass) {
		this.describer = describer;
		this.beanClass = beanClass;
	}

	/**
	 * Main execution method.
	 */
	List<PropertyDescriptor> run() {
		initializeDeclaredNameOrder();
		initializeAllActualPropertiesByName();
		validateOrderAnnotation();
		initializeSortedRelevantProperties();
		initializeSortedVisibleProperties();
		return sortedVisibleProperties;
	}

	/**
	 * Initializes the declaredNameOrder from the {@link AutoformPropertyOrder}
	 * (sets the field to null if the annotation is missing).
	 */
	private void initializeDeclaredNameOrder() {
		final AutoformPropertyOrder orderAnnotation = beanClass.getAnnotation(AutoformPropertyOrder.class);
		if (orderAnnotation == null) {
			declaredNameOrder = null;
		} else {
			declaredNameOrder = Arrays.asList(orderAnnotation.value());
		}
	}

	/**
	 * Initializes the allActualPropertiesByName field from the bean class.
	 */
	private void initializeAllActualPropertiesByName() {
		allActualPropertiesByName = new HashMap<String, PropertyDescriptor>();
		for (final PropertyDescriptor beanPropertyDescriptor : PropertyUtils.getPropertyDescriptors(beanClass)) {
			allActualPropertiesByName.put(beanPropertyDescriptor.getName(), beanPropertyDescriptor);
		}
	}

	/**
	 * Ensures that each property not tagged with {@link AutoformIgnoreProperty}
	 * is listed in the {@link AutoformPropertyOrder} annotation if and only if
	 * acceptAsRelevant() returns true for it.
	 */
	private void validateOrderAnnotation() {
		if (declaredNameOrder == null) {
			return;
		}
		for (final PropertyDescriptor beanPropertyDescriptor : allActualPropertiesByName.values()) {
			if (!hasIgnoreAnnotation(beanPropertyDescriptor)) {
				final String name = beanPropertyDescriptor.getName();
				final boolean relevant = describer.acceptAsRelevant(beanPropertyDescriptor);
				final boolean listed = declaredNameOrder.contains(name);
				if (relevant && !listed) {
					throw new IllegalStateException("autoform bean property " + name + " is relevant (according to acceptAsRelevant()) but not listed in AutoformPropertyOrder");
				} else if (listed && !relevant) {
					throw new IllegalStateException("autoform bean property " + name + " is not relevant (according to acceptAsRelevant()) but listed in AutoformPropertyOrder");
				}
			}
		}
	}

	/**
	 * Initializes the sortedRelevantProperties field.
	 */
	private void initializeSortedRelevantProperties() {
		sortedRelevantProperties = new ArrayList<PropertyDescriptor>();
		if (declaredNameOrder == null) {
			for (final PropertyDescriptor beanPropertyDescriptor : allActualPropertiesByName.values()) {
				if (!hasIgnoreAnnotation(beanPropertyDescriptor) && describer.acceptAsRelevant(beanPropertyDescriptor)) {
					sortedRelevantProperties.add(beanPropertyDescriptor);
				}
			}
		} else {
			for (final String name : declaredNameOrder) {
				final PropertyDescriptor beanPropertyDescriptor = allActualPropertiesByName.get(name);
				if (!hasIgnoreAnnotation(beanPropertyDescriptor)) {
					sortedRelevantProperties.add(beanPropertyDescriptor);
				}
			}
		}
	}

	/**
	 * Initializes the sortedVisibleProperties field by filtering the sortedRelevantProperties.
	 */
	private void initializeSortedVisibleProperties() {
		sortedVisibleProperties = new ArrayList<PropertyDescriptor>();
		for (final PropertyDescriptor beanPropertyDescriptor : sortedRelevantProperties) {
			if (describer.acceptAsVisible(beanPropertyDescriptor)) {
				sortedVisibleProperties.add(beanPropertyDescriptor);
			}
		}
	}

	/**
	 * Checks whether the {@link AutoformIgnoreProperty} is attached to the getter method
	 * of the specified bean property.
	 */
	private boolean hasIgnoreAnnotation(final PropertyDescriptor beanPropertyDescriptor) {
		return (beanPropertyDescriptor.getReadMethod().getAnnotation(AutoformIgnoreProperty.class) != null);
	}

}
