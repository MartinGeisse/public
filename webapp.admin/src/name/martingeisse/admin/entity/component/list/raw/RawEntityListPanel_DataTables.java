/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.raw;

import java.util.Iterator;

import name.martingeisse.admin.entity.component.list.EntityInstanceDataProvider;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.list.EntityListFilter;
import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.list.IEntityListFilterAcceptor;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;
import name.martingeisse.admin.readonly.IPropertyReadOnlyRenderer;
import name.martingeisse.admin.readonly.ReadOnlyRenderingConfigurationUtil;
import name.martingeisse.admin.util.IGetPageable;
import name.martingeisse.admin.util.LinkUtil;
import name.martingeisse.common.javascript.JavascriptAssembler;
import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.common.util.PrimitiveUtil;
import name.martingeisse.wicket.util.ISimpleCallbackListener;
import name.martingeisse.wicket.util.StringValueUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;

import com.mysema.query.support.Expressions;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

/**
 * Raw presentation of entities, using JQuery DataTables. This class does not
 * implement {@link IGetPageable} intentionally since pagination is done in
 * Javascript.
 */
public class RawEntityListPanel_DataTables extends Panel implements IEntityListFilterAcceptor, ISimpleCallbackListener {

	/**
	 * the filter
	 */
	private IEntityListFilter filter;
	
	/**
	 * the renderers
	 */
	private transient IPropertyReadOnlyRenderer[] renderers;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 */
	public RawEntityListPanel_DataTables(final String id, final IModel<EntityDescriptor> entityModel) {
		super(id);
		setDefaultModel(entityModel);
	}

	/**
	 * Getter method for the width.
	 * @return the width
	 */
	public int getWidth() {
		return getEntity().getRawEntityListFieldOrder().length;
	}
	
