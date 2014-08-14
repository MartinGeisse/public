/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.family;

import name.martingeisse.papyros.entity.TemplateFamily;
import name.martingeisse.papyros.frontend.AbstractFrontendPage;
import name.martingeisse.wicket.component.stdform.BeanStandardFormPanel;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import com.mysema.query.QueryException;

/**
 * This page allows the user to create a template family.
 */
public final class CreateTemplateFamilyPage extends AbstractFrontendPage {

	/**
	 * the keyComponent
	 */
	private final FormComponent<?> keyComponent;
	
	/**
	 * Constructor.
	 */
	public CreateTemplateFamilyPage() {
		add(new BookmarkablePageLink<>("templateFamilyListLink", TemplateFamilyListPage.class));
		BeanStandardFormPanel<TemplateFamily> stdform = new BeanStandardFormPanel<TemplateFamily>("stdform", Model.of(new TemplateFamily()), true) {
			@Override
			protected void onSubmit() {
				TemplateFamily templateFamily = getBean();
				try {
					templateFamily.insert();
				} catch (QueryException e) {
					System.out.println(e);
				}
				if (templateFamily.getId() == null) {
					keyComponent.error("could not create template family");
				} else {
					setResponsePage(TemplateFamilyPage.class, new PageParameters().add("key", templateFamily.getKey()));
				}
			};
		};
		keyComponent = stdform.addTextField("Key", "key").setRequired().getFormComponent();
		stdform.addSubmitButton();
		add(stdform);
	}
	
}
