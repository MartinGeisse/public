/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.slave_services.papyros.frontend.template;

import name.martingeisse.slave_services.common.frontend.AbstractFrontendPage;
import name.martingeisse.slave_services.common.frontend.components.PageParameterDrivenTabPanel;
import name.martingeisse.slave_services.entity.Template;
import name.martingeisse.slave_services.entity.TemplateFamily;
import name.martingeisse.slave_services.entity.TemplatePreviewDataSet;
import name.martingeisse.slave_services.papyros.backend.PapyrosDataUtil;
import name.martingeisse.slave_services.papyros.frontend.components.PreviewTemplateIframe;
import name.martingeisse.slave_services.papyros.frontend.family.TemplateFamilyPage;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * The main page to manage a template.
 */
public class TemplatePage extends AbstractFrontendPage {

	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public TemplatePage(final PageParameters pageParameters) {
		super(pageParameters);
		final Pair<Template, TemplateFamily> pair = PapyrosDataUtil.loadTemplateAndTemplateFamily(pageParameters);
		final Template template = pair.getLeft();
		final TemplateFamily family = pair.getRight();

		add(new BookmarkablePageLink<>("templateFamilyLink", TemplateFamilyPage.class, new PageParameters().add("key", family.getKey())));
		add(new Label("templateFamilyName", family.getName()));
		add(new Label("templateFamilyKey", family.getKey()));
		add(new Label("languageKey", template.getLanguageKey()));

		final PageParameterDrivenTabPanel tabPanel = new PageParameterDrivenTabPanel("tabs", "tab") {
			@Override
			protected Component createBody(String id, String selector) {
				if (selector.equals(".preview")) {

					TemplatePreviewDataSet previewDataSet = PapyrosDataUtil.loadFirstPreviewDataSet(family.getId());
					final Fragment fragment = new Fragment(id, "tabPreview", TemplatePage.this);
					fragment.add(new PreviewTemplateIframe("iframe", new PropertyModel<String>(template, "content"), Model.of(previewDataSet)));
					return fragment;

				} else if (selector.equals(".edit")) {

					return new EditTemplateFragment(id, "tabEdit", TemplatePage.this, family, template) {
						@Override
						protected void onSubmit() {
							setResponsePage(TemplatePage.class, createTabLinkPageParameters(".preview"));
						}
					};

				} else {
					return new EmptyPanel(id);
				}
			}
		};
		tabPanel.addTab("Preview", ".preview");
		tabPanel.addTab("Edit", ".edit");
		add(tabPanel);

	}

}
