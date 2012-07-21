/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.component.page.AbstractAdminPage;
import name.martingeisse.admin.entity.component.list.AbstractEntityListPage;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.INavigationLocator;

import org.apache.wicket.markup.html.WebPage;

/**
 * This handler is used to mount entity list pages. It adds the entity
 * name as an implicit page parameter.
 * 
 * TODO: Allow {@link AbstractEntityListPage} to obtain the implicit navigation
 * path page parameter, obtain this node, then store entity list filters etc. here.
 * 
 * TODO: The functionality to obtain the navigation node for a page is already there.
 * Add a convenience function to {@link AbstractAdminPage}. Make it clear what the
 * convenience function does and how it differs from the page's implementation
 * of {@link INavigationLocator} and the PP itself.
 * 
 * A node of this type can be the canonical list node of the respective entity. Only
 * one such node can exist for each entity. This causes the navigation system to mount
 * the root node of instance-local navigation for the entity as a child of this node.
 * The typical candidate for being the canonical list node for an entity is an
 * unfiltered list node for the entity.
 * 
 * This page uses the entity name as a default for the node id and title.
 * 
 * TODO: add display name functionality to {@link EntityDescriptor} and use that
 * as the default for the node title.
 */
public final class BookmarkableEntityListNavigationHandler extends BookmarkablePageNavigationHandler {

	/**
	 * the canonicalEntityListNode
	 */
	private boolean canonicalEntityListNode;

	/**
	 * Constructor.
	 * @param pageClass the page class
	 * @param entity the entity type to link to
	 */
	public BookmarkableEntityListNavigationHandler(final Class<? extends WebPage> pageClass, final EntityDescriptor entity) {
		this(pageClass, entity.getTableName());
	}

	/**
	 * Constructor.
	 * @param pageClass the page class
	 * @param entityName the name of the entity to link to
	 */
	public BookmarkableEntityListNavigationHandler(final Class<? extends WebPage> pageClass, final String entityName) {
		super(pageClass);
		setId(entityName);
		setTitle(entityName);
		getImplicitPageParameters().add("entity", entityName);
		this.canonicalEntityListNode = false;
	}

	/**
	 * Getter method for the canonicalEntityListNode.
	 * @return the canonicalEntityListNode
	 */
	public boolean isCanonicalEntityListNode() {
		return canonicalEntityListNode;
	}

	/**
	 * Setter method for the canonicalEntityListNode.
	 * @param canonicalEntityListNode the canonicalEntityListNode to set
	 * @return this for chaining
	 */
	public BookmarkableEntityListNavigationHandler setCanonicalEntityListNode(final boolean canonicalEntityListNode) {
		this.canonicalEntityListNode = canonicalEntityListNode;
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.AbstractNavigationNodeHandler#getEntityNameForCanonicalEntityListNode()
	 */
	@Override
	public String getEntityNameForCanonicalEntityListNode() {
		return (canonicalEntityListNode ? getImplicitPageParameters().get("entity").toString() : null);
	}

}
