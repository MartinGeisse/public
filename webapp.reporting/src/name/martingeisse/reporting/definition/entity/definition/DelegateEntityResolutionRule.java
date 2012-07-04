/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity.definition;

/**
 * This rule delegates entity resolution to a linked table.
 */
public final class DelegateEntityResolutionRule implements IEntitySelectorResolutionRule {

	/**
	 * the selector
	 */
	private final String selector;

	/**
	 * the linkName
	 */
	private final String linkName;

	/**
	 * Constructor.
	 * @param selector the selector to delegate
	 * @param linkName the name of the link to use for delegation
	 */
	public DelegateEntityResolutionRule(final String selector, final String linkName) {
		this.selector = selector;
		this.linkName = linkName;
	}

	/**
	 * Getter method for the selector.
	 * @return the selector
	 */
	public String getSelector() {
		return selector;
	}

	/**
	 * Getter method for the linkName.
	 * @return the linkName
	 */
	public String getLinkName() {
		return linkName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.entity.definition.IEntitySelectorResolutionRule#resolveEntitySelector(name.martingeisse.reporting.definition.entity.definition.EntityDefinitionTable, java.lang.String)
	 */
	@Override
	public EntitySelectorResolutionRuleResult resolveEntitySelector(EntityDefinitionTable currentTable, String entitySelector) {
		return (this.selector.equals(entitySelector) ? new EntitySelectorResolutionRuleResult(linkName, false) : null);
	}

}
