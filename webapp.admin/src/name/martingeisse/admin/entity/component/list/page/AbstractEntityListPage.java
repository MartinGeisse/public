/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.page;

import name.martingeisse.admin.component.page.AbstractAdminPage;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Base class for pages that display a list of entity instances.
 * This class determines the entity from the "entity" page
 * parameter and provides automatic paging support.
 * 
 * TODO: paging support doesn't work!
 * 
 * This class supports obtaining an entity list filter from the
 * corresponding navigation node. However, this class itself
 * does not use the filter automatically in any way.
 * 
 * TODO: Rename to AbstractPaginatedAdminPage
 */
public abstract class AbstractEntityListPage extends AbstractAdminPage {
	
	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public AbstractEntityListPage(final PageParameters parameters) {
		super(parameters);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.pages.AbstractAdminPage#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
	
		// create components
		String entityNameKey = ("schema.entity." + EntityConfigurationUtil.getGeneralEntityConfiguration().getEntityName(determineEntityTypeModel().getObject()));
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
	 * Determines the model for the entity type for the panel.
	 * @return the model for the entity type.
	 */
	protected abstract IModel<EntityDescriptor> determineEntityTypeModel();
	
	/**
	 * Returns the {@link IPageable} for automatic paging support, or null to disable
	 * automatic paging support.
	 * @return the pageable or null
	 */
	protected abstract IPageable getPageable();
	
}
