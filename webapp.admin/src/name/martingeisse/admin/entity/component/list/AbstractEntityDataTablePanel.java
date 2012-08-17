/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list;

import java.util.Iterator;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.list.EntityListFilter;
import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.list.IEntityListFilterAcceptor;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.util.IGetPageable;
import name.martingeisse.common.javascript.JavascriptAssembler;
import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.common.util.PrimitiveUtil;
import name.martingeisse.wicket.util.ISimpleCallbackListener;
import name.martingeisse.wicket.util.StringValueUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;

import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;

/**
 * Base class for entity lists that are implemented with jQuery DataTables.
 * This class does not implement {@link IGetPageable} intentionally since
 * pagination is done in Javascript.
 */
public abstract class AbstractEntityDataTablePanel extends Panel implements IEntityListFilterAcceptor, ISimpleCallbackListener {

	/**
	 * the filter
	 */
	private IEntityListFilter filter;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 */
	public AbstractEntityDataTablePanel(final String id, final IModel<EntityDescriptor> entityModel) {
		super(id);
		setDefaultModel(entityModel);
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
	public EntityDescriptor getEntityDescriptor() {
		return (EntityDescriptor)getDefaultModelObject();
	}

	/**
	 * @return the URL that must be used as the JSON source of the DataTable configuration
	 */
	public String getCallbackUrl() {
		return urlFor(ISimpleCallbackListener.INTERFACE, null).toString();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.list.IEntityListFilterAcceptor#acceptEntityListFilter(name.martingeisse.admin.entity.list.IEntityListFilter)
	 */
	@Override
	public void acceptEntityListFilter(final IEntityListFilter filter) {
		this.filter = filter;
	}

	/**
	 * @return the number of columns of the DataTable
	 */
	protected abstract int getColumnCount();

	/**
	 * Returns a comparable column expression for the specified column index, used
	 * to sort the table. May return null to ignore a column when sorting.
	 * @param columnIndex the column index
	 * @return the expression to sort that column, or null to skip sorting
	 */
	protected abstract Expression<Comparable<?>> getColumnSortExpression(int columnIndex);

	/**
	 * Returns a predicate to filter rows for the specified search term.
	 * May return null to indicate no filtering.
	 * @param searchTerm the search term
	 * @return the search predicate
	 */
	protected abstract Predicate getSearchPredicate(String searchTerm);

	/**
	 * This method should use the specified assembler to generate data for
	 * a single row. Specifically, this method should assume that a JSON array
	 * is currently being assembled, and should generate as many fields as
	 * this table has columns, each using {@link JavascriptAssembler#prepareListElement()}
	 * and then one of the value-assembling methods.
	 * 
	 * This method may produce more data elements than this table has columns.
	 * This is useful to provide additional data to Javascript code, such as
	 * a link URL to make each row a clickable link. Consumption of such
	 * additional data is completely up to the scripts provided by subclasses.
	 * 
	 * @param entityInstance the entity instance to assembler data for
	 * @param assembler the assembler to use
	 */
	protected abstract void assembleRowFields(EntityInstance entityInstance, JavascriptAssembler assembler);

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.util.ISimpleCallbackListener#onSimpleCallback()
	 */
	@Override
	public void onSimpleCallback() {

		// determine generic parameters
		final WebRequest webRequest = (WebRequest)(RequestCycle.get().getRequest());
		final IRequestParameters parameters = webRequest.getQueryParameters();
		final int offset = PrimitiveUtil.fallback(StringValueUtil.getOptionalLowerCappedInteger(parameters.getParameterValue("iDisplayStart"), 0), 0);
		final int count = PrimitiveUtil.fallback(StringValueUtil.getOptionalLowerCappedInteger(parameters.getParameterValue("iDisplayLength"), 1), 10);
		// column-wise searching: bSearchable_*, sSearch_*
		final int echoToken = parameters.getParameterValue("sEcho").toInt();

		// determine sort parameters
		final int sortColumnCount = PrimitiveUtil.fallback(StringValueUtil.getOptionalLowerCappedInteger(parameters.getParameterValue("iSortingCols"), 0), 0);
		OrderSpecifier<Comparable<?>>[] orderSpecifiers = GenericTypeUtil.unsafeCast(new OrderSpecifier<?>[sortColumnCount]);
		int orderSpecifierCount = 0;
		final int columnCount = getColumnCount();
		for (int i = 0; i < sortColumnCount; i++) {
			final int sortColumnIndex = parameters.getParameterValue("iSortCol_" + i).toInt();
			if (sortColumnIndex >= columnCount) {
				continue;
			}
			final Expression<Comparable<?>> sortColumnExpression = getColumnSortExpression(sortColumnIndex);
			if (sortColumnExpression == null) {
				continue;
			}
			final Order sortColumnOrder = (parameters.getParameterValue("sSortDir_" + i).toString().equalsIgnoreCase("desc") ? Order.DESC : Order.ASC);
			orderSpecifiers[orderSpecifierCount] = new OrderSpecifier<Comparable<?>>(sortColumnOrder, sortColumnExpression);
			orderSpecifierCount++;
		}
		if (orderSpecifierCount < orderSpecifiers.length) {
			orderSpecifiers = GenericTypeUtil.unsafeCast((Comparable<?>[])ArrayUtils.subarray(orderSpecifiers, 0, orderSpecifierCount));
		}

		// determine search parameters
		final String searchTerm = StringUtils.trimToNull(parameters.getParameterValue("sSearch").toString());
		final Predicate searchPredicate = (searchTerm == null ? null : getSearchPredicate(searchTerm));

		// determine the filter that will actually be used as (implied AND manual)
		IEntityListFilter actualFilter;
		if (searchPredicate == null) {
			actualFilter = filter;
		} else if (filter == null) {
			actualFilter = new EntityListFilter(searchPredicate);
		} else {
			actualFilter = new EntityListFilter(Expressions.predicate(Ops.AND, filter.getFilterPredicate(), searchPredicate));
		}

		// fetch data
		final EntityInstanceDataProvider dataProvider = new EntityInstanceDataProvider(getEntityDescriptorModel(), actualFilter, orderSpecifiers);
		final int sizeWithBothFilters = dataProvider.size();
		final Iterator<? extends EntityInstance> iterator = dataProvider.iterator(offset, count);

		// determine the number of entries with only the implied filter (DataTables wants to show this).
		int sizeWithImpliedFilter;
		if (searchPredicate == null) {
			sizeWithImpliedFilter = sizeWithBothFilters;
		} else {
			sizeWithImpliedFilter = new EntityInstanceDataProvider(getEntityDescriptorModel(), filter, null).size();
		}

		// generate the output
		final JavascriptAssembler assembler = new JavascriptAssembler();
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
			final EntityInstance entityInstance = iterator.next();
			assembler.prepareListElement();
			assembler.beginList();
			assembleRowFields(entityInstance, assembler);
			assembler.endList();
		}
		assembler.endList();
		assembler.endObject();

		// send the output to the browser
		final Response response = RequestCycle.get().getResponse();
		response.write(assembler.getAssembledCode());

	}

}
