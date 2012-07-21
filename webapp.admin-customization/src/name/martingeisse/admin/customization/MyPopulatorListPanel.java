/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import java.util.Arrays;
import java.util.List;

import name.martingeisse.admin.entity.component.list.populator.EntityFieldPopulator;
import name.martingeisse.admin.entity.component.list.populator.IEntityCellPopulator;
import name.martingeisse.admin.entity.component.list.populator.MultiCellPopulator;
import name.martingeisse.admin.entity.component.list.populator.PopulatorBasedEntityListPanel;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.model.IModel;

/**
 *
 */
public class MyPopulatorListPanel extends PopulatorBasedEntityListPanel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 */
	public MyPopulatorListPanel(String id, IModel<EntityDescriptor> entityModel) {
		super(id, entityModel, createPopulators());
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private static List<IEntityCellPopulator> createPopulators() {
		IEntityCellPopulator description = new EntityFieldPopulator("Role Description", "role_description");
		IEntityCellPopulator order = new EntityFieldPopulator("Role Order", "role_order");
		IEntityCellPopulator multi = new MultiCellPopulator("Test", new EntityFieldPopulator(null, "role_description"), new EntityFieldPopulator(null, "role_order"));
		return Arrays.<IEntityCellPopulator> asList(description, order, multi);
	}

}
