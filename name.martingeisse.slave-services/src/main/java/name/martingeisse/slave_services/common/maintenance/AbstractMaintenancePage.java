/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.common.maintenance;

import name.martingeisse.slave_services.application.page.AbstractApplicationPage;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

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
