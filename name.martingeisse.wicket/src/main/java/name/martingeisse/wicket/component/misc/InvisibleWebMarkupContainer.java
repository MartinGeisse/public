/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.misc;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * This container is invisible (in the Wicket sense). It can be used
 * as a default placeholder for other optional components. Since it is
 * invisible, Wicket enclosures can react to it.
 * 
 * Note that this component won't output its component tag, so it cannot
 * be the target of an AJAX re-render (though any ancestor will do).
 */
public final class InvisibleWebMarkupContainer extends WebMarkupContainer {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public InvisibleWebMarkupContainer(final String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public InvisibleWebMarkupContainer(final String id, final IModel<?> model) {
		super(id, model);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return false;
	}
	
}
