/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.multi;

import java.util.Arrays;
import java.util.List;

import name.martingeisse.admin.entity.multi.populator.EntityFieldPopulator;
import name.martingeisse.admin.entity.multi.populator.FragmentPopulator;
import name.martingeisse.admin.entity.multi.populator.IEntityCellPopulator;
import name.martingeisse.admin.entity.multi.populator.PopulatorBasedEntityListPanel;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.single.EntityInstance;
import name.martingeisse.admin.entity.single.EntityInstanceFieldModel;

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
	 * @param entity the entity
	 * @param parameters the page parameters
	 */
	public PopulatorDataViewPanel(String id, EntityDescriptor entity, PageParameters parameters) {
		super(id, entity, null);
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
