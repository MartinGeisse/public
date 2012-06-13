/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * This class contains the information about a single property
 * in an autoform, using a Java Beans {@link PropertyDescriptor}.
 */
public final class DefaultAutoformPropertyDescriptor extends AbstractAutoformPropertyDescriptor {

	/**
	 * the bean
	 */
	private Object bean;
	
	/**
	 * the propertyDescriptor
	 */
	private transient PropertyDescriptor propertyDescriptor;

	/**
	 * Creates an instance from a Java Bean property.
	 * 
	 * This class remembers the bean and property name to reconstruct a property descriptor
	 * when de-serialized.
	 * 
	 * @param bean the bean that contains the property
	 * @param propertyDescriptor the property descriptor for the bean property
	 */
	public DefaultAutoformPropertyDescriptor(Serializable bean, PropertyDescriptor propertyDescriptor) {
		super(bean, propertyDescriptor);
		this.bean = bean;
		this.propertyDescriptor = propertyDescriptor;
	}
	
	/**
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		try {
			this.propertyDescriptor = PropertyUtils.getPropertyDescriptor(bean, getName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.AbstractAutoformPropertyDescriptor#isReadOnlyByDefault()
	 */
	@Override
	protected boolean isReadOnlyByDefault() {
		return (propertyDescriptor.getWriteMethod() == null) || (getAnnotationProvider().getAnnotation(AutoformReadOnly.class) != null);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.AbstractAutoformPropertyDescriptor#getAnnotationProvider()
	 */
	@Override
	public AnnotatedElement getAnnotationProvider() {
		return propertyDescriptor.getReadMethod();
	}

}
