/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the definition of an entity that can be used
 * in report definitions.
 */
public final class EntityDefinition {

	/**
	 * the name
	 */
	private String name;

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
	public EntityDefinition() {
		this.name = null;
		this.databaseTableName = null;
		this.links = new HashMap<String, EntityDefinitionLink>();
		this.propertySets = new HashMap<String, String[]>();
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Getter method for the databaseTableName.
	 * @return the databaseTableName
	 */
	public String getDatabaseTableName() {
		return databaseTableName;
	}

	/**
	 * Setter method for the databaseTableName.
	 * @param databaseTableName the databaseTableName to set
	 */
	public void setDatabaseTableName(final String databaseTableName) {
		this.databaseTableName = databaseTableName;
	}

	/**
	 * Getter method for the links.
	 * @return the links
	 */
	public Map<String, EntityDefinitionLink> getLinks() {
		return links;
	}

	/**
	 * Setter method for the links.
	 * @param links the links to set
	 */
	public void setLinks(final Map<String, EntityDefinitionLink> links) {
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
