/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable.render;

import java.util.Iterator;
import java.util.List;

import name.martingeisse.admin.entity.component.list.datatable.AbstractEntityDataTablePanel;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.common.javascript.JavascriptAssembler;

import org.apache.commons.collections.IteratorUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.response.StringResponse;

import com.mysema.query.types.Expression;

/**
 * This DataTable implementation requires its subclass to directly render
 * the JSON data for each row that is sent to the client's DataTable.
 * 
 * The subclass must assume that this class produces square brackets for the
 * JSON array that represents the current row, and must produce the
 * comma-separated JSON values for that array in its wicket:extend element.
 * This class invokes populateRowItem() for each row to add subcomponents.
 * 
 * One common way to produce the values is to use wicket:json to generate
 * a JSON string from a piece of component-aware markup. Any text as well as
 * HTML tags in such markup are JSON-encoded, decoded again by the DataTable,
 * and will be recognized as HTML tags by the browser when the DataTable
 * inserts them into the table cells. This allows to fill table cells with
 * arbitrary markup and components conveniently, using wicket:json to denote
 * table cells. Do not forget the separating commas, though.
 * 
 * Alternatively, JSON can be generated directly by the calling code and
 * inserted via {@link Label}s. In that case, disable model escaping of
 * the labels to preserve greater-than, less-than, and ampersand characters.
 * 
 * @param <CD> the column descriptor type
 */
public abstract class AbstractJsonRenderingEntityDataTablePanel<CD extends RenderingColumnDescriptor> extends AbstractEntityDataTablePanel<CD> {
	
	/**
	 * the entityInstances
	 */
	private transient List<EntityInstance> entityInstances;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 */
	public AbstractJsonRenderingEntityDataTablePanel(String id, IModel<EntityDescriptor> entityModel) {
		super(id, entityModel);
	}

	/**
	 * Getter method for the entityInstances.
	 * @return the entityInstances
	 */
	public List<EntityInstance> getEntityInstances() {
		return entityInstances;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onDetach()
	 */
	@Override
	protected void onDetach() {
		entityInstances = null;
		super.onDetach();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		WebMarkupContainer contentRenderArea = new WebMarkupContainer("contentRenderArea");
		contentRenderArea.setVisible(false);
		add(contentRenderArea);
		
		ListView<EntityInstance> entityInstancesListView = new ListView<EntityInstance>("rows", new PropertyModel<List<EntityInstance>>(this, "entityInstances")) {
			
			/* (non-Javadoc)
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<EntityInstance> item) {
				AbstractJsonRenderingEntityDataTablePanel.this.populateRowItem(item);
			}
			
			/* (non-Javadoc)
			 * @see org.apache.wicket.markup.html.list.ListView#renderItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void renderItem(ListItem<?> item) {
				if (item.getIndex() > 0) {
					getResponse().write(",");
				}
				super.renderItem(item);
			}
			
		};
		contentRenderArea.add(entityInstancesListView);
	}
	
	/**
	 * Subclasses must implement this method to add components to data rows.
	 * @param item the row item
	 */
	protected abstract void populateRowItem(ListItem<EntityInstance> item);

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void setEntityInstances(final Iterator<? extends EntityInstance> iterator) {
		entityInstances = IteratorUtils.toList(iterator);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel#assembleRows(java.util.Iterator, name.martingeisse.common.javascript.JavascriptAssembler)
	 */
	@Override
	protected void assembleRows(final Iterator<? extends EntityInstance> iterator, JavascriptAssembler assembler) {
		
		// replace the current response by a fake response to capture the rendering output
		Response previousResponse = RequestCycle.get().getResponse();
		StringResponse fakeResponse = new StringResponse();
		RequestCycle.get().setResponse(fakeResponse);
		
		// store the entity instances so the list view can get them
		setEntityInstances(iterator);
		
		// render the content rendering area
		WebMarkupContainer contentRenderArea = (WebMarkupContainer)get("contentRenderArea");
		contentRenderArea.setVisible(true);
		contentRenderArea.render();
		contentRenderArea.setVisible(false);
		
		// restore the original response
		RequestCycle.get().setResponse(previousResponse);
		
		// JSON-encode the rendered output
		assembler.getBuilder().append(fakeResponse.toString());
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel#assembleRowFields(name.martingeisse.admin.entity.instance.EntityInstance, name.martingeisse.common.javascript.JavascriptAssembler)
	 */
	@Override
	protected void assembleRowFields(EntityInstance entityInstance, JavascriptAssembler assembler) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.datatable.AbstractEntityDataTablePanel#isColumnSortable(int)
	 */
	@Override
	protected boolean isColumnSortable(int columnIndex) {
		return (getColumnSortExpression(columnIndex) != null);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.datatable.AbstractEntityDataTablePanel#getColumnSortExpression(int)
	 */
	@Override
	protected Expression<Comparable<?>> getColumnSortExpression(int columnIndex) {
		return getColumnDescriptor(columnIndex).getSortExpression();
	}
	
}
