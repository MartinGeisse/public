/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.search;

import com.mysema.query.types.Predicate;

import name.martingeisse.admin.entity.EntityCapabilities;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * This class wraps the code that invokes an {@link IEntitySearchStrategy}.
 */
public final class EntitySearcher {

	/**
	 * the entity
	 */
	private final EntityDescriptor entity;
	
	/**
	 * the strategy
	 */
	private final IEntitySearchStrategy strategy;
	
	/**
	 * Constructor.
	 * @param entity the entity
	 * @param strategy the strategy to wrap
	 */
	public EntitySearcher(final EntityDescriptor entity, final IEntitySearchStrategy strategy) {
		this.entity = entity;
		this.strategy = strategy;
	}
	
	/**
	 * Constructor.
	 * @param entity the entity
	 */
	public EntitySearcher(final EntityDescriptor entity) {
		this(entity, determineSearchStrategy(entity));
	}
	
	/**
	 * Determines the search strategy for the specified entity from the application configuration.
	 */
	private static IEntitySearchStrategy determineSearchStrategy(final EntityDescriptor entity) {
		int maxScore = Integer.MIN_VALUE;
		IEntitySearchContributor maxScoreContributor = null;
		for (final IEntitySearchContributor contributor : EntityCapabilities.entitySearchContributorCapability) {
			final int score = contributor.getScore(entity);
			if (score > maxScore) {
				maxScoreContributor = contributor;
				maxScore = score;
			}
		}
		if (maxScoreContributor != null) {
			final IEntitySearchStrategy strategy = maxScoreContributor.getSearchStrategy(entity);
			if (strategy == null) {
				throw new RuntimeException("winning IEntitySearchContributor (" + maxScoreContributor + ") for entity " + entity.getName() + " returned null");
			}
			return strategy;
		} else {
			return null;
		}
	}

	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return entity;
	}
	
	/**
	 * Getter method for the strategy.
	 * @return the strategy
	 */
	public IEntitySearchStrategy getStrategy() {
		return strategy;
	}
	
	/**
	 * Checks whether a search strategy is installed for this entity.
	 * @return true if searching is supported, false if not
	 */
	public boolean isSearchSupported() {
		return (strategy != null);
	}

	/**
	 * Creates an entity list filter for this entity and for the specified search term,
	 * or null if no useful filter can be found for the search term.
	 * @param searchTerm the search term
	 * @return the filter
	 */
	public Predicate createSearchFilter(final String searchTerm) {
		return (strategy == null ? null : strategy.createFilter(entity, searchTerm));
	}
	
}
