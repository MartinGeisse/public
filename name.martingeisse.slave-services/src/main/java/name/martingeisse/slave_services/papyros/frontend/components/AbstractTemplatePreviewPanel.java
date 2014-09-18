/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.slave_services.papyros.frontend.components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * This panel pulls template source code from its model and a preview
 * data set from a subclass method, to render the template.
 * 
 * This class does not provide any markup. Subclasses must do this,
 * and include a component tag with wicket:id "iframe" for the
 * preview iframe.
 */
public abstract class AbstractTemplatePreviewPanel extends Panel {
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public AbstractTemplatePreviewPanel(String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public AbstractTemplatePreviewPanel(String id, IModel<?> model) {
		super(id, model);
	}

}
