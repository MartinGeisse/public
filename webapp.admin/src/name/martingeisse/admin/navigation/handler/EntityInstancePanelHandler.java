/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import org.apache.wicket.markup.html.panel.Panel;

import name.martingeisse.admin.entity.component.instance.EntityInstancePanelPage;
import name.martingeisse.admin.entity.instance.EntityInstance;

/**
 * This handler allows to mount arbitrary panel classes that accept
 * a model of type {@link EntityInstance} in the navigation. This
 * handler must only be used in entity-local navigation.
 * 
 * TODO: This class currently demands that the class is public, such
 * that it can be obtained from the class loader and instantiated. This
 * is necessary because only the class name is passed as a page parameter,
 * and upon receiving a request the page is instantiated. This can be
 * fixed by using a global registry instead of the class loader -- this
 * would allow to register private classes as well as arbitrary page factories
 * and also allow to use a key string instead of the fully qualified
 * class name. Something like a "global factory service"?
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
