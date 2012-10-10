/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable.populator;

import java.util.Arrays;
import java.util.List;

import name.martingeisse.admin.entity.EntitySelection;
import name.martingeisse.admin.entity.component.list.datatable.render.AbstractJsonRenderingEntityDataTablePanel;
import name.martingeisse.admin.entity.instance.IEntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.wicket.util.json.JsonEncodingContainer;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * This table implementation is centered around {@link ICellPopulator} objects that render
 * entity fields to table cells. The populators are stored in the column descriptors.
 * 
 * TODO: parameter check / return value check: ab hier weiterprüfen
 * 
 * @param <CD> the column descriptor type
 */
public abstract class AbstractPopulatorEntityDataTablePanel<CD extends PopulatorColumnDescriptor> extends AbstractJsonRenderingEntityDataTablePanel<CD> {

	/**
	 * the populatorListModel
	 */
	private transient IModel<List<ICellPopulator<IEntityInstance>>> populatorListModel;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 */
	public AbstractPopulatorEntityDataTablePanel(String id, IModel<EntityDescriptor> entityModel) {
		super(id, entityModel);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param selection the entity selection
	 */
	public AbstractPopulatorEntityDataTablePanel(final String id, final EntitySelection selection) {
		super(id, selection);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.datatable.render.AbstractJsonRenderingEntityDataTablePanel#onDetach()
	 */
	@Override
	protected void onDetach() {
		populatorListModel = null;
		super.onDetach();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.datatable.render.AbstractJsonRenderingEntityDataTablePanel#populateRowItem(org.apache.wicket.markup.repeater.Item)
	 */
	@Override
	protected void populateRowItem(ListItem<IEntityInstance> item) {
		
		// use a list model for the cell populators that is shared between all rows
		if (populatorListModel == null) {
			populatorListModel = new AbstractReadOnlyModel<List<ICellPopulator<IEntityInstance>>>() {
				@Override
				public List<ICellPopulator<IEntityInstance>> getObject() {
					ICellPopulator<IEntityInstance>[] populators = getColumnDescriptors();
					return Arrays.asList(populators);
				}
			};
		}
		
		// pass the model for the entity instance to the cell items
		final IModel<IEntityInstance> instanceModel = item.getModel();
		
		// create a list view for the cells
		item.add(new ListView<ICellPopulator<IEntityInstance>>("cells", populatorListModel) {
			
			@Override
			protected ListItem<ICellPopulator<IEntityInstance>> newItem(int index, IModel<ICellPopulator<IEntityInstance>> itemModel) {
				return new JsonCellItem(Integer.toString(index), index, itemModel);
			}
			
			@Override
			protected void populateItem(ListItem<ICellPopulator<IEntityInstance>> item) {
				item.getModelObject().populateItem((Item<ICellPopulator<IEntityInstance>>)item, "contents", instanceModel);
			}
			
			@Override
			protected void renderItem(ListItem<?> item) {
				if (item.getIndex() > 0) {
					getResponse().write(",");
				}
				super.renderItem(item);
			}
			
		});
		
	}

	/**
	 * This class has a {@link JsonEncodingContainer} child and delegates the add()
	 * method for new components to that child.
	 */
	private static class JsonCellItem extends Item<ICellPopulator<IEntityInstance>> {

		JsonCellItem(String id, int index, IModel<ICellPopulator<IEntityInstance>> model) {
			super(id, index, model);
			super.add(new JsonEncodingContainer("jsonEncoder"));
		}

		@Override
		public MarkupContainer add(Component... childs) {
			return ((WebMarkupContainer)get(0)).add(childs);
		}
		
		@Override
		public MarkupContainer addOrReplace(Component... childs) {
			return ((WebMarkupContainer)get(0)).addOrReplace(childs);
		}
		
	}
	
}
