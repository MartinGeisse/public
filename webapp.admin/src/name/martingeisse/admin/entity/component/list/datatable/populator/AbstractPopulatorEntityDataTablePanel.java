/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable.populator;

import java.util.Arrays;
import java.util.List;

import name.martingeisse.admin.entity.component.list.datatable.render.AbstractJsonRenderingEntityDataTablePanel;
import name.martingeisse.admin.entity.instance.EntityInstance;
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

import com.mysema.query.types.Expression;

/**
 * This table implementation is centered around {@link ICellPopulator} objects that render
 * entity fields to table cells. The populators are stored in the column descriptors.
 * 
 * @param <CD> the column descriptor type
 */
public abstract class AbstractPopulatorEntityDataTablePanel<CD extends PopulatorColumnDescriptor> extends AbstractJsonRenderingEntityDataTablePanel<CD> {

	/**
	 * the populatorListModel
	 */
	private transient IModel<List<ICellPopulator<EntityInstance>>> populatorListModel;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 */
	public AbstractPopulatorEntityDataTablePanel(String id, IModel<EntityDescriptor> entityModel) {
		super(id, entityModel);
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
	protected void populateRowItem(ListItem<EntityInstance> item) {
		
		// use a list model for the cell populators that is shared between all rows
		if (populatorListModel == null) {
			populatorListModel = new AbstractReadOnlyModel<List<ICellPopulator<EntityInstance>>>() {
				@Override
				public List<ICellPopulator<EntityInstance>> getObject() {
					ICellPopulator<EntityInstance>[] populators = getColumnDescriptors();
					return Arrays.asList(populators);
				}
			};
		}
		
		// pass the model for the entity instance to the cell items
		final IModel<EntityInstance> instanceModel = item.getModel();
		
		// create a list view for the cells
		item.add(new ListView<ICellPopulator<EntityInstance>>("cells", populatorListModel) {
			
			@Override
			protected ListItem<ICellPopulator<EntityInstance>> newItem(int index, IModel<ICellPopulator<EntityInstance>> itemModel) {
				return new JsonCellItem(Integer.toString(index), index, itemModel);
			}
			
			@Override
			protected void populateItem(ListItem<ICellPopulator<EntityInstance>> item) {
				item.getModelObject().populateItem((Item<ICellPopulator<EntityInstance>>)item, "contents", instanceModel);
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
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.datatable.AbstractEntityDataTablePanel#getColumnSortExpression(int)
	 */
	@Override
	protected Expression<Comparable<?>> getColumnSortExpression(int columnIndex) {
		return null; // TODO -- if this comes from the CD, maybe move back to AbstractJsonRenderingEntityDataTablePanel
		// since it should work there too
	}

	/**
	 * This class has a {@link JsonEncodingContainer} child and delegates the add()
	 * method for new components to that child.
	 */
	private static class JsonCellItem extends Item<ICellPopulator<EntityInstance>> {

		JsonCellItem(String id, int index, IModel<ICellPopulator<EntityInstance>> model) {
			super(id, index, model);
			super.add(new JsonEncodingContainer("jsonEncoder"));
//			super.add(new WebMarkupContainer("jsonEncoder"));
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
