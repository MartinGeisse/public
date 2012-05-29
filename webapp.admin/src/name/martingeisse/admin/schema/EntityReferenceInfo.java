/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.schema;

/**
 * Information about a reference between entities. For any property of an
 * entity that refers to the same or another entity, an instance of this
 * class can be obtained to gather information about the reference.
 * 
 * Each instance of this class describes a reference at entity descriptor
 * level, i.e. it is not tied to a specific entity instance, but applies
 * to all instances of the entity.
 */
public class EntityReferenceInfo {

	/**
	 * the source
	 */
	private EntityDescriptor source;
	
	/**
	 * the destination
	 */
	private EntityDescriptor destination;
	
	/**
	 * the fieldName
	 */
	private String fieldName;
	
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
	public void setSource(EntityDescriptor source) {
		this.source = source;
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
	public void setDestination(EntityDescriptor destination) {
		this.destination = destination;
	}

	/**
	 * Getter method for the fieldName.
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Setter method for the fieldName.
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
}
