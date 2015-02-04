/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.slave_services.babel.frontend;

import java.util.List;

import name.martingeisse.slave_services.common.frontend.AbstractFrontendPage;
import name.martingeisse.slave_services.common.frontend.HomePage;
import name.martingeisse.slave_services.entity.MessageFamily;
import name.martingeisse.slave_services.entity.QMessageFamily;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.wicket.component.misc.PageParameterDrivenTabPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.mysema.query.sql.SQLQuery;

/**
 * This page displays a list of all template families.
 */
public final class OverviewPage extends AbstractFrontendPage {

	/**
	 * Constructor.
	 */
	public OverviewPage() {
		final PageParameterDrivenTabPanel tabPanel = new PageParameterDrivenTabPanel("tabs", "tab") {
			@Override
			protected Component createBody(String id, String selector) {
				if (selector.equals(".messageFamilies")) {
					return createMessageFamiliesTabBody(id);
				} else if (selector.equals(".languages")) {
					return createLanguagesTabBody(id);
				} else {
					return new EmptyPanel(id);
				}
			}
		};
		tabPanel.addTab("Messages", ".messageFamilies");
		tabPanel.addTab("Languages", ".languages");
		add(tabPanel);
	}
	
	/**
	 * 
	 */
	private Component createMessageFamiliesTabBody(String id) {
		final Fragment fragment = new Fragment(id, "tabMessageFamilies", this);
		IModel<List<MessageFamily>> model = new AbstractReadOnlyModel<List<MessageFamily>>() {
			@Override
			public List<MessageFamily> getObject() {
				final QMessageFamily qmf = QMessageFamily.messageFamily;
				final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
				return query.from(qmf).list(qmf);
			}
		};
		fragment.add(new ListView<MessageFamily>("families", model) {
			@Override
			protected void populateItem(ListItem<MessageFamily> item) {
				MessageFamily messageFamily = item.getModelObject();
				String domain = messageFamily.getDomain();
				String messageKey = messageFamily.getMessageKey();
				item.add(new Label("domain", domain));
				Link<Void> link = new BookmarkablePageLink<>("link", MessageFamilyPage.class, new PageParameters().add("domain", domain).add("messageKey", messageKey));
				link.add(new Label("messageKey", messageKey));
				link.add(new Label("developerText", messageFamily.getDeveloperText()));
				item.add(link);
			}
		});
		fragment.add(new BookmarkablePageLink<>("createLink1", HomePage.class)); // TODO
		fragment.add(new BookmarkablePageLink<>("createLink2", HomePage.class)); // TODO
		return fragment;
	}
	
	/**
	 * 
	 */
	private Component createLanguagesTabBody(String id) {
		final Fragment fragment = new Fragment(id, "tabLanguages", this);
		return fragment;
	}
	
}
