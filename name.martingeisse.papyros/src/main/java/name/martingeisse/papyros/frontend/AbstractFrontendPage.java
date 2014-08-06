/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.papyros.frontend;

import name.martingeisse.papyros.application.page.AbstractApplicationPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Base class for all front-end pages.
 */
public class AbstractFrontendPage extends AbstractApplicationPage {

	/**
	 * Constructor.
	 */
	public AbstractFrontendPage() {
		super();
	}

	/**
	 * Constructor.
	 * @param model the page model
	 */
	public AbstractFrontendPage(final IModel<?> model) {
		super(model);
	}

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public AbstractFrontendPage(final PageParameters parameters) {
		super(parameters);
	}

}
