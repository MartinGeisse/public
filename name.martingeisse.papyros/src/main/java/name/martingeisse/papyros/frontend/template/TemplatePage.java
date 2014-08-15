/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.template;

import name.martingeisse.papyros.backend.PapyrosDataUtil;
import name.martingeisse.papyros.backend.RenderTemplateAction;
import name.martingeisse.papyros.entity.PreviewDataSet;
import name.martingeisse.papyros.entity.Template;
import name.martingeisse.papyros.entity.TemplateFamily;
import name.martingeisse.papyros.frontend.AbstractFrontendPage;
import name.martingeisse.papyros.frontend.components.Iframe;
import name.martingeisse.papyros.frontend.components.PageParameterDrivenTabPanel;
import name.martingeisse.papyros.frontend.family.TemplateFamilyPage;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.json.simple.JSONValue;

/**
 * The main page to manage a template.
 */
public class TemplatePage extends AbstractFrontendPage {

	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public TemplatePage(PageParameters pageParameters) {
		super(pageParameters);
		final Pair<Template, TemplateFamily> pair = PapyrosDataUtil.loadTemplateAndTemplateFamily(pageParameters);
		final Template template = pair.getLeft();
		final TemplateFamily family = pair.getRight();
		
		add(new BookmarkablePageLink<>("templateFamilyLink", TemplateFamilyPage.class, new PageParameters().add("key", family.getKey())));
		add(new Label("templateFamilyKey", family.getKey()));
		add(new Label("languageKey", template.getLanguageKey()));
		
		PageParameterDrivenTabPanel tabPanel = new PageParameterDrivenTabPanel("tabs", "tab") {
			@Override
			protected Component createBody(String id, StringValue selector) {
				String selectorValue = selector.toString(".preview");
				if (selectorValue.equals(".preview")) {
					
					final PreviewDataSet previewDataSet = PapyrosDataUtil.loadPreviewDataSet(template.getTemplateFamilyId(), 0);
					final Object previewData = (previewDataSet == null ? null : JSONValue.parse(previewDataSet.getData()));
					final RenderTemplateAction renderTemplateAction = new RenderTemplateAction();
					renderTemplateAction.setTemplate(template.getContent());
					renderTemplateAction.setData(previewData);
					final String renderedContent = renderTemplateAction.render();
					
					Fragment fragment = new Fragment(id, "tabPreview", TemplatePage.this);
					fragment.add(new Iframe("iframe", Model.of(renderedContent)));
					return fragment;
					
				} else if (selectorValue.equals(".edit")) {
					Fragment fragment = new Fragment(id, "tabEdit", TemplatePage.this);
					fragment.add(new BookmarkablePageLink<Void>("editLink", EditTemplatePage.class, new PageParameters().add("key", family.getKey()).add("language", template.getLanguageKey())));
					return fragment;
				} else if (selectorValue.equals(".preview-data")) {
					return new Fragment(id, "tabPreviewData", TemplatePage.this);
				} else if (selectorValue.equals(".schema")) {
					return new Fragment(id, "tabSchema", TemplatePage.this);
				} else if (selectorValue.equals(".configuration")) {
					return new Fragment(id, "tabConfiguration", TemplatePage.this);
				} else {
					return new EmptyPanel(id);
				}
			}
		};
		tabPanel.addTab("Preview", ".preview");
		tabPanel.addTab("Edit", ".edit");
		tabPanel.addDropdownTab("Advanced").addTab("Preview Data", ".preview-data").addTab("Schema", ".schema").addTab("Configuration", ".configuration");
		add(tabPanel);
		
	}

}
