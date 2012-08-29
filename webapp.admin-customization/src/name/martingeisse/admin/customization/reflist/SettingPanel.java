/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.reflist;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.instance.EntityInstanceFieldModel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * This panel is specialized on displaying a "setting" and its
 * notes.
 */
public class SettingPanel extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param instanceModel the model
	 */
	public SettingPanel(final String id, final IModel<EntityInstance> instanceModel) {
		super(id);
		add(new Label("name", new EntityInstanceFieldModel<String>(instanceModel, "name")));
		add(new Label("type", new EntityInstanceFieldModel<String>(instanceModel, "type")));
		add(new Label("data", new EntityInstanceFieldModel<String>(instanceModel, "data")));
	}
	
}
