/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity.definition;


/**
 * This interface represents a rule that resolves an entity selector --
 * a segment from an entity path, used in fetch clauses -- at an
 * entity table.
 * 
 * Note that {@link EntityDefinitionTable} uses an implicit fallback
 * rule that recognizes a link whose name is the entity selector
 * and returns the target table of the link as the destination table
 * of the selector.
 */
public interface IEntitySelectorResolutionRule {

	/**
	 * Resolves the given entity selector.
	 * @param currentTable the table to resolve from
	 * @param entitySelector the selector to resolve
	 * @return the resolution result, or null if this rule does not recognize the selector
	 */
	public EntitySelectorResolutionRuleResult resolveEntitySelector(EntityDefinitionTable currentTable, String entitySelector);
	
}
