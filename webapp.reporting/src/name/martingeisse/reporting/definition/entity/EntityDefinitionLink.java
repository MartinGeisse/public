/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

/**
 * This class represents a link from one table to another in an
 * entity definition.
 */
public final class EntityDefinitionLink {

	/**
	 * the destination
	 */
	private EntityDefinition destination;

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
	 * @param destination the entity to link to
	 * @param sourceTableKeyColumnName the name of the key column in the source table
	 * @param destinationTableKeyColumnName the name of the key column in the destination table
	 */
	public EntityDefinitionLink(final EntityDefinition destination, final String sourceTableKeyColumnName, final String destinationTableKeyColumnName) {
		this.destination = destination;
		this.sourceTableKeyColumnName = sourceTableKeyColumnName;
		this.destinationTableKeyColumnName = destinationTableKeyColumnName;
	}

	/**
	 * Getter method for the destination.
	 * @return the destination
	 */
	public EntityDefinition getDestination() {
		return destination;
	}

	/**
	 * Setter method for the destination.
	 * @param destination the destination to set
	 */
	public void setDestination(final EntityDefinition destination) {
		this.destination = destination;
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
