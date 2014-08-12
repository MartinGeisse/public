/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.stdform;

import name.martingeisse.sql.terms.IEntityWithId;

import org.apache.wicket.model.IModel;

/**
 * Specific form panel implementation for editing entity beans.
 * 
 * Only long-typed IDs are currently supported.
 * 
 * @param <T> the entity type
 */
public class EntityStandardFormPanel<T extends IEntityWithId<Long>> extends BeanStandardFormPanel<T> {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the bean model
	 * @param stateless whether to use a stateless form
	 */
	public EntityStandardFormPanel(final String id, final IModel<T> model, final boolean stateless) {
		super(id, model, stateless);
	}

}
