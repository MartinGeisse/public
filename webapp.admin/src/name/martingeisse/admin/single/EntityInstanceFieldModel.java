/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.single;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * This model refers to a field of an {@link EntityInstance}.
 * It works similar to a {@link PropertyModel}, with the 'container'
 * being either an {@link EntityInstance} or an appropriate
 * {@link IModel}.
 * @param <T> the field type
 */
public class EntityInstanceFieldModel<T> implements IModel<T> {

	/**
	 * the container
	 */
	private Object container;

	/**
	 * the fieldName
	 */
	private String fieldName;

	/**
	 * Constructor.
	 */
	public EntityInstanceFieldModel() {
	}

	/**
	 * Constructor.
	 * @param container the container
	 * @param fieldName the field name
	 */
	public EntityInstanceFieldModel(final Object container, final String fieldName) {
		this.container = container;
		this.fieldName = fieldName;
	}

	/**
	 * Getter method for the container.
	 * @return the container
	 */
	public Object getContainer() {
		return container;
	}

	/**
	 * Setter method for the container.
	 * @param container the container to set
	 */
	public void setContainer(final Object container) {
		this.container = container;
	}

	/**
	 * Getter method for the fieldName.
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Setter method for the fieldName.
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
		if (container instanceof IModel<?>) {
			final IModel<?> containerModel = (IModel<?>)container;
			containerModel.detach();
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#getObject()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getObject() {
		return (T)resolveInstance().getFieldValue(fieldName);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
	 */
	@Override
	public void setObject(final T object) {
		resolveInstance().setFieldValue(fieldName, object);
	}

	/**
	 * @return the entity instance
	 */
	private EntityInstance resolveInstance() {
		if (container instanceof IModel<?>) {
			final IModel<?> containerModel = (IModel<?>)container;
			return (EntityInstance)containerModel.getObject();
		} else {
			return (EntityInstance)container;
		}
	}

}
