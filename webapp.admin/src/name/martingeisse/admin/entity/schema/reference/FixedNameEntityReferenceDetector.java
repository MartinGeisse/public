/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.reference;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * Detects an entity reference whenever a property name matches a fixed
 * string, with a fixed destination entity. The detector can optionally
 * be restricted to a single referencing (source) entity.
 * 
 * If the key property in the destination entity is not its ID,
 * then destinationPropertyName specifies that property.
 */
public final class FixedNameEntityReferenceDetector extends AbstractEntityReferenceDetector {

	/**
	 * the sourceEntityName
	 */
	private final String sourceEntityName;

	/**
	 * the name
	 */
	private final String knownPropertyName;

	/**
	 * the destinationEntityName
	 */
	private final String destinationEntityName;
	
	/**
	 * the destinationPropertyName
	 */
	private final String destinationPropertyName;

	/**
	 * Constructor.
	 * @param sourceEntityName the name of the referencing entity, or null to match any referencing entity
	 * @param knownPropertyName the "known" property name that marks references
	 * @param destinationEntityName the name of the destination entity
	 * @param destinationPropertyName the name of the key property in the destination entity
	 */
	public FixedNameEntityReferenceDetector(final String sourceEntityName, final String knownPropertyName, final String destinationEntityName, final String destinationPropertyName) {
		this.sourceEntityName = sourceEntityName;
		this.knownPropertyName = knownPropertyName;
		this.destinationEntityName = destinationEntityName;
		this.destinationPropertyName = destinationPropertyName;
	}

	/**
	 * Getter method for the sourceEntityName.
	 * @return the sourceEntityName
	 */
	public String getSourceEntityName() {
		return sourceEntityName;
	}

	/**
	 * Getter method for the knownPropertyName.
	 * @return the knownPropertyName
	 */
	public String getKnownPropertyName() {
		return knownPropertyName;
	}

	/**
	 * Getter method for the destinationEntityName.
	 * @return the destinationEntityName
	 */
	public String getDestinationEntityName() {
		return destinationEntityName;
	}
	
	/**
	 * Getter method for the destinationPropertyName.
	 * @return the destinationPropertyName
	 */
	public String getDestinationPropertyName() {
		return destinationPropertyName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.reference.IEntityReferenceDetector#detectEntityReference(name.martingeisse.admin.entity.schema.ApplicationSchema, name.martingeisse.admin.entity.schema.EntityDescriptor, java.lang.String)
	 */
	@Override
	public EntityReferenceInfo detectEntityReference(ApplicationSchema schema, EntityDescriptor entity, String propertyName) {
		if (sourceEntityName == null || sourceEntityName.equals(entity.getName())) {
			if (propertyName.equals(knownPropertyName)) {
				EntityReferenceInfo reference = new EntityReferenceInfo();
				reference.setSource(entity);
				reference.setSourceFieldName(propertyName);
				reference.setDestination(schema.findEntity(destinationEntityName));
				reference.setDestinationFieldName(destinationPropertyName == null ? reference.getDestination().getIdColumnName() : destinationPropertyName);
				return reference;
			}
		}
		return null;
	}

}
