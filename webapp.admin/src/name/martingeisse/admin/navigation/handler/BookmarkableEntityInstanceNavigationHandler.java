/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.IEntityNameAware;
import name.martingeisse.admin.navigation.INavigationNodeHandler;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.string.StringValue;

/**
 * This handler is intended for use within entity-local
 * navigation trees, and especially the general template
 * for such trees. When the template is cloned for an entity,
 * this handler receives the entity name and stores it
 * in an implicit page parameter called "entity".
 */
public class BookmarkableEntityInstanceNavigationHandler extends BookmarkablePageNavigationHandler implements IEntityNameAware {

	/**
	 * Constructor.
	 * @param pageClass the page class
	 */
	public BookmarkableEntityInstanceNavigationHandler(Class<? extends WebPage> pageClass) {
		super(pageClass);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.IEntityNameAware#getEntityName()
	 */
	@Override
	public String getEntityName() {
		StringValue value = getImplicitPageParameters().get("entity");
		return (value == null ? null : value.toString());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.IEntityNameAware#setEntityName(java.lang.String)
	 */
	@Override
	public void setEntityName(String entityName) {
		getImplicitPageParameters().remove("entity");
		getImplicitPageParameters().add("entity", entityName);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.BookmarkablePageNavigationHandler#createClone()
	 */
	@Override
	public INavigationNodeHandler createClone() {
		BookmarkableEntityInstanceNavigationHandler clone = new BookmarkableEntityInstanceNavigationHandler(getPageClass());
		clone.mergeDataFrom(this);
		return clone;
	}

}
