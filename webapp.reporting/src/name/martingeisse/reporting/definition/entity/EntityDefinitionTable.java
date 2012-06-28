/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a database table in an entity definition.
 */
public class EntityDefinitionTable {

	/**
	 * the databaseTableName
	 */
	private String databaseTableName;

	/**
	 * the links
	 */
	private Map<String, EntityDefinitionLink> links;

	/**
	 * Constructor.
	 */
	public EntityDefinitionTable() {
		this.links = new HashMap<String, EntityDefinitionLink>();
	}

	/**
	 * Getter method for the databaseTableName.
	 * @return the databaseTableName
	 */
	public final String getDatabaseTableName() {
		return databaseTableName;
	}

	/**
	 * Setter method for the databaseTableName.
	 * @param databaseTableName the databaseTableName to set
	 */
	public final void setDatabaseTableName(final String databaseTableName) {
		this.databaseTableName = databaseTableName;
	}

	/**
	 * Getter method for the links.
	 * @return the links
	 */
	public final Map<String, EntityDefinitionLink> getLinks() {
		return links;
	}

	/**
	 * Setter method for the links.
	 * @param links the links to set
	 */
	public final void setLinks(final Map<String, EntityDefinitionLink> links) {
		this.links = links;
	}

}
