/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.wicket.autoform.annotation.structure.AutoformIgnoreProperty;
import name.martingeisse.wicket.autoform.annotation.structure.AutoformPropertyOrder;

/**
 * An instance of this class is generated per run of {@link AbstractAutoformBeanDescriber}.
 * @param <BEANDESC> the type of implementation-specific bean descriptors being used
 * @param <PROPDESC> the type of implementation-specific property descriptors being used
 * @param <PARENT> the type of the parent describer
 */
public abstract class AbstractAutoformBeanDescriberHelper<BEANDESC, PROPDESC, PARENT extends AbstractAutoformBeanDescriber<BEANDESC, PROPDESC, ?>> {

	/**
	 * the describer
	 */
	private final PARENT describer;

	/**
	 * the beanDescriptor
	 */
	private final BEANDESC beanDescriptor;

	/**
	 * the declaredNameOrder
	 */
	private List<String> declaredNameOrder;

	/**
	 * the actualPropertiesByName
	 */
	private Map<String, PROPDESC> allActualPropertiesByName;

	/**
	 * the sortedRelevantProperties
	 */
	private List<PROPDESC> sortedRelevantProperties;

	/**
	 * the sortedVisibleProperties
	 */
	private List<PROPDESC> sortedVisibleProperties;

	/**
	 * Constructor.
	 * @param describer the bean describer, used to call overridable methods
	 * @param beanDescriptor the descriptor for the bean to describe
	 */
	public AbstractAutoformBeanDescriberHelper(final PARENT describer, final BEANDESC beanDescriptor) {
		this.describer = describer;
		this.beanDescriptor = beanDescriptor;
	}

	/**
	 * Main execution method.
	 * @return the list of relevant and visible properties in expected order
	 */
	public List<PROPDESC> run() {
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
		final AutoformPropertyOrder orderAnnotation = getBeanAnnotation(beanDescriptor, AutoformPropertyOrder.class);
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
		allActualPropertiesByName = new HashMap<String, PROPDESC>();
		for (final PROPDESC beanPropertyDescriptor : getPropertyDescriptors(beanDescriptor)) {
			allActualPropertiesByName.put(getPropertyName(beanPropertyDescriptor), beanPropertyDescriptor);
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
		for (final PROPDESC beanPropertyDescriptor : allActualPropertiesByName.values()) {
			if (!hasIgnoreAnnotation(beanPropertyDescriptor)) {
				final String name = getPropertyName(beanPropertyDescriptor);
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
		sortedRelevantProperties = new ArrayList<PROPDESC>();
		if (declaredNameOrder == null) {
			for (final PROPDESC beanPropertyDescriptor : allActualPropertiesByName.values()) {
				if (!hasIgnoreAnnotation(beanPropertyDescriptor) && describer.acceptAsRelevant(beanPropertyDescriptor)) {
					sortedRelevantProperties.add(beanPropertyDescriptor);
				}
			}
		} else {
			for (final String name : declaredNameOrder) {
				final PROPDESC beanPropertyDescriptor = allActualPropertiesByName.get(name);
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
		sortedVisibleProperties = new ArrayList<PROPDESC>();
		for (final PROPDESC beanPropertyDescriptor : sortedRelevantProperties) {
			if (describer.acceptAsVisible(beanPropertyDescriptor)) {
				sortedVisibleProperties.add(beanPropertyDescriptor);
			}
		}
	}

	/**
	 * Checks whether the {@link AutoformIgnoreProperty} is attached to the getter method
	 * of the specified bean property.
	 */
	private boolean hasIgnoreAnnotation(final PROPDESC beanPropertyDescriptor) {
		return (getPropertyAnnotation(beanPropertyDescriptor, AutoformIgnoreProperty.class) != null);
	}

	/**
	 * Returns an annotation for the bean being described.
	 * @param <A> the static type of the annotation
	 * @param beanDescriptor the descriptor for the bean to describe
	 * @param annotationClass the annotation class
	 * @return the annotation, or null if not found
	 */
	protected abstract <A extends Annotation> A getBeanAnnotation(BEANDESC beanDescriptor, Class<A> annotationClass);

	/**
	 * Returns all property descriptors for the bean descriptor.
	 * @param beanDescriptor the descriptor for the bean to describe
	 * @return an array of all property descriptors
	 */
	protected abstract PROPDESC[] getPropertyDescriptors(BEANDESC beanDescriptor);
	
	/**
	 * Returns the name of a property from a property descriptor.
	 * @param propertyDescriptor the property descriptor
	 * @return the name
	 */
	protected abstract String getPropertyName(PROPDESC propertyDescriptor);

	/**
	 * Returns an annotation for a bean property being described.
	 * @param <A> the static type of the annotation
	 * @param propertyDescriptor the descriptor for the property
	 * @param annotationClass the annotation class
	 * @return the annotation, or null if not found
	 */
	protected abstract <A extends Annotation> A getPropertyAnnotation(PROPDESC propertyDescriptor, Class<A> annotationClass);
	
}
