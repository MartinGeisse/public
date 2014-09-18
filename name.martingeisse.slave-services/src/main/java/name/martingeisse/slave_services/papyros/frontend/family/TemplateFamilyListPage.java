/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.slave_services.papyros.frontend.family;

import java.util.List;

import name.martingeisse.slave_services.common.frontend.AbstractFrontendPage;
import name.martingeisse.slave_services.entity.QTemplateFamily;
import name.martingeisse.slave_services.entity.TemplateFamily;
import name.martingeisse.sql.EntityConnectionManager;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.mysema.query.sql.SQLQuery;

/**
 * This page displays a list of all template families.
 */
public final class TemplateFamilyListPage extends AbstractFrontendPage {

	/**
	 * Constructor.
	 */
	public TemplateFamilyListPage() {
		IModel<List<TemplateFamily>> model = new AbstractReadOnlyModel<List<TemplateFamily>>() {
			@Override
			public List<TemplateFamily> getObject() {
				final QTemplateFamily qtf = QTemplateFamily.templateFamily;
				final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
				return query.from(qtf).list(qtf);
			}
		};
		add(new ListView<TemplateFamily>("families", model) {
			@Override
			protected void populateItem(ListItem<TemplateFamily> item) {
				TemplateFamily templateFamily = item.getModelObject();
				String templateFamilyKey = templateFamily.getKey();
				Link<Void> link = new BookmarkablePageLink<>("link", TemplateFamilyPage.class, new PageParameters().add("key", templateFamilyKey));
				link.add(new Label("name", templateFamily.getName()));
				link.add(new Label("key", templateFamilyKey));
				item.add(link);
			}
		});
		add(new BookmarkablePageLink<>("createLink1", CreateTemplateFamilyPage.class));
		add(new BookmarkablePageLink<>("createLink2", CreateTemplateFamilyPage.class));
	}
	
}
