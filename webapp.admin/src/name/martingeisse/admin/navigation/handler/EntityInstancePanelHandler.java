/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.component.instance.page.NavigationMountedEntityInstancePanelPage;
import name.martingeisse.admin.entity.instance.RawEntityInstance;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * This handler allows to mount arbitrary panel classes that accept
 * a model of type {@link RawEntityInstance} in the navigation. This
 * handler must only be used in entity-local navigation.
 */
public class EntityInstancePanelHandler extends BookmarkableEntityInstanceNavigationHandler {

	/**
	 * the panelClass
	 */
	private Class<? extends Panel> panelClass;

	/**
	 * Constructor.
	 * @param panelClass the panel class
	 */
	public EntityInstancePanelHandler(final Class<? extends Panel> panelClass) {
		super(NavigationMountedEntityInstancePanelPage.class);
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
	public EntityInstancePanelHandler setPanelClass(final Class<? extends Panel> panelClass) {
		this.panelClass = panelClass;
		return this;
	}

}
