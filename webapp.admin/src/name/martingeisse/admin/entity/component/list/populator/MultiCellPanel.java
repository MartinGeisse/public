/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.populator;

import java.util.List;

import name.martingeisse.admin.entity.instance.EntityInstance;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * A wicket panel that renders multiple cells to DIVs
 * using {@link ICellPopulator}s. This can be used as the
 * component added by a parent cell populator that groups
 * multiple sub-populators in a multi-cell.
 * 
 * Note that the sub-populators may be {@link ICellPopulator}s
 * of type {@link EntityInstance} -- they need not be
 * {@link IEntityCellPopulator}s -- since they do not need a title.
 */
public class MultiCellPanel extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the entity instance model
	 * @param cellPopulators the sub-populators
	 */
	public MultiCellPanel(final String id, final IModel<EntityInstance> model, final List<ICellPopulator<EntityInstance>> cellPopulators) {
		super(id, model);
		add(new ListView<ICellPopulator<EntityInstance>>("subcells", cellPopulators) {

			/* (non-Javadoc)
			 * @see org.apache.wicket.markup.html.list.ListView#newItem(int, org.apache.wicket.model.IModel)
			 */
			@Override
			protected ListItem<ICellPopulator<EntityInstance>> newItem(final int index, final IModel<ICellPopulator<EntityInstance>> itemModel) {
				return new Item<ICellPopulator<EntityInstance>>(Integer.toString(index), index, itemModel);
			}

			@Override
			protected void populateItem(final ListItem<ICellPopulator<EntityInstance>> item) {
				item.getModelObject().populateItem((Item<ICellPopulator<EntityInstance>>)item, "subcell", getEntityInstanceModel());
			}

		});
	}

	/**
	 * @return the model for the entity instance
	 */
	@SuppressWarnings("unchecked")
	public IModel<EntityInstance> getEntityInstanceModel() {
		return (IModel<EntityInstance>)getDefaultModel();
	}

}
