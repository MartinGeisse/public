/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.reference;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.lowlevel.ILowlevelDatabaseStructure;

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
	 * sourcePropertyName
	 */
	private final String sourcePropertyName;
	
	/**
	 * the sourceMultiplicity
	 */
	private final EntityReferenceEndpointMultiplicity sourceMultiplicity;

	/**
	 * the destinationEntityName
	 */
	private final String destinationEntityName;
	
	/**
	 * the destinationPropertyName
	 */
	private final String destinationPropertyName;
	
	/**
	 * the destinationMultiplicity
	 */
	private final EntityReferenceEndpointMultiplicity destinationMultiplicity;

	/**
	 * Constructor.
	 * @param sourceEntityName the name of the referencing entity, or null to match any referencing entity
	 * @param sourcePropertyName the "known" property name that marks references
	 * @param sourceMultiplicity the multiplicity of the reference on the "source" side
	 * @param destinationEntityName the name of the destination entity
	 * @param destinationPropertyName the name of the key property in the destination entity
	 * @param destinationMultiplicity the multiplicity of the reference on the "destination" side
	 */
	public FixedNameEntityReferenceDetector(final String sourceEntityName, final String sourcePropertyName, final EntityReferenceEndpointMultiplicity sourceMultiplicity, final String destinationEntityName, final String destinationPropertyName, EntityReferenceEndpointMultiplicity destinationMultiplicity) {
		this.sourceEntityName = sourceEntityName;
		this.sourcePropertyName = sourcePropertyName;
		this.sourceMultiplicity = sourceMultiplicity;
		this.destinationEntityName = destinationEntityName;
		this.destinationPropertyName = destinationPropertyName;
		this.destinationMultiplicity = destinationMultiplicity;
	}

	/**
	 * Getter method for the sourceEntityName.
	 * @return the sourceEntityName
	 */
	public String getSourceEntityName() {
		return sourceEntityName;
	}

	/**
	 * Getter method for the sourcePropertyName.
	 * @return the sourcePropertyName
	 */
	public String getSourcePropertyName() {
		return sourcePropertyName;
	}

	/**
	 * Getter method for the sourceMultiplicity.
	 * @return the sourceMultiplicity
	 */
	public EntityReferenceEndpointMultiplicity getSourceMultiplicity() {
		return sourceMultiplicity;
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
	
	/**
	 * Getter method for the destinationMultiplicity.
	 * @return the destinationMultiplicity
	 */
	public EntityReferenceEndpointMultiplicity getDestinationMultiplicity() {
		return destinationMultiplicity;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.reference.IEntityReferenceDetector#detectEntityReference(name.martingeisse.admin.entity.schema.ApplicationSchema, name.martingeisse.admin.entity.schema.lowlevel.ILowlevelDatabaseStructure, name.martingeisse.admin.entity.schema.EntityDescriptor, java.lang.String)
	 */
	@Override
	public void detectEntityReference(ApplicationSchema schema, ILowlevelDatabaseStructure lowlevelDatabaseStructure, EntityDescriptor entity, String propertyName) {
		if (sourceEntityName == null || sourceEntityName.equals(entity.getName())) {
			if (propertyName.equals(sourcePropertyName)) {
				EntityDescriptor destinationEntity = schema.findOptionalEntity(destinationEntityName);
				EntityReferenceEndpoint near = EntityReferenceEndpoint.createPair(entity, sourcePropertyName, sourceMultiplicity, destinationEntity, destinationPropertyName, destinationMultiplicity);
				entity.getReferenceEndpoints().add(near);
				destinationEntity.getReferenceEndpoints().add(near.getOther());
			}
		}
	}

}
