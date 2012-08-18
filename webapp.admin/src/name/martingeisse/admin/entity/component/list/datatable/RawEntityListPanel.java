/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable;

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

import org.apache.wicket.model.IModel;

import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;

/**
 * Raw presentation of entities, using JQuery DataTables. This class does not
 * implement {@link IGetPageable} intentionally since pagination is done in
 * Javascript.
 */
public class RawEntityListPanel extends AbstractEntityDataTablePanel<RawDataTableColumnDescriptor> {

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
	 * @see name.martingeisse.admin.entity.component.list.datatable.AbstractEntityDataTablePanel#getColumnTitles()
	 */
	@Override
	protected RawDataTableColumnDescriptor[] determineColumnDescriptors() {
		String[] columnNames = getEntityDescriptor().getRawEntityListFieldOrder();
		RawDataTableColumnDescriptor[] result = new RawDataTableColumnDescriptor[columnNames.length];
		for (int i=0; i<columnNames.length; i++) {
			result[i] = new RawDataTableColumnDescriptor(columnNames[i], columnNames[i]);
		}
		return result;
	}

	/**
	 * Returns the name of the field that is at the specified index in the
	 * globally configured raw entity field order. 
	 * @param index the index
	 * @return the field name
	 */
	String getFieldNameForGloballyDefinedOrder(final int index) {
		return getColumnDescriptor(index).getTitle();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		// determine the column renderers
		final EntityDescriptor entity = getEntityDescriptor();
		final DataTableColumnDescriptor[] fieldOrder = determineColumnDescriptors();
		renderers = new IPropertyReadOnlyRenderer[fieldOrder.length];
		for (int i = 0; i < fieldOrder.length; i++) {
			final EntityPropertyDescriptor property = entity.getPropertiesByName().get(fieldOrder[i].getTitle());
			renderers[i] = ReadOnlyRenderingConfigurationUtil.createPropertyReadOnlyRenderer(property.getType());
			if (renderers[i] == null) {
				throw new RuntimeException("no renderer");
			}
		}

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel#getColumnSortExpression(int)
	 */
	@Override
	protected Expression<Comparable<?>> getColumnSortExpression(final int columnIndex) {
		final Path<?> entityPath = Expressions.path(Object.class, IEntityListFilter.ALIAS);
		return GenericTypeUtil.unsafeCast(Expressions.path(Comparable.class, entityPath, getFieldNameForGloballyDefinedOrder(columnIndex)));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel#assembleRowFields(name.martingeisse.admin.entity.instance.EntityInstance, name.martingeisse.common.javascript.JavascriptAssembler)
	 */
	@Override
	protected void assembleRowFields(final EntityInstance entityInstance, final JavascriptAssembler assembler) {
		final DataTableColumnDescriptor[] fieldOrder = getColumnDescriptors();
		for (int i = 0; i < getColumnCount(); i++) {
			assembler.prepareListElement();
			assembler.appendStringLiteral(renderers[i].valueToString(entityInstance.getFieldValue(fieldOrder[i].getTitle())));
		}
		assembler.prepareListElement();
		assembler.appendStringLiteral(entityInstance.getId() == null ? null : LinkUtil.getSingleEntityLinkUrl(getEntityDescriptor(), entityInstance.getId()));
	}

}
