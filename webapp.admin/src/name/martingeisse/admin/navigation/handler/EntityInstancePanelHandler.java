/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import org.apache.wicket.markup.html.panel.Panel;

import name.martingeisse.admin.entity.single.EntityInstance;
import name.martingeisse.admin.entity.single.EntityInstancePanelPage;

/**
 * This handler allows to mount arbitrary panel classes that accept
 * a model of type {@link EntityInstance} in the navigation. This
 * handler must only be used in entity-local navigation.
 */
public class EntityInstancePanelHandler extends BookmarkableEntityInstanceNavigationHandler {

	/**
	 * Constructor.
	 * @param panelClass the panel class
	 */
	public EntityInstancePanelHandler(Class<? extends Panel> panelClass) {
		super(EntityInstancePanelPage.class);
		getImplicitPageParameters().add("panelClass", panelClass.getName());
	}

}
