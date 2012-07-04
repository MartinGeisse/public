/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity.definition;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a database table in an entity definition.
 */
public final class EntityDefinitionTable {

	/**
	 * the databaseTableName
	 */
	private String databaseTableName;

	/**
	 * the links
	 */
	private Map<String, EntityDefinitionLink> links;

	/**
	 * the propertySets
	 */
	private Map<String, String[]> propertySets;

	/**
	 * Constructor.
	 */
	public EntityDefinitionTable() {
		this.databaseTableName = null;
		this.links = new HashMap<String, EntityDefinitionLink>();
		this.propertySets = new HashMap<String, String[]>();
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

	/**
	 * Getter method for the propertySets.
	 * @return the propertySets
	 */
	public Map<String, String[]> getPropertySets() {
		return propertySets;
	}

	/**
	 * Setter method for the propertySets.
	 * @param propertySets the propertySets to set
	 */
	public void setPropertySets(final Map<String, String[]> propertySets) {
		this.propertySets = propertySets;
	}

}
