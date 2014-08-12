/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.family;

import name.martingeisse.papyros.backend.PapyrosDataUtil;
import name.martingeisse.papyros.entity.QTemplateFamily;
import name.martingeisse.papyros.entity.TemplateFamily;
import name.martingeisse.papyros.frontend.AbstractFrontendPage;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.wicket.component.stdform.BeanStandardFormPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import com.mysema.query.QueryException;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * This page allows the user to create a template family.
 */
public final class ChangeTemplateFamilyKeyPage extends AbstractFrontendPage {

	/**
	 * the oldKey
	 */
	private final String oldKey;
	
	/**
	 * the keyComponent
	 */
	private final FormComponent<?> keyComponent;
	
	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public ChangeTemplateFamilyKeyPage(PageParameters pageParameters) {

		final TemplateFamily family = PapyrosDataUtil.loadTemplateFamily(pageParameters);
		oldKey = family.getKey();
		add(new Label("templateFamilyKey", oldKey));
		
		BeanStandardFormPanel<TemplateFamily> stdform = new BeanStandardFormPanel<TemplateFamily>("stdform", Model.of(family), true) {
			@Override
			protected void onSubmit() {
				String newKey = getBean().getKey();
				QTemplateFamily qtf = QTemplateFamily.templateFamily;
				SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(qtf);
				update.set(qtf.key, newKey).where(qtf.key.eq(oldKey));
				try {
					update.execute();
					setResponsePage(TemplateFamilyPage.class, new PageParameters().add("key", newKey));
				} catch (QueryException e) {
					keyComponent.error("could not change key");
				}
			};
		};
		keyComponent = stdform.addTextField("Key", "key").setRequired().getFormComponent();
		stdform.addSubmitButton();
		add(stdform);
	}
	
}
