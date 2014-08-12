/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.family;

import java.util.List;
import name.martingeisse.papyros.backend.PapyrosDataUtil;
import name.martingeisse.papyros.entity.QTemplate;
import name.martingeisse.papyros.entity.TemplateFamily;
import name.martingeisse.papyros.frontend.AbstractFrontendPage;
import name.martingeisse.papyros.frontend.template.TemplatePage;
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
 * This page displays a template family.
 */
public final class TemplateFamilyPage extends AbstractFrontendPage {

	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public TemplateFamilyPage(PageParameters pageParameters) {
		
		final TemplateFamily family = PapyrosDataUtil.loadTemplateFamily(pageParameters);
		add(new Label("templateFamilyKey", family.getKey()));
		
		IModel<List<String>> templateListModel = new AbstractReadOnlyModel<List<String>>() {
			@Override
			public List<String> getObject() {
				final QTemplate qt = QTemplate.template;
				final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
				return query.from(qt).where(qt.templateFamilyId.eq(family.getId())).list(qt.languageKey);
			}
		};
		add(new ListView<String>("templates", templateListModel) {
			@Override
			protected void populateItem(ListItem<String> item) {
				String languageKey = item.getModelObject();
				Link<Void> link = new BookmarkablePageLink<>("link", TemplatePage.class, new PageParameters().add("key", family.getKey()).add("language", languageKey));
				link.add(new Label("text", languageKey));
				item.add(link);
			}
		});
	
		add(new BookmarkablePageLink<>("templateFamilyListLink", TemplateFamilyListPage.class));
		add(new BookmarkablePageLink<>("changeKeyLink", ChangeTemplateFamilyKeyPage.class, new PageParameters().add("key", family.getKey())));
	}

}
