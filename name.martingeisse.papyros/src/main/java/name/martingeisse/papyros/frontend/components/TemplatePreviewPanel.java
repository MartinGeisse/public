/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * This panel pulls template source code from its model and renders it
 * using a user-configurable data set. It takes a template family key
 * to find the list of selectable data sets.
 */
public class TemplatePreviewPanel extends Panel {
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public TemplatePreviewPanel(String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public TemplatePreviewPanel(String id, IModel<String> model) {
		super(id, model);
	}
	
}
