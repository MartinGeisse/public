/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.list.EntityListFilter;
import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.NavigationNode;

import org.apache.wicket.markup.html.WebPage;

import com.mysema.query.types.Predicate;

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
		this(pageClass, entity.getName());
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
		this.entityName = entityName;
		this.canonicalEntityListNode = false;
		this.filter = null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.AbstractNavigationNodeHandler#getFallbackId(name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public String getFallbackId(NavigationNode node) {
		return entityName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.AbstractNavigationNodeHandler#getFallbackTitle(name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public String getFallbackTitle(NavigationNode node) {
		return entityName;
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
	 * Sets a newly created {@link EntityListFilter} from the specified filter expression.
	 * @param filterPredicate the filter predicate
	 * @return this for chaining
	 */
	public BookmarkableEntityListNavigationHandler setFilter(final Predicate filterPredicate) {
		return setFilter(new EntityListFilter(filterPredicate));
	}

	/**
	 * Returns the entity for the entity name stored in this handler.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return ApplicationSchema.instance.findEntity(getEntityName());		
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.BookmarkablePageNavigationHandler#prepareMount(name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	protected void prepareMount(NavigationNode node) {
		super.prepareMount(node);
		if (getEntity() == null) {
			throw new IllegalStateException("unknown entity in navigation node " + node.getPath() + ": "+ getEntityName());
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.AbstractNavigationNodeHandler#getEntityNameForCanonicalEntityListNode()
	 */
	@Override
	public String getEntityNameForCanonicalEntityListNode() {
		return (canonicalEntityListNode ? entityName : null);
	}

}
