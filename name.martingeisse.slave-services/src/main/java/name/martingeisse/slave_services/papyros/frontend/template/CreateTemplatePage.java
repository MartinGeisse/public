/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.papyros.frontend.template;

import name.martingeisse.slave_services.common.frontend.AbstractFrontendPage;
import name.martingeisse.slave_services.entity.Template;
import name.martingeisse.slave_services.entity.TemplateFamily;
import name.martingeisse.slave_services.papyros.backend.PapyrosDataUtil;
import name.martingeisse.slave_services.papyros.frontend.family.TemplateFamilyPage;
import name.martingeisse.wicket.component.stdform.BeanStandardFormPanel;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.mysema.query.QueryException;

/**
 * This page allows the user to create a template family.
 */
public final class CreateTemplatePage extends AbstractFrontendPage {

	/**
	 * the languageKeyComponent
	 */
	private final FormComponent<?> languageKeyComponent;
	
	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public CreateTemplatePage(PageParameters pageParameters) {
		final TemplateFamily family = PapyrosDataUtil.loadTemplateFamily(pageParameters);
		add(new BookmarkablePageLink<>("templateFamilyLink", TemplateFamilyPage.class, new PageParameters().add("key", family.getKey())));
		
		BeanStandardFormPanel<Template> stdform = new BeanStandardFormPanel<Template>("stdform", Model.of(new Template()), true) {
			@Override
			protected void onSubmit() {
				Template template = getBean();
				template.setTemplateFamilyId(family.getId());
				template.setContent("");
				try {
					template.insert();
				} catch (QueryException e) {
					System.out.println(e);
				}
				if (template.getId() == null) {
					languageKeyComponent.error("could not create template");
				} else {
					setResponsePage(TemplatePage.class, new PageParameters().add("key", family.getKey()).add("language", template.getLanguageKey()));
				}
			};
		};
		languageKeyComponent = stdform.addTextField("Language Key", "languageKey").setRequired().getFormComponent();
		stdform.addSubmitButton();
		add(stdform);
	}
	
}
