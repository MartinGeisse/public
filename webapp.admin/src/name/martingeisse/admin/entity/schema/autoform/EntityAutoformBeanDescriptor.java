/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.autoform;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

import name.martingeisse.admin.entity.instance.RawEntityInstance;
import name.martingeisse.common.util.ClassKeyedContainer;
import name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriptor;
import name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriptor;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescriptor;

/**
 * {@link IAutoformBeanDescriptor} implementation for entity instances.
 */
public class EntityAutoformBeanDescriptor extends AbstractAutoformBeanDescriptor<RawEntityInstance> {

	/**
	 * Constructor.
	 * @param entityInstance the entity instance
	 * @param propertyDescriptors the property descriptors
	 */
	public EntityAutoformBeanDescriptor(final RawEntityInstance entityInstance, final List<IAutoformPropertyDescriptor> propertyDescriptors) {
		super(entityInstance, propertyDescriptors);
	}

	/**
	 * 
	 */
	private ClassKeyedContainer<Annotation> getAnnotationContainer() {
		return getBean().getEntity().getAnnotations();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriptor#getAnnotation(java.lang.Class)
	 */
	@Override
	public <A extends Annotation> A getAnnotation(final Class<A> annotationClass) {
		return getAnnotationContainer().get(annotationClass);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriptor#getAnnotations()
	 */
	@Override
	public Annotation[] getAnnotations() {
		Collection<Annotation> annotations = getAnnotationContainer().getValues();
		return annotations.toArray(new Annotation[annotations.size()]);
	}

}
