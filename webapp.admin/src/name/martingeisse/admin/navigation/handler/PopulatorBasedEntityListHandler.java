/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.component.list.datatable.populator.AbstractPopulatorEntityDataTablePanel;
import name.martingeisse.admin.entity.component.list.datatable.populator.NavigationMountedPopulatorEntityDataTablePanel;
import name.martingeisse.admin.entity.component.list.datatable.populator.PopulatorColumnDescriptor;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.wicket.populator.FragmentPopulator;

/**
 * This handler allows to mount a populator-based list of entities in the navigation.
 * 
 * This class cannot be used together with {@link FragmentPopulator} since there is
 * no way to provide markup for the fragments. To use fragment populators, build
 * a custom subclass of {@link AbstractPopulatorEntityDataTablePanel} and mount
 * it with an {@link EntityListPanelHandler}.
 */
public class PopulatorBasedEntityListHandler extends EntityListPanelHandler {

	/**
	 * the columnDescriptors
	 */
	private PopulatorColumnDescriptor[] columnDescriptors;

	/**
	 * Constructor.
	 * @param entity the entity type to link to
	 * @param columnDescriptors the column descriptors
	 */
	public PopulatorBasedEntityListHandler(final EntityDescriptor entity, final PopulatorColumnDescriptor... columnDescriptors) {
		super(NavigationMountedPopulatorEntityDataTablePanel.class, entity);
		this.columnDescriptors = columnDescriptors;
	}

	/**
	 * Constructor.
	 * @param entityName the name of the entity to link to
	 * @param columnDescriptors the column descriptors
	 */
	public PopulatorBasedEntityListHandler(final String entityName, final PopulatorColumnDescriptor... columnDescriptors) {
		super(NavigationMountedPopulatorEntityDataTablePanel.class, entityName);
		this.columnDescriptors = columnDescriptors;
	}

	/**
	 * Getter method for the columnDescriptors.
	 * @return the columnDescriptors
	 */
	public PopulatorColumnDescriptor[] getColumnDescriptors() {
		return columnDescriptors;
	}

	/**
	 * Setter method for the columnDescriptors.
	 * @param columnDescriptors the columnDescriptors to set
	 * @return this for chaining
	 */
	public PopulatorBasedEntityListHandler setColumnDescriptors(final PopulatorColumnDescriptor[] columnDescriptors) {
		this.columnDescriptors = columnDescriptors;
		return this;
	}

}
