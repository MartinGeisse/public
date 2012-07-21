/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.component.list.EntityListPanelPage;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * This handler allows to mount arbitrary panel classes that accept
 * a model of type {@link EntityDescriptor} in the navigation.
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
public class EntityListPanelHandler extends BookmarkableEntityListNavigationHandler {

	/**
	 * Constructor.
	 * @param panelClass the panel class
	 * @param entity the entity type to link to
	 */
	public EntityListPanelHandler(Class<? extends Panel> panelClass, final EntityDescriptor entity) {
		super(EntityListPanelPage.class, entity);
		getImplicitPageParameters().add("panelClass", panelClass.getName());
	}

	/**
	 * Constructor.
	 * @param panelClass the panel class
	 * @param entityName the name of the entity to link to
	 */
	public EntityListPanelHandler(Class<? extends Panel> panelClass, final String entityName) {
		super(EntityListPanelPage.class, entityName);
		getImplicitPageParameters().add("panelClass", panelClass.getName());
	}

}
