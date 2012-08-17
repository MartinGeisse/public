/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.populator;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.instance.EntityInstanceFieldModel;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * This populator presents a single entity field. 
 */
public class EntityFieldPopulator extends AbstractEntityCellPopulator {

	/**
	 * the fieldName
	 */
	private String fieldName;

	/**
	 * Constructor.
	 * @param title the title
	 * @param fieldName the field name
	 */
	public EntityFieldPopulator(final String title, final String fieldName) {
		super(title);
		this.fieldName = fieldName;
	}

	/**
	 * Getter method for the fieldName.
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Setter method for the fieldName.
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item, java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(final Item<ICellPopulator<EntityInstance>> item, final String id, final IModel<EntityInstance> instanceModel) {
		final IModel<?> fieldModel = new EntityInstanceFieldModel<Object>(instanceModel, fieldName);
		item.add(new Label(id, fieldModel));
	}

}
