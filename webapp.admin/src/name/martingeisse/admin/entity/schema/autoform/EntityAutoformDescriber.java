/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.autoform;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.wicket.autoform.annotation.structure.AutoformIgnoreProperty;
import name.martingeisse.wicket.autoform.annotation.structure.AutoformPropertyOrder;
import name.martingeisse.wicket.autoform.describe.DefaultAutoformBeanDescriber;
import name.martingeisse.wicket.autoform.describe.DefaultAutoformBeanDescription;
import name.martingeisse.wicket.autoform.describe.DefaultAutoformPropertyDescription;
import name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriber;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription;

/**
 * This class is a variant {@link DefaultAutoformBeanDescriber} that finds
 * properties from entity descriptors and annotations from the
 * autoform meta-data stored in those descriptors.
 * 
 * The "beans" described by this object are {@link EntityInstance} objects.
 */
public class EntityAutoformDescriber implements IAutoformBeanDescriber {

	/**
	 * the default instance of this class
	 */
	public static final EntityAutoformDescriber instance = new EntityAutoformDescriber();
	
	/**
	 * Prevent further instantiation of this class, but allow subclass instances.
	 */
	protected EntityAutoformDescriber() {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformBeanDescriber#describe(java.lang.Object)
	 */
	@Override
	public DefaultAutoformBeanDescription describe(Object bean) {
		
		// analyze the bean
		EntityInstance entityInstance = enforceEntityInstanceBean(bean);
		List<PropertyDescriptor> sortedVisibleBeanPropertyDescriptors = null;//new DefaultAutoformBeanDescriberHelper(this, serializableBean.getClass()).run();
		
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
	 * @return the bean, cast to {@link EntityInstance} type
	 */
	protected final EntityInstance enforceEntityInstanceBean(Object bean) {
		try {
			return (EntityInstance)bean;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("bean is not an EntityInstance");
		}
	}
	
	/**
	 * Subclasses can override this method to control which properties are considered
	 * relevant to autoforms. See the description of this class for detailed
	 * information. In particular, properties excluded by this method must not
	 * be listed in {@link AutoformPropertyOrder} (if present).
	 * 
	 * The default implementation accepts all properties.
	 * 
	 * @param beanPropertyDescriptor the descriptor for the property to check
	 * @return true to accept the property as relevant, false to consider it
	 * annotated with {@link AutoformIgnoreProperty} implicitly
	 */
	protected boolean acceptAsRelevant(PropertyDescriptor beanPropertyDescriptor) {
		return true;
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
