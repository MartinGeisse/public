/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import name.martingeisse.admin.entity.component.list.datatable.AbstractJsonRenderingEntityDataTablePanel;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * TODO: document me
 */
public class RenderedEntityDataTablePanelTest extends AbstractJsonRenderingEntityDataTablePanel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 */
	public RenderedEntityDataTablePanelTest(String id, IModel<EntityDescriptor> entityModel) {
		super(id, entityModel);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractJsonRenderingEntityDataTablePanel#getColumnTitles()
	 */
	@Override
	protected String[] getColumnTitles() {
		return new String[] {"foo", "bar", "fupp"};
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractJsonRenderingEntityDataTablePanel#populateRowItem(org.apache.wicket.markup.html.list.ListItem)
	 */
	@Override
	protected void populateRowItem(ListItem<EntityInstance> item) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractJsonRenderingEntityDataTablePanel#isSearchSupported()
	 */
	@Override
	protected boolean isSearchSupported() {
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
