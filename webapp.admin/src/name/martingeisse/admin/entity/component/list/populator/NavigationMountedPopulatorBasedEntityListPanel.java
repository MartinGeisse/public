/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.populator;

import java.util.Arrays;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.NavigationUtil;
import name.martingeisse.admin.navigation.handler.EntityListPanelHandler;
import name.martingeisse.admin.navigation.handler.PopulatorBasedEntityListHandler;
import name.martingeisse.admin.util.IGetPageable;

import org.apache.wicket.model.IModel;

/**
 * Populator-based presentation of entities. The populators are taken from the navigation node.
 * 
 * This class cannot be used together with {@link FragmentPopulator} since there is
 * no way to provide markup for the fragments. To use fragment populators, build
 * a custom subclass of {@link PopulatorBasedEntityListPanel} and mount
 * it with an {@link EntityListPanelHandler}.
 */
public class NavigationMountedPopulatorBasedEntityListPanel extends PopulatorBasedEntityListPanel implements IGetPageable {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity descriptor model
	 */
	public NavigationMountedPopulatorBasedEntityListPanel(final String id, final IModel<EntityDescriptor> entityModel) {
		super(id, entityModel);
	}

	/**
	 * @return the navigation node handler
	 */
	private PopulatorBasedEntityListHandler getHandler() {
		return (PopulatorBasedEntityListHandler)(NavigationUtil.getNavigationNodeForComponent(this).getHandler());
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		setCellPopulators(Arrays.asList(getHandler().getPopulators()));
		super.onInitialize();
	}

}
