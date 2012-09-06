/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.autoform;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.instance.EntityInstanceFieldModel;
import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;
import name.martingeisse.common.util.ClassKeyedContainer;
import name.martingeisse.wicket.autoform.describe.AbstractAutoformPropertyDescriptor;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescriptor;

/**
 * {@link IAutoformPropertyDescriptor} implementation for entity instances.
 */
public class EntityAutoformPropertyDescriptor extends AbstractAutoformPropertyDescriptor {

	/**
	 * the entityInstance
	 */
	private final EntityInstance entityInstance;
	
	/**
	 * the propertyDescriptor
	 */
	private transient EntityPropertyDescriptor propertyDescriptor;
	
	/**
	 * Constructor.
	 * @param entityInstance the entity instance
	 * @param propertyDescriptor the property descriptor
	 */
	public EntityAutoformPropertyDescriptor(EntityInstance entityInstance, EntityPropertyDescriptor propertyDescriptor) {
		super(propertyDescriptor.getName(), propertyDescriptor.getType().getJavaType(), new EntityInstanceFieldModel<Object>(entityInstance, propertyDescriptor.getName()));
		this.entityInstance = entityInstance;
	}

	/**
	 * 
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		try {
			this.propertyDescriptor = entityInstance.getEntity().getPropertiesByName().get(getName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 */
	private ClassKeyedContainer<Annotation> getAnnotationContainer() {
		return propertyDescriptor.getAnnotations();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescriptor#getAnnotation(java.lang.Class)
	 */
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return getAnnotationContainer().get(annotationClass);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescriptor#getAnnotations()
	 */
	@Override
	public Annotation[] getAnnotations() {
		Collection<Annotation> annotations = getAnnotationContainer().getValues();
		return annotations.toArray(new Annotation[annotations.size()]);
	}
	
}
