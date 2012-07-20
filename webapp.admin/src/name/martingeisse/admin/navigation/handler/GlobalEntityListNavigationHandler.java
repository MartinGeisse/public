/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.pages.EntityTablePage;

/**
 * This class links to an entity list presentation page.
 */
public final class GlobalEntityListNavigationHandler extends BookmarkablePageNavigationHandler {

	/**
	 * the canonicalEntityListNode
	 */
	private boolean canonicalEntityListNode;

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
		setId(entityName);
		setTitle(entityName);
		getImplicitPageParameters().add("entity", entityName);
		if (presenterName != null) {
			getImplicitPageParameters().add("presenter", presenterName);
		}
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
	public GlobalEntityListNavigationHandler setCanonicalEntityListNode(final boolean canonicalEntityListNode) {
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
