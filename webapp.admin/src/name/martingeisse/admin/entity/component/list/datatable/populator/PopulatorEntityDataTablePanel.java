/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable.populator;

import name.martingeisse.admin.entity.EntityDescriptorModel;
import name.martingeisse.admin.entity.EntitySelection;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.model.IModel;

/**
 * Default implementation of {@link AbstractPopulatorEntityDataTablePanel}. This implementation stores
 * the column descriptors directly in this panel.
 */
public class PopulatorEntityDataTablePanel extends AbstractPopulatorEntityDataTablePanel<PopulatorColumnDescriptor> {

	/**
	 * the columnDescriptors
	 */
	private final PopulatorColumnDescriptor[] columnDescriptors;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityName the entity name
	 * @param columnDescriptors the column descriptors
	 */
	public PopulatorEntityDataTablePanel(final String id, String entityName, PopulatorColumnDescriptor[] columnDescriptors) {
		this(id, new EntityDescriptorModel(entityName), columnDescriptors);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entity the entity
	 * @param columnDescriptors the column descriptors
	 */
	public PopulatorEntityDataTablePanel(final String id, EntityDescriptor entity, PopulatorColumnDescriptor[] columnDescriptors) {
		this(id, new EntityDescriptorModel(entity), columnDescriptors);
	}
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 * @param columnDescriptors the column descriptors
	 */
	public PopulatorEntityDataTablePanel(String id, IModel<EntityDescriptor> entityModel, PopulatorColumnDescriptor[] columnDescriptors) {
		super(id, entityModel);
		this.columnDescriptors = columnDescriptors;
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param selection the entity selection
	 * @param columnDescriptors the column descriptors
	 */
	public PopulatorEntityDataTablePanel(final String id, final EntitySelection selection, PopulatorColumnDescriptor[] columnDescriptors) {
		super(id, selection);
		this.columnDescriptors = columnDescriptors;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.datatable.AbstractEntityDataTablePanel#determineColumnDescriptors()
	 */
	@Override
	protected PopulatorColumnDescriptor[] determineColumnDescriptors() {
		return columnDescriptors;
	}

}
