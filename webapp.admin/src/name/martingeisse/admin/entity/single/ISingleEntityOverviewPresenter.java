/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.single;

import java.io.Serializable;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * This class acts as a factory for entity instance overview presentation.
 * Overview presentation differs from regular presenters in that only a 
 * single overview presenter will be used. This presenter is used when
 * the user views an entity instance page, no matter which regular presenter
 * is currently being displayed. Contributors pass overview presenters with
 * a score that determines which presenter is used for a specific entity type.
 * 
 * Entity overview presenters are by default not restricted to specific entity
 * classes, table names, or similar. That is, it is entirely possible
 * to create a presenter that can present any instance of any entity.
 */
public interface ISingleEntityOverviewPresenter extends Serializable {

	/**
	 * Creates a Wicket panel for overview presentation of an entity instance.
	 * @param id the wicket id
	 * @param model the model that returns the entity instance
	 * @return the panel
	 */
	public Panel createPanel(String id, IModel<EntityInstance> model);
	
}
