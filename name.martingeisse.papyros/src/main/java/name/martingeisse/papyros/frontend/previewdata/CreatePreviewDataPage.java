/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.previewdata;

import name.martingeisse.papyros.backend.PapyrosDataUtil;
import name.martingeisse.papyros.entity.PreviewDataSet;
import name.martingeisse.papyros.entity.Template;
import name.martingeisse.papyros.entity.TemplateFamily;
import name.martingeisse.papyros.frontend.AbstractFrontendPage;
import name.martingeisse.papyros.frontend.family.TemplateFamilyPage;
import name.martingeisse.wicket.component.stdform.BeanStandardFormPanel;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import com.mysema.query.QueryException;

/**
 * This page allows the user to create a preview data set.
 */
public final class CreatePreviewDataPage extends AbstractFrontendPage {

	/**
	 * the nameComponent
	 */
	// private final FormComponent<?> nameComponent;
	
	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public CreatePreviewDataPage(PageParameters pageParameters) {
		final TemplateFamily family = PapyrosDataUtil.loadTemplateFamily(pageParameters);
		add(new BookmarkablePageLink<>("templateFamilyLink", TemplateFamilyPage.class, new PageParameters().add("key", family.getKey())));
		/*
		BeanStandardFormPanel<PreviewDataSet> stdform = new BeanStandardFormPanel<PreviewDataSet>("stdform", Model.of(new PreviewDataSet()), true) {
			@Override
			protected void onSubmit() {
				PreviewDataSet previewDataSet = getBean();
				previewDataSet.setTemplateFamilyId(family.getId());
				previewDataSet.setPreviewDataSetNumber(0); // TODO assign a number
				previewDataSet.setOrderIndex(0);
				previewDataSet.setData("null");
				previewDataSet.set
				previewDataSet.setContent("");
				try {
					previewDataSet.insert();
				} catch (QueryException e) {
					System.out.println(e);
				}
				if (previewDataSet.getId() == null) {
					languageKeyComponent.error("could not create template");
				} else {
					setResponsePage(TemplatePage.class, new PageParameters().add("key", family.getKey()).add("language", previewDataSet.getLanguageKey()));
				}
			};
		};
		languageKeyComponent = stdform.addTextField("Language Key", "languageKey").setRequired().getFormComponent();
		stdform.addSubmitButton();
		add(stdform);
		*/
	}
	
}
