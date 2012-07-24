/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.WebPage;

/**
 * This handler is used to mount entity list pages. It adds the entity
 * name as an implicit page parameter.
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
public class BookmarkableEntityListNavigationHandler extends BookmarkablePageNavigationHandler {

	/**
	 * the entityName
	 */
	private String entityName;

	/**
	 * the canonicalEntityListNode
	 */
	private boolean canonicalEntityListNode;

	/**
	 * the filter
	 */
	private IEntityListFilter filter;

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
		if (entityName == null) {
			throw new IllegalArgumentException("entityName is null");
		}
		setId(entityName);
		setTitle(entityName);
		this.entityName = entityName;
		this.canonicalEntityListNode = false;
		this.filter = null;
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
	 * @return this for chaining
	 */
	public BookmarkableEntityListNavigationHandler setEntityName(final String entityName) {
		this.entityName = entityName;
		return this;
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

	/**
	 * Getter method for the filter.
	 * @return the filter
	 */
	public IEntityListFilter getFilter() {
		return filter;
	}

	/**
	 * Setter method for the filter.
	 * @param filter the filter to set
	 * @return this for chaining
	 */
	public BookmarkableEntityListNavigationHandler setFilter(final IEntityListFilter filter) {
		this.filter = filter;
		return this;
	}

	/**
	 * Returns the entity for the entity name stored in this handler.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return ApplicationSchema.instance.findEntity(getEntityName());		
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.AbstractNavigationNodeHandler#getEntityNameForCanonicalEntityListNode()
	 */
	@Override
	public String getEntityNameForCanonicalEntityListNode() {
		return (canonicalEntityListNode ? entityName : null);
	}

}
