/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.page;

import name.martingeisse.admin.component.page.AbstractPaginatedAdminPage;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Base class for pages that display a list of entity instances. This page inherits
 * paging support and adds a page title based on the entity name.
 */
public abstract class AbstractEntityListPage extends AbstractPaginatedAdminPage {
	
	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public AbstractEntityListPage(final PageParameters parameters) {
		super(parameters);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.component.page.AbstractPaginatedAdminPage#createTitleModel()
	 */
	@Override
	protected IModel<String> createTitleModel() {
		IModel<EntityDescriptor> entityTypeModel = determineEntityTypeModel();
		EntityDescriptor entity = entityTypeModel.getObject();
		String entityNameKey = ("schema.entity." + entity.getName());
		return new StringResourceModel(entityNameKey, this, null);
	}

	/**
	 * Determines the model for the entity type for the panel.
	 * @return the model for the entity type.
	 */
	protected abstract IModel<EntityDescriptor> determineEntityTypeModel();
	
}
