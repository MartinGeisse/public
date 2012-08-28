/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.component.list.page.EntityListPanelPage;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * This handler allows to mount arbitrary panel classes that accept
 * a model of type {@link EntityDescriptor} in the navigation.
 */
public class EntityListPanelHandler extends BookmarkableEntityListNavigationHandler {

	/**
	 * the panelClass
	 */
	private Class<? extends Panel> panelClass;

	/**
	 * Constructor.
	 * @param panelClass the panel class
	 * @param entity the entity type to link to
	 */
	public EntityListPanelHandler(final Class<? extends Panel> panelClass, final EntityDescriptor entity) {
		super(EntityListPanelPage.class, entity);
		this.panelClass = panelClass;
	}

	/**
	 * Constructor.
	 * @param panelClass the panel class
	 * @param entityName the name of the entity to link to
	 */
	public EntityListPanelHandler(final Class<? extends Panel> panelClass, final String entityName) {
		super(EntityListPanelPage.class, entityName);
		this.panelClass = panelClass;
	}

	/**
	 * Getter method for the panelClass.
	 * @return the panelClass
	 */
	public Class<? extends Panel> getPanelClass() {
		return panelClass;
	}

	/**
	 * Setter method for the panelClass.
	 * @param panelClass the panelClass to set
	 * @return this for chaining
	 */
	public EntityListPanelHandler setPanelClass(final Class<? extends Panel> panelClass) {
		this.panelClass = panelClass;
		return this;
	}

}