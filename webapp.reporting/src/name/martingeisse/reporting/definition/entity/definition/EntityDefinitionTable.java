/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	 * the entitySelectorResolutionRules
	 */
	private List<IEntitySelectorResolutionRule> entitySelectorResolutionRules;

	/**
	 * Constructor.
	 */
	public EntityDefinitionTable() {
		this.databaseTableName = null;
		this.links = new HashMap<String, EntityDefinitionLink>();
		this.propertySets = new HashMap<String, String[]>();
		this.entitySelectorResolutionRules = new ArrayList<IEntitySelectorResolutionRule>();
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

	/**
	 * Getter method for the entitySelectorResolutionRules.
	 * @return the entitySelectorResolutionRules
	 */
	public List<IEntitySelectorResolutionRule> getEntitySelectorResolutionRules() {
		return entitySelectorResolutionRules;
	}

	/**
	 * Setter method for the entitySelectorResolutionRules.
	 * @param entitySelectorResolutionRules the entitySelectorResolutionRules to set
	 */
	public void setEntitySelectorResolutionRules(final List<IEntitySelectorResolutionRule> entitySelectorResolutionRules) {
		this.entitySelectorResolutionRules = entitySelectorResolutionRules;
	}

	/**
	 * Resolves the given entity selector. This method first checks all entity resolution rules,
	 * throwing an exception if more than one rule matches. If no rule matches, this method checks
	 * link names as a fallback (i.e., each link name is an implicitly recognized entity selector).
	 * Returns null if this fails too.
	 * 
	 * @param entitySelector the selector to resolve
	 * @return the resolution result, or null if no rule recognizes the selector and the
	 * selector does not match any link name.
	 */
	public EntitySelectorResolutionRuleResult resolveEntitySelector(String entitySelector) {
		
		// check rules
		EntitySelectorResolutionRuleResult firstResult = null;
		for (IEntitySelectorResolutionRule rule : entitySelectorResolutionRules) {
			EntitySelectorResolutionRuleResult result = rule.resolveEntitySelector(this, entitySelector);
			if (result != null) {
				if (firstResult != null) {
					throw new IllegalStateException("more than one entity resolution rule for table " + databaseTableName + " matches selector " + entitySelector);
				} else {
					firstResult = result;
				}
			}
		}
		if (firstResult != null) {
			return firstResult;
		}
		
		// check link names
		if (links.containsKey(entitySelector)) {
			return new EntitySelectorResolutionRuleResult(entitySelector, true);
		}

		// selector not found
		return null;
		
	}
	
}
