/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.family;

import name.martingeisse.papyros.entity.TemplateFamily;
import name.martingeisse.papyros.frontend.AbstractFrontendPage;
import name.martingeisse.wicket.component.stdform.BeanStandardFormPanel;

import org.apache.wicket.model.Model;

/**
 * This page allows the user to create a template family.
 */
public final class CreateTemplateFamilyPage extends AbstractFrontendPage {

	/**
	 * Constructor.
	 */
	public CreateTemplateFamilyPage() {
		BeanStandardFormPanel<TemplateFamily> stdform = new BeanStandardFormPanel<TemplateFamily>("stdform", Model.of(new TemplateFamily()), true);
		stdform.addTextField("Key", "key");
		stdform.addSubmitButton();
		add(stdform);
	}
	
}
