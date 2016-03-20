/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.papyros.frontend.family;

import java.util.List;

import name.martingeisse.slave_services.common.frontend.AbstractFrontendPage;
import name.martingeisse.slave_services.entity.QTemplate;
import name.martingeisse.slave_services.entity.QTemplatePreviewDataSet;
import name.martingeisse.slave_services.entity.TemplateFamily;
import name.martingeisse.slave_services.entity.TemplatePreviewDataSet;
import name.martingeisse.slave_services.papyros.backend.PapyrosDataUtil;
import name.martingeisse.slave_services.papyros.frontend.components.NerdsOnlyLinkPanel;
import name.martingeisse.slave_services.papyros.frontend.previewdata.CreatePreviewDataPage;
import name.martingeisse.slave_services.papyros.frontend.previewdata.PreviewDataPage;
import name.martingeisse.slave_services.papyros.frontend.schema.SchemaPage;
import name.martingeisse.slave_services.papyros.frontend.template.CreateTemplatePage;
import name.martingeisse.slave_services.papyros.frontend.template.TemplatePage;
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
 * This page displays a template family.
 */
public final class TemplateFamilyPage extends AbstractFrontendPage {

	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public TemplateFamilyPage(PageParameters pageParameters) {
		
		final TemplateFamily family = PapyrosDataUtil.loadTemplateFamily(pageParameters);
		add(new Label("templateFamilyName", family.getName()));
		add(new Label("templateFamilyKey", family.getKey()));
		add(new BookmarkablePageLink<>("templateFamilyListLink", TemplateFamilyListPage.class));
		add(new BookmarkablePageLink<>("editLink", EditTemplateFamilyPage.class, new PageParameters().add("key", family.getKey())));
		
		final PageParameterDrivenTabPanel tabPanel = new PageParameterDrivenTabPanel("tabs", "tab") {
			@Override
			protected Component createBody(String id, String selector) {
				if (selector.equals(".templates")) {

					final Fragment fragment = new Fragment(id, "tabTemplates", TemplateFamilyPage.this);
					fragment.add(new BookmarkablePageLink<>("createLink1", CreateTemplatePage.class, new PageParameters().add("key", family.getKey())));
					fragment.add(new BookmarkablePageLink<>("createLink2", CreateTemplatePage.class, new PageParameters().add("key", family.getKey())));

					IModel<List<String>> templateListModel = new AbstractReadOnlyModel<List<String>>() {
						@Override
						public List<String> getObject() {
							final QTemplate qt = QTemplate.template;
							final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
							return query.from(qt).where(qt.templateFamilyId.eq(family.getId())).list(qt.languageKey);
						}
					};
					fragment.add(new ListView<String>("templates", templateListModel) {
						@Override
						protected void populateItem(ListItem<String> item) {
							String languageKey = item.getModelObject();
							Link<Void> link = new BookmarkablePageLink<>("link", TemplatePage.class, new PageParameters().add("key", family.getKey()).add("language", languageKey));
							link.add(new Label("text", languageKey));
							item.add(link);
						}
					});

					return fragment;

				} else if (selector.equals(".preview-data")) {
					
					final Fragment fragment = new Fragment(id, "tabPreviewData", TemplateFamilyPage.this);
					// TODO link to correct page
					fragment.add(new BookmarkablePageLink<>("createLink1", CreatePreviewDataPage.class, new PageParameters().add("key", family.getKey())));
					fragment.add(new BookmarkablePageLink<>("createLink2", CreatePreviewDataPage.class, new PageParameters().add("key", family.getKey())));

					// TODO list preview data sets, not templates
					IModel<List<TemplatePreviewDataSet>> previewDataListModel = new AbstractReadOnlyModel<List<TemplatePreviewDataSet>>() {
						@Override
						public List<TemplatePreviewDataSet> getObject() {
							final QTemplatePreviewDataSet qpds = QTemplatePreviewDataSet.templatePreviewDataSet;
							final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
							return query.from(qpds).where(qpds.templateFamilyId.eq(family.getId())).orderBy(qpds.orderIndex.asc()).list(qpds);
						}
					};
					fragment.add(new ListView<TemplatePreviewDataSet>("previewDataSets", previewDataListModel) {
						@Override
						protected void populateItem(ListItem<TemplatePreviewDataSet> item) {
							TemplatePreviewDataSet previewDataSet = item.getModelObject();
							String previewDataKey = previewDataSet.getPreviewDataKey();
							Link<Void> link = new BookmarkablePageLink<>("link", PreviewDataPage.class, new PageParameters().add("key", family.getKey()).add("previewDataKey", previewDataKey));
							link.add(new Label("name", previewDataSet.getName()));
							link.add(new Label("key", previewDataKey));
							item.add(link);
						}
					});

					return fragment;
					
				} else if (selector.equals(".schema")) {
					Fragment fragment = new Fragment(id, "tabSchema", TemplateFamilyPage.this);
					fragment.add(new NerdsOnlyLinkPanel("linkPanel", SchemaPage.class, new PageParameters().add("key", family.getKey())));
					return fragment;
				} else if (selector.equals(".configuration")) {
					return new Fragment(id, "tabConfiguration", TemplateFamilyPage.this);
				} else {
					return new EmptyPanel(id);
				}
			}
		};
		tabPanel.addTab("Templates", ".templates");
		tabPanel.addTab("Preview Data", ".preview-data");
		tabPanel.addTab("Schema", ".schema");
		tabPanel.addTab("Configuration", ".configuration");
		add(tabPanel);
		
	}

}
