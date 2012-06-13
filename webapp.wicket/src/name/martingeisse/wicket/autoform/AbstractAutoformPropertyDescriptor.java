/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;

import name.martingeisse.common.terms.DisplayName;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * This class implements basic fields defined in {@link DefaultAutoformPropertyDescriptor}.
 */
public abstract class AbstractAutoformPropertyDescriptor implements IAutoformPropertyDescriptor {

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
	 * the beanModel
	 */
	private final IModel<?> beanModel;

	/**
	 * the readOnlyOverride
	 */
	private Boolean readOnlyOverride;
	
	/**
	 * the displayNameOverride
	 */
	private String displayNameOverride;

	/**
	 * the assignedComponent
	 */
	private Panel assignedComponent;

	/**
	 * Constructor.
	 * @param name the name of the property
	 * @param type the Java type of the property
	 * @param model the Wicket model used to read/write the property value
	 * @param beanModel the Wicket model used to read the enclosing bean (optional; needed only by a few components)
	 * @param readOnlyOverride whether the property is read-only
	 */
	public AbstractAutoformPropertyDescriptor(final String name, final Class<?> type, final IModel<?> model, final IModel<?> beanModel, final Boolean readOnlyOverride) {
		this.name = name;
		this.type = type;
		this.model = model;
		this.beanModel = beanModel;
		this.readOnlyOverride = readOnlyOverride;
	}

	/**
	 * Creates an instance from a Java Bean property.
	 * @param bean the bean that contains the property
	 * @param propertyDescriptor the property descriptor for the bean property. The property descriptor
	 * is not saved by this class since that would hinder serializability.
	 */
	public AbstractAutoformPropertyDescriptor(final Serializable bean, final PropertyDescriptor propertyDescriptor) {
		this(propertyDescriptor.getName(), propertyDescriptor.getPropertyType(), new PropertyModel<Object>(bean, propertyDescriptor.getName()), Model.of(bean), null);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#getType()
	 */
	@Override
	public Class<?> getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#getModel()
	 */
	@Override
	public IModel<?> getModel() {
		return model;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#getBeanModel()
	 */
	@Override
	public IModel<?> getBeanModel() {
		return beanModel;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#getReadOnlyOverride()
	 */
	@Override
	public Boolean getReadOnlyOverride() {
		return readOnlyOverride;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#setReadOnlyOverride(java.lang.Boolean)
	 */
	@Override
	public void setReadOnlyOverride(final Boolean readOnlyOverride) {
		this.readOnlyOverride = readOnlyOverride;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#getDisplayNameOverride()
	 */
	@Override
	public String getDisplayNameOverride() {
		return displayNameOverride;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#setDisplayNameOverride(java.lang.String)
	 */
	@Override
	public void setDisplayNameOverride(String displayNameOverride) {
		this.displayNameOverride = displayNameOverride;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#getAssignedComponent()
	 */
	@Override
	public Panel getAssignedComponent() {
		return assignedComponent;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#setAssignedComponent(org.apache.wicket.markup.html.panel.Panel)
	 */
	@Override
	public void setAssignedComponent(final Panel assignedComponent) {
		this.assignedComponent = assignedComponent;
	}


	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return (readOnlyOverride == null) ? isReadOnlyByDefault() : readOnlyOverride;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		if (displayNameOverride == null) {
			final AnnotatedElement annotationProvider = getAnnotationProvider();
			final DisplayName annotation = annotationProvider.getAnnotation(DisplayName.class);
			return (annotation == null) ? name : annotation.value();
		} else {
			return displayNameOverride;
		}
	}

	/**
	 * This method determines whether the property is read-only by itself, ignoring
	 * the read-only override.
	 */
	protected abstract boolean isReadOnlyByDefault();

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyDescriptor#getAnnotationProvider()
	 */
	@Override
	public abstract AnnotatedElement getAnnotationProvider();
	
}
