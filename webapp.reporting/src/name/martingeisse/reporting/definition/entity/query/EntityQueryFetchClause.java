/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity.query;

import name.martingeisse.reporting.definition.entity.EntityPath;

/**
 * Represents a "fetch" clause in an entity query or nested within
 * another fetch clause.
 * 
 * A fetch clause consists of
 * - an entity path that gets resolved by entity resolution routes
 * - an optional property selector array that specifies the properties and
 *   property sets to fetch. If no property selector array is present, all
 *   properties will be fetched. (TODO)
 * - row filters that accept or reject fetched rows. If no such filter
 *   is present, all rows will be accepted. (TODO)
 * - zero or more fetch sub-clauses that specify sub-queries to start from
 *   the leaf entity of this fetch clause. (TODO)
 *   
 * Note that property selectors, filters, and sub-fetches apply to the entity
 * selected by the last segment of the entity path. To apply any of these to
 * other segments of the path, split the path by using nested fetch clauses.
 */
public final class EntityQueryFetchClause {

	/**
	 * the path
	 */
	private EntityPath path;

	/**
	 * the propertySelectors
	 */
	private String[] propertySelectors;

	/**
	 * Constructor.
	 */
	public EntityQueryFetchClause() {
	}

	/**
	 * Constructor.
	 * @param path the path of the table to fetch
	 */
	public EntityQueryFetchClause(final EntityPath path) {
		this.path = path;
	}

	/**
	 * Constructor.
	 * @param path the path of the table to fetch
	 */
	public EntityQueryFetchClause(final String path) {
		this.path = new EntityPath(path);
	}

	/**
	 * Getter method for the path.
	 * @return the path
	 */
	public EntityPath getPath() {
		return path;
	}

	/**
	 * Setter method for the path.
	 * @param path the path to set
	 */
	public void setPath(final EntityPath path) {
		this.path = path;
	}

	/**
	 * Getter method for the propertySelectors.
	 * @return the propertySelectors
	 */
	public String[] getPropertySelectors() {
		return propertySelectors;
	}

	/**
	 * Setter method for the propertySelectors.
	 * @param propertySelectors the propertySelectors to set
	 */
	public void setPropertySelectors(final String[] propertySelectors) {
		this.propertySelectors = propertySelectors;
	}

}
