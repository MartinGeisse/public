/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.family;

import java.util.List;

import name.martingeisse.papyros.entity.QTemplateFamily;
import name.martingeisse.papyros.frontend.AbstractFrontendPage;
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
		IModel<List<String>> model = new AbstractReadOnlyModel<List<String>>() {
			@Override
			public List<String> getObject() {
				final QTemplateFamily qtf = QTemplateFamily.templateFamily;
				final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
				return query.from(qtf).list(qtf.key);
			}
		};
		add(new ListView<String>("families", model) {
			@Override
			protected void populateItem(ListItem<String> item) {
				String templateFamilyKey = item.getModelObject();
				Link<Void> link = new BookmarkablePageLink<>("link", TemplateFamilyPage.class, new PageParameters().add("key", templateFamilyKey));
				link.add(new Label("text", templateFamilyKey));
				item.add(link);
			}
		});
	}
	
}
