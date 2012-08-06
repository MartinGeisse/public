/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.model;

import org.apache.wicket.model.LoadableDetachableModel;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * This model is basically a model for an {@link EntityDescriptor}, but stores the
 * entity name separately to be serializable.
 */
public final class EntityDescriptorModel extends LoadableDetachableModel<EntityDescriptor> {

	/**
	 * the entityName
	 */
	private String entityName;
	
	/**
	 * Constructor.
	 * @param entityName the entity name
	 */
	public EntityDescriptorModel(String entityName) {
		super();
		this.entityName = entityName;
	}
	
	/**
	 * Constructor.
	 * @param entity the entity
	 */
	public EntityDescriptorModel(EntityDescriptor entity) {
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
