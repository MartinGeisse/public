/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * This model is basically a model for an {@link EntityDescriptor}, but stores the
 * entity name separately to be serializable.
 */
public final class EntityDescriptorModel extends LoadableDetachableModel<EntityDescriptor> {

	/**
	 * the entityName
	 */
	private final String entityName;

	/**
	 * Constructor.
	 * @param entityName the entity name
	 */
	public EntityDescriptorModel(final String entityName) {
		super();
		this.entityName = entityName;
	}

	/**
	 * Constructor.
	 * @param entity the entity
	 */
	public EntityDescriptorModel(final EntityDescriptor entity) {
		super(entity);
		this.entityName = entity.getName();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected EntityDescriptor load() {
		return ApplicationSchema.instance.findEntity(entityName);
	}

}
