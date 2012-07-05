/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.single;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * This class acts as a factory for entity instance presentation.
 * 
 * A user invokes a presenter by passing an URL that selects an entity
 * class, entity ID, and presenter name. This fetches the entity instance
 * by ID, then uses the presenter to create a Wicket panel that allows
 * to interact with the entity instance.
 * 
 * Entity presenters are by default not restricted to specific entity
 * classes, table names, or similar. That is, it is entirely possible
 * to create a presenter that can present any instance of any entity.
 */
public interface ISingleEntityPresenter {

	/**
	 * Returns the presenter's URL ID. This ID is used to mount a page for
	 * the presenter.
	 * @return the URL ID
	 */
	public String getUrlId();
	
	/**
	 * Returns the presenter's title. This is used for displaying a link
	 * to the presenter's page.
	 * @return the title
	 */
	public String getTitle();

	/**
	 * Creates a Wicket panel for presentation of an entity instance.
	 * @param id the wicket id
	 * @param model the model that returns the entity instance
	 * @return the panel
	 */
	public Panel createPanel(String id, IModel<EntityInstance> model);
	
}
