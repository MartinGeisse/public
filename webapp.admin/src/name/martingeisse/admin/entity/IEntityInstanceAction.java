/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import java.io.Serializable;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * An action that can be executed on an entity instance. Note that
 * implementations may or may not be tied to single instances, in
 * which case they must not be executed on other instances. See
 * {@link IEntityInstanceActionContributor} for the idea behind this.
 */
public interface IEntityInstanceAction extends Serializable {

	/**
	 * @return the name of this action
	 */
	public String getName();
	
	/**
	 * Checks whether this action is active for the specified entity instance.
	 * If the action is inactive for an entity instance, execute() must not be
	 * used on that instance, and the corresponding UI widget should be disabled.
	 * @param entityInstance the entity instance to check for
	 * @param entityDescriptor the entity descriptor
	 * @return true if this action is active for the specified instance, false
	 * if inactive.
	 */
	public boolean isActiveFor(Object entityInstance, EntityDescriptor entityDescriptor);
	
	/**
	 * Executes this action on the specified instance.
	 * @param entityInstance the entity instance
	 * @param entityDescriptor the entity descriptor
	 */
	public void execute(Object entityInstance, EntityDescriptor entityDescriptor);
	
}
