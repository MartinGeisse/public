/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import java.io.Serializable;

import name.martingeisse.admin.multi.IGlobalEntityListPresenter;
import name.martingeisse.admin.schema.EntityDescriptor;
import name.martingeisse.admin.single.ISingleEntityOverviewPresenter;
import name.martingeisse.admin.single.ISingleEntityPresenter;

/**
 * Implementations are added by plugins. They contribute
 * {@link ISingleEntityOverviewPresenter}, {@link ISingleEntityPresenter}
 * and {@link IGlobalEntityListPresenter} objects for a given
 * {@link EntityDescriptor}.
 */
public interface IEntityPresentationContributor extends Serializable {

	/**
	 * Contributes entity presenters to the specified entity. Presenters
	 * may be created by this method or they may be shared presenters
	 * that are just added to the entity.
	 * 
	 * @param entity the entity to contribute presenters to
	 */
	public void contributeEntityPresenters(EntityDescriptor entity);

}
