/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity.definition;

/**
 * This class represents a link from one table to another in an
 * entity definition.
 */
public final class EntityDefinitionLink {

	/**
	 * the destinationTable
	 */
	private EntityDefinitionTable destinationTable;

	/**
	 * the sourceTableKeyColumnName
	 */
	private String sourceTableKeyColumnName;

	/**
	 * the destinationTableKeyColumnName
	 */
	private String destinationTableKeyColumnName;

	/**
	 * Constructor.
	 */
	public EntityDefinitionLink() {
	}

	/**
	 * Constructor.
	 * @param destinationTable the table to link to
	 * @param sourceTableKeyColumnName the name of the key column in the source table
	 * @param destinationTableKeyColumnName the name of the key column in the destination table
	 */
	public EntityDefinitionLink(final EntityDefinitionTable destinationTable, final String sourceTableKeyColumnName, final String destinationTableKeyColumnName) {
		this.destinationTable = destinationTable;
		this.sourceTableKeyColumnName = sourceTableKeyColumnName;
		this.destinationTableKeyColumnName = destinationTableKeyColumnName;
	}

	/**
	 * Getter method for the destinationTable.
	 * @return the destinationTable
	 */
	public final EntityDefinitionTable getDestinationTable() {
		return destinationTable;
	}

	/**
	 * Setter method for the destinationTable.
	 * @param destinationTable the destinationTable to set
	 */
	public final void setDestinationTable(final EntityDefinitionTable destinationTable) {
		this.destinationTable = destinationTable;
	}

	/**
	 * Getter method for the sourceTableKeyColumnName.
	 * @return the sourceTableKeyColumnName
	 */
	public final String getSourceTableKeyColumnName() {
		return sourceTableKeyColumnName;
	}

	/**
	 * Setter method for the sourceTableKeyColumnName.
	 * @param sourceTableKeyColumnName the sourceTableKeyColumnName to set
	 */
	public final void setSourceTableKeyColumnName(final String sourceTableKeyColumnName) {
		this.sourceTableKeyColumnName = sourceTableKeyColumnName;
	}

	/**
	 * Getter method for the destinationTableKeyColumnName.
	 * @return the destinationTableKeyColumnName
	 */
	public final String getDestinationTableKeyColumnName() {
		return destinationTableKeyColumnName;
	}

	/**
	 * Setter method for the destinationTableKeyColumnName.
	 * @param destinationTableKeyColumnName the destinationTableKeyColumnName to set
	 */
	public final void setDestinationTableKeyColumnName(final String destinationTableKeyColumnName) {
		this.destinationTableKeyColumnName = destinationTableKeyColumnName;
	}

}
