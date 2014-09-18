/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.slave_services.papyros.frontend.previewdata;

import name.martingeisse.slave_services.common.frontend.AbstractFrontendPage;
import name.martingeisse.slave_services.entity.TemplateFamily;
import name.martingeisse.slave_services.entity.TemplatePreviewDataSet;
import name.martingeisse.slave_services.papyros.backend.PapyrosDataUtil;
import name.martingeisse.slave_services.papyros.frontend.family.TemplateFamilyPage;
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
	 * the keyComponent
	 */
	private final FormComponent<?> keyComponent;
	
	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public CreatePreviewDataPage(PageParameters pageParameters) {
		final TemplateFamily family = PapyrosDataUtil.loadTemplateFamily(pageParameters);
		add(new BookmarkablePageLink<>("templateFamilyLink", TemplateFamilyPage.class, new PageParameters().add("key", family.getKey())));
		BeanStandardFormPanel<TemplatePreviewDataSet> stdform = new BeanStandardFormPanel<TemplatePreviewDataSet>("stdform", Model.of(new TemplatePreviewDataSet()), true) {
			@Override
			protected void onSubmit() {
				TemplatePreviewDataSet previewDataSet = getBean();
				previewDataSet.setTemplateFamilyId(family.getId());
				previewDataSet.setOrderIndex(0);
				previewDataSet.setData("null");
				try {
					previewDataSet.insert();
				} catch (QueryException e) {
					System.out.println(e);
				}
				if (previewDataSet.getId() == null) {
					keyComponent.error("could not create template");
				} else {
					setResponsePage(PreviewDataPage.class, new PageParameters().add("key", family.getKey()).add("previewDataKey", previewDataSet.getPreviewDataKey()));
				}
			};
		};
		keyComponent = stdform.addTextField("Preview Data Key", "previewDataKey").setRequired().getFormComponent();
		stdform.addTextField("Name", "name").setRequired();
		stdform.addSubmitButton();
		add(stdform);
	}
	
}
