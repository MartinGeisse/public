/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.instance.page;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.NavigationUtil;
import name.martingeisse.admin.navigation.handler.EntityInstancePanelHandler;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class allows to mount an entity instance panel in the navigation
 * in combination with {@link EntityInstancePanelHandler}.
 */
public class EntityInstancePanelPage extends AbstractEntityInstancePanelPage {

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public EntityInstancePanelPage(final PageParameters parameters) {
		super(parameters);
	}

	/**
	 * @return the navigation node handler
	 */
	private EntityInstancePanelHandler getHandler() {
		return (EntityInstancePanelHandler)(NavigationUtil.getNavigationNodeForPage(this).getHandler());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.instance.page.AbstractEntityInstancePanelPage#determineEntityType()
	 */
	@Override
	protected EntityDescriptor determineEntityType() {
		return getHandler().getEntity();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.instance.page.AbstractEntityInstancePanelPage#determinePanelClass()
	 */
	@Override
	protected Class<? extends Panel> determinePanelClass() {
		return getHandler().getPanelClass();
	}

}
