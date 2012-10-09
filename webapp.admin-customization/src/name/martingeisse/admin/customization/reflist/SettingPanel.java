/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.reflist;

import name.martingeisse.admin.entity.component.instance.AbstractEntityInstancePanel;
import name.martingeisse.admin.entity.component.list.datatable.raw.RawEntityListPanel;
import name.martingeisse.admin.entity.instance.RawEntityInstance;
import name.martingeisse.admin.entity.instance.EntityInstanceFieldModel;
import name.martingeisse.admin.entity.list.EntityConditions;
import name.martingeisse.admin.util.LinkUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * This panel is specialized on displaying a "setting" and its
 * notes.
 */
public class SettingPanel extends AbstractEntityInstancePanel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param instanceModel the model
	 */
	public SettingPanel(final String id, final IModel<RawEntityInstance> instanceModel) {
		super(id, instanceModel);
		add(new Label("name", new EntityInstanceFieldModel<String>(instanceModel, "name")));
		add(new Label("type", new EntityInstanceFieldModel<String>(instanceModel, "type")));
		add(new Label("data", new EntityInstanceFieldModel<String>(instanceModel, "data")));

		add(LinkUtil.createSingleEntityLink("editLink", "settings", null, "edit"));

		IModel<RawEntityInstance> groupModel = createModelForRelatedSingleEntityInstance("group_id");
		add(new Label("group", new EntityInstanceFieldModel<String>(groupModel, "name")));

		final EntityConditions notesPredicate = new EntityConditions();
		notesPredicate.addFieldEquals("setting_name", instanceModel.getObject().getDataRowFieldValue("name"));
		final RawEntityListPanel notesPanel = new RawEntityListPanel("notes", "settings_notes");
		notesPanel.acceptEntityListFilter(notesPredicate);
		add(notesPanel);
	}

}
