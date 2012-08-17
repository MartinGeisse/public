/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.raw;

import name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;
import name.martingeisse.admin.readonly.IPropertyReadOnlyRenderer;
import name.martingeisse.admin.readonly.ReadOnlyRenderingConfigurationUtil;
import name.martingeisse.admin.util.IGetPageable;
import name.martingeisse.admin.util.LinkUtil;
import name.martingeisse.common.javascript.JavascriptAssembler;
import name.martingeisse.common.util.GenericTypeUtil;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

/**
 * Raw presentation of entities, using JQuery DataTables. This class does not
 * implement {@link IGetPageable} intentionally since pagination is done in
 * Javascript.
 */
public class RawEntityListPanel extends AbstractEntityDataTablePanel {
	
	/**
	 * the renderers
	 */
	private transient IPropertyReadOnlyRenderer[] renderers;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 */
	public RawEntityListPanel(final String id, final IModel<EntityDescriptor> entityModel) {
		super(id, entityModel);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return getEntityDescriptor().getRawEntityListFieldOrder().length;
	}
	
	/**
	 * Returns the name of the field that is at the specified index in the
	 * globally configured raw entity field order. 
	 * @param index the index
	 * @return the field name
	 */
	String getFieldNameForGloballyDefinedOrder(int index) {
		return getEntityDescriptor().getRawEntityListFieldOrder()[index];
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		setOutputMarkupId(true);
		
		// assemble the JSON configuration for our home-grown initialization function
		JavascriptAssembler assembler = new JavascriptAssembler();
		assembler.beginObject();
		assembler.prepareObjectProperty("url");
		assembler.appendStringLiteral(getCallbackUrl());
		assembler.prepareObjectProperty("showSearchField");
		assembler.appendBooleanLiteral(isSearchSupported());
		assembler.endObject();
		
		// create components
		add(new Loop("headers", new PropertyModel<Integer>(RawEntityListPanel.this, "columnCount")) {
			@Override
			protected void populateItem(LoopItem item) {
				item.add(new Label("name", getFieldNameForGloballyDefinedOrder(item.getIndex())));
			}
		});
		add(new Label("configuration", assembler.getAssembledCode()));

	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		
		// determine the column renderers
		EntityDescriptor entity = getEntityDescriptor();
		final String[] fieldOrder = entity.getRawEntityListFieldOrder();
		renderers = new IPropertyReadOnlyRenderer[fieldOrder.length];
		for (int i=0; i<fieldOrder.length; i++) {
			EntityPropertyDescriptor property = entity.getPropertiesByName().get(fieldOrder[i]);
			renderers[i] = ReadOnlyRenderingConfigurationUtil.createPropertyReadOnlyRenderer(property.getType());
			if (renderers[i] == null) {
				throw new RuntimeException("no renderer");
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.renderOnDomReadyJavaScript("$('#" + getMarkupId() + "').createRawEntityTable();\n");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel#getColumnSortExpression(int)
	 */
	@Override
	protected Expression<Comparable<?>> getColumnSortExpression(int columnIndex) {
		Path<?> entityPath = Expressions.path(Object.class, IEntityListFilter.ALIAS);
		return GenericTypeUtil.unsafeCast(Expressions.path(Comparable.class, entityPath, getFieldNameForGloballyDefinedOrder(columnIndex)));
	}

	/**
	 * @return true if supported, false if not
	 */
	protected boolean isSearchSupported() {
		return getEntityDescriptor().isSearchSupported();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel#getSearchPredicate(java.lang.String)
	 */
	@Override
	protected Predicate getSearchPredicate(String searchTerm) {
		return getEntityDescriptor().createSearchFilter(searchTerm).getFilterPredicate();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel#assembleRowFields(name.martingeisse.admin.entity.instance.EntityInstance, name.martingeisse.common.javascript.JavascriptAssembler)
	 */
	@Override
	protected void assembleRowFields(EntityInstance entityInstance, JavascriptAssembler assembler) {
		EntityDescriptor entity = entityInstance.getEntity();
		final String[] fieldOrder = entity.getRawEntityListFieldOrder();
		for (int i=0; i<getColumnCount(); i++) {
			assembler.prepareListElement();
			assembler.appendStringLiteral(renderers[i].valueToString(entityInstance.getFieldValue(fieldOrder[i])));
		}
		assembler.prepareListElement();
		assembler.appendStringLiteral(entityInstance.getId() == null ? null : LinkUtil.getSingleEntityLinkUrl(getEntityDescriptor(), entityInstance.getId()));
	}

}
