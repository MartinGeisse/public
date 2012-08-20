/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable.populator;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.NavigationUtil;
import name.martingeisse.admin.navigation.handler.PopulatorBasedEntityListHandler2;

import org.apache.wicket.model.IModel;

/**
 * Navigation-mounted implementation of {@link AbstractPopulatorEntityDataTablePanel}.
 * This implementation obtains the column descriptors from the navigation node handler.
 * 
 * For details on how the navigation node for this component is found, see
 * {@link NavigationUtil#getNavigationNodeForComponent(org.apache.wicket.Component)}.
 */
public class NavigationMountedPopulatorEntityDataTablePanel extends AbstractPopulatorEntityDataTablePanel<PopulatorColumnDescriptor> {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 */
	public NavigationMountedPopulatorEntityDataTablePanel(final String id, final IModel<EntityDescriptor> entityModel) {
		super(id, entityModel);
	}

	/**
	 * @return the navigation node handler
	 */
	private PopulatorBasedEntityListHandler2 getHandler() {
		return (PopulatorBasedEntityListHandler2)(NavigationUtil.getNavigationNodeForComponent(this).getHandler());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.datatable.AbstractEntityDataTablePanel#determineColumnDescriptors()
	 */
	@Override
	protected PopulatorColumnDescriptor[] determineColumnDescriptors() {
		return getHandler().getColumnDescriptors();
	}

}
