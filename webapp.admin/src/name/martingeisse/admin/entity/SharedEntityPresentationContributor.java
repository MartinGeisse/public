/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import java.util.List;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.entity.multi.IGlobalEntityListPresenter;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.common.computation.predicate.IPredicate;

/**
 * Simple implementation of {@link IEntityPresentationContributor}.
 * This implementation uses a {@link IPredicate} to detect
 * entities it applies to, and adds a set of shared
 * {@link IGlobalEntityListPresenter} instances to all matched entities.
 * 
 * The predicate may be null. In this case the presenters are
 * added to all entities.
 * 
 * This class also implements {@link IPlugin} to allow convenient usage
 * outside any other plugin.
 */
public class SharedEntityPresentationContributor implements IEntityPresentationContributor, IPlugin {

	/**
	 * the entityPredicate
	 */
	private IPredicate<EntityDescriptor> entityPredicate;

	/**
	 * the listPresenters
	 */
	private IGlobalEntityListPresenter[] listPresenters;

	/**
	 * Constructor.
	 */
	public SharedEntityPresentationContributor() {
	}

	/**
	 * Constructor.
	 * @param entityPredicate the predicate that determines to which entities this contributor applies
	 * @param presenters the presenters to return
	 */
	public SharedEntityPresentationContributor(final IPredicate<EntityDescriptor> entityPredicate, final IGlobalEntityListPresenter... presenters) {
		this.entityPredicate = entityPredicate;
		this.listPresenters = presenters;
	}

	/**
	 * Getter method for the entityPredicate.
	 * @return the entityPredicate
	 */
	public IPredicate<EntityDescriptor> getEntityPredicate() {
		return entityPredicate;
	}

	/**
	 * Setter method for the entityPredicate.
	 * @param entityPredicate the entityPredicate to set
	 */
	public void setEntityPredicate(final IPredicate<EntityDescriptor> entityPredicate) {
		this.entityPredicate = entityPredicate;
	}

	/**
	 * Getter method for the listPresenters.
	 * @return the listPresenters
	 */
	public IGlobalEntityListPresenter[] getListPresenters() {
		return listPresenters;
	}

	/**
	 * Setter method for the listPresenters.
	 * @param listPresenters the listPresenters to set
	 */
	public void setListPresenters(final IGlobalEntityListPresenter[] listPresenters) {
		this.listPresenters = listPresenters;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityPresenterContributor#contributeEntityPresenters(name.martingeisse.admin.schema.EntityDescriptor)
	 */
	@Override
	public void contributeEntityPresenters(final EntityDescriptor entity) {
		if (entityPredicate == null || entityPredicate.evaluate(entity)) {
			addAll(listPresenters, entity.getGlobalListPresenters());
		}
	}

	/**
	 * 
	 */
	private static <T> void addAll(T[] source, final List<T> destination) {
		if (source != null) {
			for (final T element : source) {
				destination.add(element);
			}
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute() {
		EntityConfigurationUtil.addEntityPresentationContributor(this);
	}

}
