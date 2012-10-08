/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable.raw;

import name.martingeisse.admin.entity.EntityDescriptorModel;
import name.martingeisse.admin.entity.EntitySelection;
import name.martingeisse.admin.entity.component.list.datatable.AbstractEntityDataTablePanel;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.util.IGetPageable;
import name.martingeisse.admin.util.LinkUtil;
import name.martingeisse.common.javascript.JavascriptAssembler;
import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.wicket.util.WicketConverterUtil;

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
	 * the MAX_TEXT_LENGTH
	 */
	private static final int MAX_TEXT_LENGTH = 50;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityName the entity name
	 */
	public RawEntityListPanel(final String id, String entityName) {
		this(id, new EntityDescriptorModel(entityName));
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entity the entity
	 */
	public RawEntityListPanel(final String id, EntityDescriptor entity) {
		this(id, new EntityDescriptorModel(entity));
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 */
	public RawEntityListPanel(final String id, final IModel<EntityDescriptor> entityModel) {
		super(id, entityModel);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param selection the entity selection
	 */
	public RawEntityListPanel(final String id, final EntitySelection selection) {
		super(id, selection);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.datatable.AbstractEntityDataTablePanel#getColumnTitles()
	 */
	@Override
	protected RawDataTableColumnDescriptor[] determineColumnDescriptors() {
		String[] columnNames = getEntityDescriptor().getProperties().getRawEntityListPropertyOrder();
		RawDataTableColumnDescriptor[] result = new RawDataTableColumnDescriptor[columnNames.length];
		for (int i=0; i<columnNames.length; i++) {
			result[i] = new RawDataTableColumnDescriptor(columnNames[i], columnNames[i]);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.datatable.AbstractEntityDataTablePanel#isColumnSortable(int)
	 */
	@Override
	protected boolean isColumnSortable(int columnIndex) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel#getColumnSortExpression(int)
	 */
	@Override
	protected Expression<Comparable<?>> getColumnSortExpression(final int columnIndex) {
		final Path<?> entityPath = Expressions.path(Object.class, EntityDescriptor.ALIAS);
		return GenericTypeUtil.unsafeCast(Expressions.path(Comparable.class, entityPath, getColumnDescriptor(columnIndex).getFieldName()));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityDataTablePanel#assembleRowFields(name.martingeisse.admin.entity.instance.EntityInstance, name.martingeisse.common.javascript.JavascriptAssembler)
	 */
	@Override
	protected void assembleRowFields(final EntityInstance entityInstance, final JavascriptAssembler assembler) {
		for (int i = 0; i < getColumnCount(); i++) {
			Object value = entityInstance.getFieldValue(getColumnDescriptor(i).getFieldName());
			String valueText = WicketConverterUtil.convertValueToString(value, this, MAX_TEXT_LENGTH);
			assembler.prepareListElement();
			assembler.appendStringLiteral(valueText);
		}
		assembler.prepareListElement();
		assembler.appendStringLiteral(entityInstance.getId() == null ? null : LinkUtil.getSingleEntityLinkUrl(getEntityDescriptor(), entityInstance.getId()));
	}

}
