/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.reference;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * Information about a reference between entities. For any property of an
 * entity that refers to the same or another entity, an instance of this
 * class can be obtained to gather information about the reference.
 * 
 * Each instance of this class describes a reference at entity descriptor
 * level, i.e. it is not tied to a specific entity instance, but applies
 * to all instances of the entity.
 * 
 * Instances are created by the framework for the references returned
 * by reference detectors ({@link IEntityReferenceDetector}).
 */
public final class EntityReferenceInfo {

	/**
	 * the source
	 */
	private EntityDescriptor source;

	/**
	 * the sourceFieldName
	 */
	private String sourceFieldName;

	/**
	 * the destination
	 */
	private EntityDescriptor destination;

	/**
	 * the destinationFieldName
	 */
	private String destinationFieldName;

	/**
	 * Constructor.
	 */
	public EntityReferenceInfo() {
	}

	/**
	 * Getter method for the source.
	 * @return the source
	 */
	public EntityDescriptor getSource() {
		return source;
	}

	/**
	 * Setter method for the source.
	 * @param source the source to set
	 */
	public void setSource(final EntityDescriptor source) {
		this.source = source;
	}

	/**
	 * Getter method for the sourceFieldName.
	 * @return the sourceFieldName
	 */
	public String getSourceFieldName() {
		return sourceFieldName;
	}

	/**
	 * Setter method for the sourceFieldName.
	 * @param sourceFieldName the sourceFieldName to set
	 */
	public void setSourceFieldName(final String sourceFieldName) {
		this.sourceFieldName = sourceFieldName;
	}

	/**
	 * Getter method for the destination.
	 * @return the destination
	 */
	public EntityDescriptor getDestination() {
		return destination;
	}

	/**
	 * Setter method for the destination.
	 * @param destination the destination to set
	 */
	public void setDestination(final EntityDescriptor destination) {
		this.destination = destination;
	}

	/**
	 * Getter method for the destinationFieldName.
	 * @return the destinationFieldName
	 */
	public String getDestinationFieldName() {
		return destinationFieldName;
	}

	/**
	 * Setter method for the destinationFieldName.
	 * @param destinationFieldName the destinationFieldName to set
	 */
	public void setDestinationFieldName(final String destinationFieldName) {
		this.destinationFieldName = destinationFieldName;
	}

}
