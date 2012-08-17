/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import name.martingeisse.admin.navigation.NavigationNode;

/**
 * Implementations contribute to the local navigation tree of each entity.
 */
public interface IEntityNavigationContributor {

	/**
	 * Contributes navigation nodes to the local navigation tree of the
	 * specified entity.
	 * @param entity the entity
	 * @param mainEntityInstanceNode the main navigation node for instances
	 * of the entity, i.e. the root node of the local navigation.
	 */
	public void contributeNavigationNodes(EntityDescriptor entity, NavigationNode mainEntityInstanceNode);

}
