/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.model.IModel;

import com.mysema.query.types.Predicate;

/**
 * This class groups an entity descriptor and filtering predicate in
 * a single object.
 */
public final class EntitySelection {

	/**
	 * the entityModel
	 */
	private final IModel<EntityDescriptor> entityModel;

	/**
	 * the predicate
	 */
	private final Predicate predicate;

	/**
	 * Constructor.
	 * @param entityModel the model for the entity descriptor
	 */
	public EntitySelection(final IModel<EntityDescriptor> entityModel) {
		this.entityModel = entityModel;
		this.predicate = null;
	}

	/**
	 * Constructor.
	 * @param entityModel the model for the entity descriptor
	 * @param predicate the fetch predicate
	 */
	public EntitySelection(final IModel<EntityDescriptor> entityModel, final Predicate predicate) {
		this.entityModel = entityModel;
		this.predicate = predicate;
	}

	/**
	 * Getter method for the entityModel.
	 * @return the entityModel
	 */
	public IModel<EntityDescriptor> getEntityModel() {
		return entityModel;
	}

	/**
	 * Getter method for the predicate.
	 * @return the predicate
	 */
	public Predicate getPredicate() {
		return predicate;
	}

}
