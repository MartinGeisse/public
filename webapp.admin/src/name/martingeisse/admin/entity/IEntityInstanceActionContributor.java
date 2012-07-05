/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import java.io.Serializable;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * Implementations are provided by plugins and contribute actions
 * to entity instances.
 * 
 * Actions are contributed in two sets: Actions that are viable for
 * all instances of an entity, and specific actions for a single
 * instance. All actions are conceptually targeted at entity instances,
 * but providing actions in a centralized way for all instances
 * allows more sophisticated user interfaces and also reduces
 * memory consumption.
 * 
 * Entity descriptors and instances are passed twice: Once when
 * actions are contributed, and once when actions are executed.
 * This scheme is very flexible in various scenarios, such as:
 * - singleton actions that apply to all instances of potentially
 *   multiple entity types
 * - action objects that are specifically created for single
 *   entity instances
 * - per-instance selection of one of a set of singleton actions
 */
public interface IEntityInstanceActionContributor extends Serializable {

	/**
	 * Contributes global actions that can be executed on all instances
	 * of the specified descriptor.
	 * @param entityDescriptor the descriptor for the entity on whose instances
	 * the returned actions can be applied
	 * @return the actions. May return null instead of an empty array.
	 */
	public IEntityInstanceAction[] contributeGlobalActions(EntityDescriptor entityDescriptor);

	/**
	 * Contributes per-instance actions that can be executed only on the
	 * specified entity instance.
	 * @param entityInstance the entity instance on which the returned
	 * actions can be executed
	 * @param entityDescriptor the descriptor for the entity instance
	 * @return the actions. May return null instead of an empty array.
	 */
	public IEntityInstanceAction[] contributeInstanceActions(Object entityInstance, EntityDescriptor entityDescriptor);
	
}
