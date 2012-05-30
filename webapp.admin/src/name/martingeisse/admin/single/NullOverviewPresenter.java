/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.single;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * This presenter uses an {@link EmptyPanel} and thus shows no overview at all.
 */
public class NullOverviewPresenter implements ISingleEntityOverviewPresenter {

	/**
	 * A shared instance of this class.
	 */
	public static final NullOverviewPresenter instance = new NullOverviewPresenter();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.single.ISingleEntityOverviewPresenter#createPanel(java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public Panel createPanel(String id, IModel<EntityInstance> model) {
		return new EmptyPanel(id);
	}

}
