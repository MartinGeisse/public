/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.multi;

import name.martingeisse.admin.entity.component.list.AbstractEntityDataViewPanel;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

/**
 * Id-only presentation of entities.
 */
public class RoleOrderListPanel extends AbstractEntityDataViewPanel {
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity model
	 */
	public RoleOrderListPanel(final String id, final IModel<EntityDescriptor> entityModel) {
		super(id, entityModel);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.AbstractEntityDataViewPanel#createHeaderRepeater(java.lang.String)
	 */
	@Override
	protected AbstractRepeater createHeaderRepeater(String id) {
		RepeatingView repeater = new RepeatingView(id);
		repeater.add(new Label(repeater.newChildId(), "one"));
		repeater.add(new Label(repeater.newChildId(), "two"));
		repeater.add(new Label(repeater.newChildId(), "three"));
		return repeater;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.AbstractEntityDataViewPanel#populateRowItem(org.apache.wicket.markup.repeater.Item)
	 */
	@Override
	protected void populateRowItem(Item<EntityInstance> rowItem) {
		rowItem.add(new Label("roleOrder", "" + rowItem.getModelObject().getFieldValue("role_order")));
	}

}
