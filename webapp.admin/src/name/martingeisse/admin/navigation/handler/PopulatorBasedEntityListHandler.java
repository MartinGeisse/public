/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.component.list.populator.PopulatorBasedEntityListPanel;
import name.martingeisse.admin.entity.component.list.populator.FragmentPopulator;
import name.martingeisse.admin.entity.component.list.populator.IEntityCellPopulator;
import name.martingeisse.admin.entity.component.list.populator.NavigationMountedPopulatorBasedEntityListPanel;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * This handler allows to mount a populator-based list of entities in the navigation.
 * 
 * This class cannot be used together with {@link FragmentPopulator} since there is
 * no way to provide markup for the fragments. To use fragment populators, build
 * a custom subclass of {@link PopulatorBasedEntityListPanel} and mount
 * it with an {@link EntityListPanelHandler}.
 */
public class PopulatorBasedEntityListHandler extends EntityListPanelHandler {

	/**
	 * the populators
	 */
	private IEntityCellPopulator[] populators;

	/**
	 * Constructor.
	 * @param entity the entity type to link to
	 * @param populators the populators to use
	 */
	public PopulatorBasedEntityListHandler(final EntityDescriptor entity, final IEntityCellPopulator... populators) {
		super(NavigationMountedPopulatorBasedEntityListPanel.class, entity);
		this.populators = populators;
	}

	/**
	 * Constructor.
	 * @param entityName the name of the entity to link to
	 * @param populators the populators to use
	 */
	public PopulatorBasedEntityListHandler(final String entityName, final IEntityCellPopulator... populators) {
		super(NavigationMountedPopulatorBasedEntityListPanel.class, entityName);
		this.populators = populators;
	}

	/**
	 * Getter method for the populators.
	 * @return the populators
	 */
	public IEntityCellPopulator[] getPopulators() {
		return populators;
	}

	/**
	 * Setter method for the populators.
	 * @param populators the populators to set
	 * @return this for chaining
	 */
	public PopulatorBasedEntityListHandler setPopulators(final IEntityCellPopulator[] populators) {
		this.populators = populators;
		return this;
	}

}
