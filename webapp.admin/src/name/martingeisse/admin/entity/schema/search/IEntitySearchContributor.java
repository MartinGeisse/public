/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.search;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * Instances contribute {@link IEntitySearchStrategy} with a score.
 * For each entity, the contributor with the highest score gets to
 * provide the search strategy actually used.
 */
public interface IEntitySearchContributor {

	/**
	 * Returns the score of this contributor for the specified entity.
	 * This method must return {@link Integer#MIN_VALUE} if the entity
	 * is not supported by this contributor.
	 * @param entity the entity
	 * @return the score
	 */
	public int getScore(EntityDescriptor entity);

	/**
	 * Returns the search strategy for the specified entity.
	 * @param entity the entity
	 * @return the search strategy
	 */
	public IEntitySearchStrategy getSearchStrategy(EntityDescriptor entity);

}
