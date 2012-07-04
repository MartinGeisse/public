/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity.definition;

/**
 * This class represents the results from {@link IEntitySelectorResolutionRule}.
 * It contains the name of a link to follow and a flag that tells the caller
 * whether the link target is the destination table. If it isn't, the caller
 * shall ask the link target to resolve the entity selector.
 */
public final class EntitySelectorResolutionRuleResult {

	/**
	 * the linkName
	 */
	private final String linkName;

	/**
	 * the destinationTable
	 */
	private final boolean destinationTable;

	/**
	 * Constructor.
	 * @param linkName the name of the link to follow
	 * @param destinationTable whether the link target is the destination table or just
	 * a delegate resolver.
	 */
	public EntitySelectorResolutionRuleResult(final String linkName, final boolean destinationTable) {
		this.linkName = linkName;
		this.destinationTable = destinationTable;
	}

	/**
	 * Getter method for the linkName.
	 * @return the linkName
	 */
	public String getLinkName() {
		return linkName;
	}

	/**
	 * Getter method for the destinationTable.
	 * @return the destinationTable
	 */
	public boolean isDestinationTable() {
		return destinationTable;
	}

}
