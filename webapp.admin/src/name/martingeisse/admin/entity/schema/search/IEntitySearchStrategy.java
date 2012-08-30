/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.search;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

import com.mysema.query.types.Predicate;

/**
 * Given a search term, this strategy is able to create a {@link Predicate}
 * for the entity for which it was returned by an {@link IEntitySearchContributor}.
 */
public interface IEntitySearchStrategy {

	/**
	 * Creates a new filter for the specified entity and search term.
	 * @param entity the entity
	 * @param searchTerm the search term
	 * @return the filter
	 */
	public Predicate createFilter(EntityDescriptor entity, String searchTerm);

}
