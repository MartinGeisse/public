/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

/**
 * Represents a "fetch" clause in an entity query.
 */
public class EntityQueryFetchClause {

	/**
	 * the path
	 */
	private EntityPath path;

	/**
	 * Constructor.
	 */
	public EntityQueryFetchClause() {
	}

	/**
	 * Constructor.
	 * @param path the path of the table to fetch
	 */
	public EntityQueryFetchClause(EntityPath path) {
		this.path = path;
	}

	/**
	 * Constructor.
	 * @param path the path of the table to fetch
	 */
	public EntityQueryFetchClause(String path) {
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

}
