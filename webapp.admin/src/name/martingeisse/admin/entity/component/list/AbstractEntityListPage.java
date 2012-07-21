/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list;

import name.martingeisse.admin.component.page.AbstractAdminPage;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.component.list.populator.PopulatorBasedEntityListPanel;
import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.list.IEntityListFilterProvider;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.INavigationNodeHandler;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.NavigationUtil;

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
 * 
 * TODO: paging support doesn't work!
 * 
 * This class supports obtaining an entity list filter from the
 * corresponding navigation node. However, this class itself
 * does not use the filter automatically in any way.
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
	
	/**
	 * This method obtains the entity list filter (if any) for this page.
	 * 
	 * The default implementation fetches the filter from the handler of the
	 * navigation node used to reach this page (if any).
	 * 
	 * TODO: Allow {@link PopulatorBasedEntityListPanel} to obtain its populators
	 * from the navigation node the same way as this method does. Goal: Panel
	 * subclasses only needed for special cases and for fragment populators.
	 * 
	 * @return the filter or null
	 */
	public IEntityListFilter getEntityListFilter() {
		NavigationNode navigationNode = NavigationUtil.getNavigationNodeForPage(this);
		if (navigationNode != null) {
			INavigationNodeHandler handler = navigationNode.getHandler();
			if (handler instanceof IEntityListFilterProvider) {
				IEntityListFilterProvider provider = (IEntityListFilterProvider)handler;
				return provider.getEntityListFilter();
			}
		}
		return null;
	}
	
}
