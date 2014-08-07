/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.maintenance;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import name.martingeisse.papyros.application.page.AbstractApplicationPage;

/**
 * Base class for all "maintenance area" pages.
 */
public class AbstractMaintenancePage extends AbstractApplicationPage {

	/**
	 * Constructor.
	 */
	public AbstractMaintenancePage() {
		super();
	}

	/**
	 * Constructor.
	 * @param model the page model
	 */
	public AbstractMaintenancePage(IModel<?> model) {
		super(model);
	}

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public AbstractMaintenancePage(PageParameters parameters) {
		super(parameters);
	}

}
