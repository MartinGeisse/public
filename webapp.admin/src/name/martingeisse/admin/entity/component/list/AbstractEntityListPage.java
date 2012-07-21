/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list;

import name.martingeisse.admin.component.page.AbstractAdminPage;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Base class for pages that display a list of entity instances.
 * This class determines the entity from the "entity" page
 * parameter and provides automatic paging support.
 */
public abstract class AbstractEntityListPage extends AbstractAdminPage {

	/**
	 * the entity
	 */
	private EntityDescriptor entity;
	
	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public AbstractEntityListPage(final PageParameters parameters) {
		super(parameters);
		entity = determineEntity(parameters);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.pages.AbstractAdminPage#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
	
		// create components
		String entityNameKey = ("schema.entity." + EntityConfigurationUtil.getGeneralEntityConfiguration().getEntityName(entity));
		StringResourceModel entityDisplayNameModel = new StringResourceModel(entityNameKey, this, null);
		getMainContainer().add(new Label("entityName", entityDisplayNameModel));
		IPageable pageable = getPageable();
		if (pageable == null) {
			getMainContainer().add(new WebComponent("topPagingNavigator"));
			getMainContainer().add(new WebComponent("bottomPagingNavigator"));
		} else {
			getMainContainer().add(new PagingNavigator("topPagingNavigator", pageable));
			getMainContainer().add(new PagingNavigator("bottomPagingNavigator", pageable));
		}
		
	}

	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return entity;
	}
	
	/**
	 * Returns the {@link IPageable} for automatic paging support, or null to disable
	 * automatic paging support.
	 * @return the pageable or null
	 */
	protected abstract IPageable getPageable();
	
}
