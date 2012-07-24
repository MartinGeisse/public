/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.multi;

import java.util.Arrays;
import java.util.List;

import name.martingeisse.admin.entity.component.list.populator.EntityFieldPopulator;
import name.martingeisse.admin.entity.component.list.populator.FragmentPopulator;
import name.martingeisse.admin.entity.component.list.populator.IEntityCellPopulator;
import name.martingeisse.admin.entity.component.list.populator.PopulatorBasedEntityListPanel;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.instance.EntityInstanceFieldModel;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This panel tests mixed populator / data view based entity list rendering.
 */
public class PopulatorDataViewPanel extends PopulatorBasedEntityListPanel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 * @param parameters the page parameters
	 */
	public PopulatorDataViewPanel(String id, IModel<EntityDescriptor> entityModel, PageParameters parameters) {
		super(id, entityModel);
		setCellPopulators(createPopulatorList());
	}

	/**
	 * @return
	 */
	private List<IEntityCellPopulator> createPopulatorList() {
		return Arrays.<IEntityCellPopulator>asList(
			new EntityFieldPopulator("Role Description", "role_description"),
			new FragmentPopulator(this, "frag1") {
				@Override
				protected void populateFragment(Fragment fragment, IModel<EntityInstance> rowModel) {
					fragment.add(new Label("field", new EntityInstanceFieldModel<String>(rowModel, "role_description")));
				};
			}
		);
	}
	
}