	/**
	 * Getter method for the entityDescriptorModel.
	 * @return the entityDescriptorModel
	 */
	@SuppressWarnings("unchecked")
	public IModel<EntityDescriptor> getEntityDescriptorModel() {
		return (IModel<EntityDescriptor>)getDefaultModel();
	}
	
	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return (EntityDescriptor)getDefaultModelObject();
	}
	
	/**
	 * Returns the name of the field that is at the specified index in the
	 * globally configured raw entity field order. 
	 * @param index the index
	 * @return the field name
	 */
	String getFieldNameForGloballyDefinedOrder(int index) {
		return getEntity().getRawEntityListFieldOrder()[index];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.list.IEntityListFilterAcceptor#acceptEntityListFilter(name.martingeisse.admin.entity.list.IEntityListFilter)
	 */
	@Override
	public void acceptEntityListFilter(IEntityListFilter filter) {
		this.filter = filter;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		setOutputMarkupId(true);
		
		// create components
		add(new Loop("headers", new PropertyModel<Integer>(RawEntityListPanel_DataTables.this, "width")) {
			@Override
			protected void populateItem(LoopItem item) {
				item.add(new Label("name", getFieldNameForGloballyDefinedOrder(item.getIndex())));
			}
		});
		add(new Label("configuration", urlFor(ISimpleCallbackListener.INTERFACE, null).toString()));

	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		
		// determine the column renderers
		EntityDescriptor entity = getEntity();
		final String[] fieldOrder = entity.getRawEntityListFieldOrder();
		renderers = new IPropertyReadOnlyRenderer[fieldOrder.length];
		for (int i=0; i<fieldOrder.length; i++) {
			EntityPropertyDescriptor property = entity.getProperties().get(fieldOrder[i]);
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
	 * @see name.martingeisse.wicket.util.ISimpleCallbackListener#onSimpleCallback()
	 */
	@Override
	public void onSimpleCallback() {
		
		// preparation
		EntityDescriptor entity = getEntity();
		int width = getWidth();
		
		// determine generic parameters
		WebRequest webRequest = (WebRequest)(RequestCycle.get().getRequest());
		IRequestParameters parameters = webRequest.getQueryParameters();
		int offset = PrimitiveUtil.fallback(StringValueUtil.getOptionalLowerCappedInteger(parameters.getParameterValue("iDisplayStart"), 0), 0);
		int count = PrimitiveUtil.fallback(StringValueUtil.getOptionalLowerCappedInteger(parameters.getParameterValue("iDisplayLength"), 1), 10);
		// column-wise searching: bSearchable_*, sSearch_*
		int echoToken = parameters.getParameterValue("sEcho").toInt();
		
		// determine sort parameters TODO order still invalid, fix column order first
		int sortColumnCount = PrimitiveUtil.fallback(StringValueUtil.getOptionalLowerCappedInteger(parameters.getParameterValue("iSortingCols"), 0), 0);
		OrderSpecifier<Comparable<?>>[] orderSpecifiers = GenericTypeUtil.unsafeCast(new OrderSpecifier<?>[sortColumnCount]);
		int orderSpecifierCount = 0;
		Path<?> entityPath = Expressions.path(Object.class, IEntityListFilter.ALIAS);
		for (int i=0; i<sortColumnCount; i++) {
			int sortColumnIndex = parameters.getParameterValue("iSortCol_" + i).toInt();
			if (sortColumnIndex >= width) {
				continue;
			}
			Order sortColumnOrder = (parameters.getParameterValue("sSortDir_" + i).toString().equalsIgnoreCase("desc") ? Order.DESC : Order.ASC);
			Path<Comparable<?>> path = GenericTypeUtil.unsafeCast(Expressions.path(Comparable.class, entityPath, getFieldNameForGloballyDefinedOrder(i)));
			orderSpecifiers[orderSpecifierCount] = new OrderSpecifier<Comparable<?>>(sortColumnOrder, path);
			orderSpecifierCount++;
		}
		if (orderSpecifierCount < orderSpecifiers.length) {
			orderSpecifiers = GenericTypeUtil.unsafeCast((Comparable<?>[])ArrayUtils.subarray(orderSpecifiers, 0, orderSpecifierCount));
		}
		
		// determine search parameters TODO
		// String searchTerm = StringUtils.trimToNull(parameters.getParameterValue("sSearch").toString());
		/*
			if (!empty($searchTerm)) {
				$conditions['name LIKE'] = ('%'.str_replace('%', '', $searchTerm).'%');
			}
		 */
		
		// determine the filter predicate manually specified by the user (TODO: not yet implemented)
		Predicate manualFilterPredicate = null;
		
		// determine the filter that will actually be used as (implied AND manual)
		IEntityListFilter actualFilter;
		if (manualFilterPredicate == null) {
			actualFilter = filter;
		} else {
			actualFilter = new EntityListFilter(Expressions.predicate(Ops.AND, filter.getFilterPredicate(), manualFilterPredicate));
		}
		
		// fetch data
		// TODO: column header/data order mismatch
		EntityInstanceDataProvider dataProvider = new EntityInstanceDataProvider(getEntityDescriptorModel(), actualFilter, orderSpecifiers);
		int sizeWithBothFilters = dataProvider.size();
		System.out.println("* " + offset + " / " + count);
		Iterator<? extends EntityInstance> iterator = dataProvider.iterator(offset, count);
		
		// determine the number of entries with only the implied filter (DataTables wants to show this).
		int sizeWithImpliedFilter;
		if (manualFilterPredicate == null) {
			sizeWithImpliedFilter = sizeWithBothFilters;
		} else {
			sizeWithImpliedFilter = new EntityInstanceDataProvider(getEntityDescriptorModel(), filter, null).size();
		}
		
		// generate the output
		JavascriptAssembler assembler = new JavascriptAssembler();
		assembler.beginObject();
		assembler.prepareObjectProperty("sEcho");
		assembler.appendNumericLiteral(echoToken);
		assembler.prepareObjectProperty("iTotalRecords");
		assembler.appendNumericLiteral(sizeWithImpliedFilter);
		assembler.prepareObjectProperty("iTotalDisplayRecords");
		assembler.appendNumericLiteral(sizeWithBothFilters);
		assembler.prepareObjectProperty("aaData");
		assembler.beginList();
		while (iterator.hasNext()) {
			EntityInstance entityInstance = iterator.next();
			assembler.prepareListElement();
			assembler.beginList();
			for (int i=0; i<width; i++) {
				assembler.prepareListElement();
				assembler.appendStringLiteral(renderers[i].valueToString(entityInstance.getData()[i]));
			}
			assembler.prepareListElement();
			assembler.appendStringLiteral(entityInstance.getId() == null ? null : LinkUtil.getSingleEntityLinkUrl(entity, entityInstance.getId()));
			assembler.endList();
		}
		assembler.endList();
		assembler.endObject();
		
		// send the output to the browser
		Response response = RequestCycle.get().getResponse();
		response.write(assembler.getAssembledCode());
		
	}

}
