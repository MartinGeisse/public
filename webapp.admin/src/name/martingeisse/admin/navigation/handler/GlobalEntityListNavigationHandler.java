/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.NavigationMountedRequestMapper;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.pages.EntityTablePage;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class links to an entity list presentation page.
 */
public final class GlobalEntityListNavigationHandler extends AbstractNavigationNodeHandler {

	/**
	 * the entityName
	 */
	private String entityName;

	/**
	 * the presenterName
	 */
	private String presenterName;

	/**
	 * Constructor.
	 */
	public GlobalEntityListNavigationHandler() {
	}

	/**
	 * Constructor.
	 * @param entity the entity type to link to
	 */
	public GlobalEntityListNavigationHandler(final EntityDescriptor entity) {
		this(entity, null);
	}

	/**
	 * Constructor.
	 * @param entityName the name of the entity to link to
	 */
	public GlobalEntityListNavigationHandler(final String entityName) {
		this(entityName, null);
	}

	/**
	 * Constructor.
	 * @param entity the entity type to link to
	 * @param presenterName the name of the list presenter to link to
	 */
	public GlobalEntityListNavigationHandler(final EntityDescriptor entity, final String presenterName) {
		this(entity.getTableName(), presenterName);
	}

	/**
	 * Constructor.
	 * @param entityName the name of the entity to link to
	 * @param presenterName the name of the list presenter to link to
	 */
	public GlobalEntityListNavigationHandler(final String entityName, final String presenterName) {
		this.entityName = entityName;
		this.presenterName = presenterName;
	}

	/**
	 * Getter method for the entityName.
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * Setter method for the entityName.
	 * @param entityName the entityName to set
	 */
	public void setEntityName(final String entityName) {
		this.entityName = entityName;
	}

	/**
	 * Getter method for the presenterName.
	 * @return the presenterName
	 */
	public String getPresenterName() {
		return presenterName;
	}

	/**
	 * Setter method for the presenterName.
	 * @param presenterName the presenterName to set
	 */
	public void setPresenterName(final String presenterName) {
		this.presenterName = presenterName;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.AbstractNavigationNodeHandler#mountRequestMappers(name.martingeisse.admin.application.wicket.AdminWicketApplication, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public void mountRequestMappers(AdminWicketApplication application, NavigationNode node) {
		application.mount(new NavigationMountedRequestMapper(node.getPath(), EntityTablePage.class));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createLink(java.lang.String, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public AbstractLink createLink(final String id, NavigationNode node) {
		final PageParameters parameters = new PageParameters();
		parameters.add(NavigationMountedRequestMapper.PAGE_PARAMETER_NAME, node.getPath());
		parameters.add("entity", entityName);
		if (presenterName != null) {
			parameters.add("presenter", presenterName);
		}
		return new BookmarkablePageLink<Void>(id, EntityTablePage.class, parameters);
	}

}
