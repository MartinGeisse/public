/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.reflist;

import name.martingeisse.admin.entity.EntityInstanceModel;
import name.martingeisse.admin.entity.EntitySelection;
import name.martingeisse.admin.entity.component.list.datatable.raw.RawEntityListPanel;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.instance.EntityInstanceFieldModel;
import name.martingeisse.admin.entity.list.EntityConditions;

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

		final Object groupId = instanceModel.getObject().getFieldValue("group_id");
		if (groupId == null) {
			add(new Label("group", "<NONE>"));
		} else {
			final EntitySelection groupSelection = EntitySelection.forId("settings_groups", groupId);
			final EntityInstanceModel groupModel = groupSelection.createSingleInstanceModel(true);
			add(new Label("group", new EntityInstanceFieldModel<String>(groupModel, "name")));
		}

		final EntityConditions notesPredicate = new EntityConditions();
		notesPredicate.addFieldEquals("setting_name", instanceModel.getObject().getFieldValue("name"));
		final RawEntityListPanel notesPanel = new RawEntityListPanel("notes", "settings_notes");
		notesPanel.acceptEntityListFilter(notesPredicate);
		add(notesPanel);
	}

}
