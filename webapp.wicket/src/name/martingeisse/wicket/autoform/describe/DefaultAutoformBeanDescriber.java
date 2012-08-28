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

import name.martingeisse.wicket.autoform.annotation.structure.AutoformIgnoreProperty;
import name.martingeisse.wicket.autoform.annotation.structure.AutoformPropertyOrder;

/**
 * This describer builds descriptions for bean properties with the following rules:
 * - properties for which acceptAsRelevant() returns false are considered
 *   irrelevant to autoforms in general, such as Java's getClass(). No
 *   property description is build from them, and including them in
 *   {@link AutoformPropertyOrder} is an error.
 * - properties for which acceptAsVisible() returns false are hidden in
 *   the description, although relevant in principle. They are also expected
 *   to appear in {@link AutoformPropertyOrder} (if present).
 *   This can be used to hide specific properties of a bean according to
 *   the context, using a context-specific describer subclass.
 * - properties tagged with {@link AutoformIgnoreProperty} do not produce
 *   a property description. Whether or not they appear in
 *   {@link AutoformPropertyOrder} is ignored. This allows the bean author to
 *   skip the distinction and hide a property with the same annotation,
 *   no matter if the property is irrelevant, relevant but permanently
 *   hidden, or temporarily hidden for quick prototyping.
 * 
 * Internals:
 * 
 * The describer starts with the list of all bean properties, and first
 * performs the following validation check: If {@link AutoformPropertyOrder}
 * is present, then each property that is not tagged with
 * {@link AutoformIgnoreProperty} must appear in that order list if and
 * only if it is relevant according to acceptAsRelevant().
 * 
 * The describer then builds a property list: If {@link AutoformPropertyOrder}
 * is present, then the list is built from that order, skipping all properties
 * with {@link AutoformIgnoreProperty}. Otherwise it contains all properties
 * without {@link AutoformIgnoreProperty} in an unspecified order.
 * 
 * This describer then removes all hidden properties, i.e. properties for
 * which acceptAsVisible() returns false. For all remaining properties, a
 * property description is generated.
 */
public class DefaultAutoformBeanDescriber implements IAutoformBeanDescriber {

	/**
	 * the default instance of this class
	 */
	public static final DefaultAutoformBeanDescriber instance = new DefaultAutoformBeanDescriber();
	
	/**
	 * Prevent further instantiation of this class, but allow subclass instances.
	 */
	protected DefaultAutoformBeanDescriber() {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformBeanDescriber#describe(java.lang.Object)
	 */
	@Override
	public DefaultAutoformBeanDescription describe(Object bean) {
		
		// analyze the bean
		Serializable serializableBean = enforceSerializableBean(bean);
		List<PropertyDescriptor> sortedVisibleBeanPropertyDescriptors = new DefaultAutoformBeanDescriberHelper(this, serializableBean.getClass()).run();
		
		// generate our own property descriptors for the properties
		List<IAutoformPropertyDescription> propertyDescriptions = new ArrayList<IAutoformPropertyDescription>();
		for (PropertyDescriptor beanPropertyDescriptor : sortedVisibleBeanPropertyDescriptors) {
			propertyDescriptions.add(new DefaultAutoformPropertyDescription(serializableBean, beanPropertyDescriptor));
		}
		
		// generate the bean description
		return createBeanDescription(bean, propertyDescriptions);
		
	}
	
	/**
	 * @param bean the bean
	 * @return the bean, cast to {@link Serializable} type
	 */
	protected final Serializable enforceSerializableBean(Object bean) {
		try {
			return (Serializable)bean;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("bean is not serializable");
		}
	}
	
	/**
	 * Subclasses can override this method to control which properties are considered
	 * relevant to autoforms. See the description of this class for detailed
	 * information. In particular, properties excluded by this method must not
	 * be listed in {@link AutoformPropertyOrder} (if present).
	 * 
	 * The default implementation accepts all properties except "class".
	 * 
	 * @param beanPropertyDescriptor the descriptor for the property to check
	 * @return true to accept the property as relevant, false to consider it
	 * annotated with {@link AutoformIgnoreProperty} implicitly
	 */
	protected boolean acceptAsRelevant(PropertyDescriptor beanPropertyDescriptor) {
		return !"class".equals(beanPropertyDescriptor.getName());
	}

	/**
	 * Subclasses can override this method to control which properties are visible
	 * in an autoform. This allows to hide properties from outside a bean, but
	 * the properties are still considered relevant to autoforms. In particular,
	 * properties excluded by this method must still be listed by
	 * {@link AutoformPropertyOrder} (if present).
	 * 
	 * The default implementation does not exclude any property, i.e. it always
	 * returns true.
	 * 
	 * @param beanPropertyDescriptor the descriptor for the property to check
	 * @return true to show, false to hide
	 */
	protected boolean acceptAsVisible(PropertyDescriptor beanPropertyDescriptor) {
		return true;
	}
	
	/**
	 * Actually creates the bean description. Subclasses can override this method,
	 * for example, to post-process the property list.
	 * @param bean the bean
	 * @param propertyDescriptions the property descriptions
	 * @return the bean description
	 */
	protected DefaultAutoformBeanDescription createBeanDescription(Object bean, List<IAutoformPropertyDescription> propertyDescriptions) {
		return new DefaultAutoformBeanDescription(bean, propertyDescriptions);
	}
	
}
