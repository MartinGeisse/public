/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.pages.EntityTablePage;

import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class links to an entity list presentation page.
 */
public final class GlobalEntityListNavigationHandler extends BookmarkablePageNavigationHandler {

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
		super(EntityTablePage.class);
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
	 * @see name.martingeisse.admin.navigation.handler.BookmarkablePageNavigationHandler#addImplicitParameters(org.apache.wicket.request.mapper.parameter.PageParameters)
	 */
	@Override
	protected void addImplicitParameters(PageParameters parameters) {
		parameters.add("entity", entityName);
		if (presenterName != null) {
			parameters.add("presenter", presenterName);
		}
	}
	
}
