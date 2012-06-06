/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.multi;

import name.martingeisse.admin.multi.AbstractEntityDataViewPanel;
import name.martingeisse.admin.schema.EntityDescriptor;
import name.martingeisse.admin.single.EntityInstance;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Id-only presentation of entities.
 */
public class RoleOrderListPanel extends AbstractEntityDataViewPanel {
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entity the entity
	 * @param parameters the page parameters
	 */
	public RoleOrderListPanel(final String id, final EntityDescriptor entity, final PageParameters parameters) {
		super(id, entity, parameters);
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
