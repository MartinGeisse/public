/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.slave_services.babel.frontend;

import java.util.List;

import name.martingeisse.slave_services.babel.backend.BabelDataUtil;
import name.martingeisse.slave_services.common.frontend.AbstractFrontendPage;
import name.martingeisse.slave_services.common.frontend.HomePage;
import name.martingeisse.slave_services.entity.MessageFamily;
import name.martingeisse.slave_services.entity.MessageTranslation;
import name.martingeisse.slave_services.entity.QMessageTranslation;
import name.martingeisse.sql.EntityConnectionManager;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.mysema.query.sql.SQLQuery;

/**
 * This page displays a list of all template families.
 */
public final class MessageFamilyPage extends AbstractFrontendPage {

	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public MessageFamilyPage(PageParameters pageParameters) {
		final MessageFamily family = BabelDataUtil.loadMessageFamily(pageParameters);
		add(new BookmarkablePageLink<>("messageFamilyListLink", OverviewPage.class, new PageParameters().add("tab", ".messageFamilies")));
		add(new Label("domain", family.getDomain()));
		add(new Label("messageKey", family.getMessageKey()));
		add(new BookmarkablePageLink<>("createLink1", HomePage.class)); // TODO
		add(new BookmarkablePageLink<>("createLink2", HomePage.class)); // TODO
		
		IModel<List<MessageTranslation>> model = new AbstractReadOnlyModel<List<MessageTranslation>>() {
			@Override
			public List<MessageTranslation> getObject() {
				final QMessageTranslation qmt = QMessageTranslation.messageTranslation;
				final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
				return query.from(qmt).where(qmt.messageFamilyId.eq(family.getId())).list(qmt);
			}
		};
		add(new ListView<MessageTranslation>("translations", model) {
			@Override
			protected void populateItem(ListItem<MessageTranslation> item) {
				MessageTranslation messageTranslation = item.getModelObject();
				item.add(new Label("languageKey", messageTranslation.getLanguageKey()));
				item.add(new Label("text", messageTranslation.getText()));
			}
		});
		
	}
	
}
