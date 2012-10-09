/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import name.martingeisse.admin.entity.instance.EntityInstanceFieldModel;
import name.martingeisse.admin.entity.instance.IEntityInstance;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Simple overview panel that shows the ID, active flag and modification info.
 */
public class OverviewPanel extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the entity instance model
	 */
	public OverviewPanel(final String id, final IModel<IEntityInstance> model) {
		super(id, model);
		add(new Label("id", new EntityInstanceFieldModel<Integer>(model, "id")));
		add(new Label("active", new EntityInstanceFieldModel<Boolean>(model, "active")));
//		add(new Label("modificationTimestamp", new EntityInstanceFieldModel<T>(model, "")));
//		add(new Label("modificationUserId", new EntityInstanceFieldModel<T>(model, "")));
		add(new Label("modificationTimestamp", ""));
		add(new Label("modificationUserId", ""));
	}

}
