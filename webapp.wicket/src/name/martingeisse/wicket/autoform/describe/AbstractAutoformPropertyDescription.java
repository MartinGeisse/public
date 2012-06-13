/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.beans.PropertyDescriptor;
import java.io.Serializable;

import name.martingeisse.common.terms.DisplayName;
import name.martingeisse.wicket.autoform.annotation.AutoformComponent;
import name.martingeisse.wicket.autoform.annotation.AutoformReadOnly;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * This class implements basic fields defined in {@link DefaultAutoformPropertyDescription}
 * as well as fields automatically derived from annotations.
 */
public abstract class AbstractAutoformPropertyDescription implements IAutoformPropertyDescription {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * the type
	 */
	private final Class<?> type;

	/**
	 * the model
	 */
	private final IModel<?> model;

	/**
	 * Constructor.
	 * @param name the name of the property
	 * @param type the Java type of the property
	 * @param model the Wicket model used to read/write the property value
	 */
	public AbstractAutoformPropertyDescription(final String name, final Class<?> type, final IModel<?> model) {
		this.name = name;
		this.type = type;
		this.model = model;
	}

	/**
	 * Creates an instance from a Java Bean property.
	 * @param bean the bean that contains the property
	 * @param propertyDescriptor the property descriptor for the bean property. The property descriptor
	 * is not saved by this class since that would hinder serializability.
	 */
	public AbstractAutoformPropertyDescription(final Serializable bean, final PropertyDescriptor propertyDescriptor) {
		this(propertyDescriptor.getName(), propertyDescriptor.getPropertyType(), new PropertyModel<Object>(bean, propertyDescriptor.getName()));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		final DisplayName annotation = getAnnotation(DisplayName.class);
		return (annotation == null) ? getName() : annotation.value();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription#getType()
	 */
	@Override
	public Class<?> getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription#getModel()
	 */
	@Override
	public IModel<?> getModel() {
		return model;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return (getAnnotation(AutoformReadOnly.class) != null);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription#getComponentClassOverride()
	 */
	@Override
	public Class<? extends Component> getComponentClassOverride() {
		final AutoformComponent annotation = getAnnotation(AutoformComponent.class);
		return (annotation == null) ? null : annotation.value();
	}

}
