/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.page;

import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.NavigationUtil;
import name.martingeisse.admin.navigation.handler.EntityListPanelHandler;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class allows to mount an entity list panel in the navigation
 * in combination with {@link EntityListPanelHandler}.
 */
public final class EntityListPanelPage extends AbstractEntityListPanelPage {

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public EntityListPanelPage(final PageParameters parameters) {
		super(parameters);
	}

	/**
	 * @return the navigation node handler
	 */
	private EntityListPanelHandler getHandler() {
		return (EntityListPanelHandler)(NavigationUtil.getNavigationNodeForPage(this).getHandler());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.page.AbstractEntityListPanelPage#determineEntityTypeModel()
	 */
	@Override
	protected IModel<EntityDescriptor> determineEntityTypeModel() {
		return new LoadableDetachableModel<EntityDescriptor>() {
			@Override
			protected EntityDescriptor load() {
				return getHandler().getEntity();
			}
		};
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.page.AbstractEntityListPanelPage#determinePanelClass()
	 */
	@Override
	protected Class<? extends Panel> determinePanelClass() {
		return getHandler().getPanelClass();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.page.AbstractEntityListPanelPage#determineEntityListFilter()
	 */
	@Override
	protected IEntityListFilter determineEntityListFilter() {
		return getHandler().getFilter();
	}

}
