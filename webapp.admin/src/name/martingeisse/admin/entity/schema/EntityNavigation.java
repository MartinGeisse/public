/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import name.martingeisse.admin.navigation.NavigationNode;

/**
 * Encapsulates entity descriptor methods that are related
 * to the navigation tree.
 */
public final class EntityNavigation {

	/**
	 * the entity
	 */
	private final EntityDescriptor entity;
	
	/**
	 * the instanceNavigationRootNode
	 */
	private final NavigationNode instanceNavigationRootNode;

	/**
	 * Constructor.
	 * @param entity the entity
	 * @param instanceNavigationRootNode the navigation root node
	 */
	public EntityNavigation(EntityDescriptor entity, NavigationNode instanceNavigationRootNode) {
		this.entity = entity;
		this.instanceNavigationRootNode = instanceNavigationRootNode;
	}
	
	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return entity;
	}
	
	/**
	 * Getter method for the instanceNavigationRootNode.
	 * @return the instanceNavigationRootNode
	 */
	public NavigationNode getInstanceNavigationRootNode() {
		return instanceNavigationRootNode;
	}
	
	/**
	 * Returns one of the navigation nodes associated with instances of this entity.
	 * @param subpathSegments the subpath segments to walk from the entity instance navigation root
	 * to reach the node to link. The specified node must exist, otherwise this method throws an
	 * {@link IllegalArgumentException}.
	 * @return the navigation node
	 */
	public NavigationNode getInstanceNavigationNode(final String... subpathSegments) {
		NavigationNode node = instanceNavigationRootNode;
		for (final String segment : subpathSegments) {
			final NavigationNode child = node.findChildById(segment);
			if (child == null) {
				throw new IllegalArgumentException("subpath segment '" + segment + "' not found in node " + node.getPath());
			}
			node = child;
		}
		return node;
	}
	
}
