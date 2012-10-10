/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import name.martingeisse.admin.entity.component.list.datatable.render.AbstractJsonRenderingEntityDataTablePanel;
import name.martingeisse.admin.entity.component.list.datatable.render.RenderingColumnDescriptor;
import name.martingeisse.admin.entity.instance.IEntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * TODO: document me
 */
public class RenderedEntityDataTablePanelTest extends AbstractJsonRenderingEntityDataTablePanel<RenderingColumnDescriptor> {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 */
	public RenderedEntityDataTablePanelTest(String id, IModel<EntityDescriptor> entityModel) {
		super(id, entityModel);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.datatable.AbstractEntityDataTablePanel#determineColumnDescriptors()
	 */
	@Override
	protected RenderingColumnDescriptor[] determineColumnDescriptors() {
		return new RenderingColumnDescriptor[] {
			new RenderingColumnDescriptor("foo"),
			new RenderingColumnDescriptor("bar"),
			new RenderingColumnDescriptor("fupp"),
		};
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.datatable.render.AbstractJsonRenderingEntityDataTablePanel#populateRowItem(org.apache.wicket.markup.repeater.Item)
	 */
	@Override
	protected void populateRowItem(ListItem<IEntityInstance> item) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractJsonRenderingEntityDataTablePanel#isSearchSupported()
	 */
	@Override
	protected boolean isSearchSupported() {
		return false;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.datatable.AbstractEntityDataTablePanel#isColumnSortable(int)
	 */
	@Override
	protected boolean isColumnSortable(int columnIndex) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel#getColumnSortExpression(int)
	 */
	@Override
	protected Expression<Comparable<?>> getColumnSortExpression(int columnIndex) {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel#getSearchPredicate(java.lang.String)
	 */
	@Override
	protected Predicate getSearchPredicate(String searchTerm) {
		return null;
	}

}
