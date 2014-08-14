/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.template;

import name.martingeisse.papyros.backend.PapyrosDataUtil;
import name.martingeisse.papyros.entity.Template;
import name.martingeisse.papyros.entity.TemplateFamily;
import name.martingeisse.papyros.frontend.AbstractFrontendPage;
import name.martingeisse.papyros.frontend.components.PageParameterDrivenTabPanel;
import name.martingeisse.papyros.frontend.family.TemplateFamilyPage;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

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
		
		add(new Label("templateFamilyKey", family.getKey()));
		add(new Label("languageKey", template.getLanguageKey()));
		add(new BookmarkablePageLink<Void>("editLink", EditTemplatePage.class, new PageParameters().add("key", family.getKey()).add("language", template.getLanguageKey())));
		add(new BookmarkablePageLink<Void>("renderLink", TestRenderPage.class, new PageParameters().add("key", family.getKey()).add("language", template.getLanguageKey())));
		
		add(new BookmarkablePageLink<>("templateFamilyLink", TemplateFamilyPage.class, new PageParameters().add("key", family.getKey())));
		
		
		
		
		// tab test
		PageParameterDrivenTabPanel tabPanel = new PageParameterDrivenTabPanel("tabTest", "tab") {
			@Override
			protected Component createBody(String id, StringValue selector) {
				return new Fragment(id, selector.toString("tabTestBody1"), TemplatePage.this);
			}
		};
		tabPanel.addTab("tabTestBody1", "Foo");
		tabPanel.addTab("tabTestBody2", "Bar");
		tabPanel.addTab("tabTestBody3", "Fupp");
		add(tabPanel);
		
	}

}
